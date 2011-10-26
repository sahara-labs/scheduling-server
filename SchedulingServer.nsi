/**
 * SAHARA Installer
 * 
 * Sahara installer/uninstaller script for Windows.  
 
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2010, University of Technology, Sydney
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of the University of Technology, Sydney nor the names 
 *    of its contributors may be used to endorse or promote products derived from 
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Tejaswini Deshpande (tdeshpan)
 * @author Michael Diponio (mdiponio)
 * @date 12th March 2010
 */
 
; -----------------------------------------------------------------------------
; --- SchedulingServer.nsi                                                  ---
; -----------------------------------------------------------------------------
!include MUI2.nsh
!include TextFunc.nsh
!include nsDialogs.nsh
!include LogicLib.nsh
!include WordFunc.nsh
!include Sections.nsh

!define SF_UNSELECTED  0


; The name of the installer
Name "SAHARA Labs :: Scheduling Server"

!define REGKEY "SOFTWARE\Sahara Scheduling Server"

; SAHARA Labs Scheduling Server Version
!define Version "4.0-capstone"

!define JREVersion "1.6"

; The file to write
OutFile "package/SchedulingServer-${Version}-${Arch}.exe"

; The default installation directory
InstallDir "C:\Program Files\Sahara"

BrandingText "$(^Name)"
WindowIcon off
XPStyle On
Var skipSection

; -----------------------------------------------------------------------------
; --- Interface Settings                                                    ---
;------------------------------------------------------------------------------
!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_BITMAP "installer\labshare.bmp"

!define MUI_ICON "installer\labshare.ico"
!define MUI_ABORTWARNING
  
!define MUI_UNICON "installer\win-install.ico"


!define Sahara_SSWindows_Service "Scheduling Server"

; -----------------------------------------------------------------------------
; --- Pages                                                                 ---
; -----------------------------------------------------------------------------
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "License"
Page custom getInstallChoice selectionDone
!define MUI_PAGE_CUSTOMFUNCTION_Pre ComponentPagePre
!insertmacro MUI_PAGE_COMPONENTS

Var DirHeaderText
Var DirHeaderSubText
!define MUI_PAGE_HEADER_TEXT    "$DirHeaderText"
!define MUI_PAGE_HEADER_SUBTEXT "$DirHeaderSubText"

!define MUI_PAGE_CUSTOMFUNCTION_Pre DirectoryPagePre
!define MUI_PAGE_CUSTOMFUNCTION_SHOW DirectoryPageShow
!insertmacro MUI_PAGE_DIRECTORY
!define MUI_PAGE_CUSTOMFUNCTION_Pre SetInstallDir
!insertmacro MUI_PAGE_INSTFILES

; -----------------------------------------------------------------------------
; --- Custom pages for database setup and the required variables            ---
; -----------------------------------------------------------------------------
Var Dialog
Var InfoLabel
Var SSInstallClick
Var DBSetupClick
Var Checkbox_State
Var DBDir
Var DBExec
Var DBUser
Var Host
Var DBUserPass
Var DBNameTemp
Var DBName
Var DBStatus
Var UserStatus
Var PrivilegesStatus
Var SchemaStatus
Var SampleDataStatus
Var DBSetupLog
Var DBDirText
Var DBType
Var DBTypeStr
Var DBSetupOnly

Page custom confirmDB confirmDBPost
Page custom dbSetup
Page custom setupDatabase setupDatabasePost
PageEx directory
    PageCallbacks DDBDirectoryPre DDBDirectoryShow checkDB
    DirText $DBDirText
    DirVar $DBDir
PageExEnd
Page custom getDBDetails getDetails
Page custom createDB
Page custom printSummary revertBack

Var finishTitle
Var finishText
!define MUI_FINISHPAGE_TITLE "$finishTitle"
!define MUI_FINISHPAGE_TEXT "$finishText"
!insertmacro MUI_PAGE_FINISH

!insertmacro MUI_UNPAGE_CONFIRM
!define MUI_PAGE_CUSTOMFUNCTION_LEAVE un.CheckSlectedComponents
!insertmacro MUI_UNPAGE_COMPONENTS
!insertmacro MUI_UNPAGE_INSTFILES

; -----------------------------------------------------------------------------
; --- Language                                                              ---
; -----------------------------------------------------------------------------
!insertmacro MUI_LANGUAGE "English"

Var ExitInstall
Var UpgradeSelected
Var DisplayText
Var NoSectionSelectedUninstall 
Var SSAlreadyInstalled ;NI=Not installed, V2=2.x version installed, SAME=installed and same version, 
                       ;V3=Major version 3 is installed but with different minor version/build,
                       ;OTHER=some other version than thsi version or 2.x is installed
					   
; -----------------------------------------------------------------------------
; --- Pre install actions                                                   ---
; -----------------------------------------------------------------------------
Function DirectoryPagePre
    ${If} $DBSetupOnly S== "true"
        Abort
    ${EndIf}
	; If there is already an installation of Sahara, use the same folder for this installation. Else let the user select the installation folder
 	${If} $SSAlreadyInstalled S== "NI"
		StrCpy $DirHeaderText "Choose Install Location"
		StrCpy $DirHeaderSubText "Choose the folder in which to install $(^Name)"
	${Else} 
		StrCpy $DirHeaderText "Using existing $(^Name) installation folder"
		StrCpy $DirHeaderSubText "$(^Name) is already installed on this machine. Installer will upgarde the existing installation"
	${EndIf}

FunctionEnd

Function ComponentPagePre
    ${If} $DBSetupOnly S== "true"
        Abort
    ${EndIf}    

FunctionEnd

Function CheckSSVersion
    Var /GLOBAL SSVersion
    Push $R0
    ReadRegStr $SSVersion HKLM "${REGKEY}" "CurrentVersion"
    StrCpy $R0 $SSVersion 1
    ${If} $R0 S== ""
        ; SS not installed. Clean install
        StrCpy $SSAlreadyInstalled "NI"
    ${ElseIf} $R0 S== "2"
        ; SS version 2.x is installed. Can be upgraded
        StrCpy $SSAlreadyInstalled "V2"
    ${ElseIf} $SSVersion S== ${Version}
        ; Same SS version is installed. No action
        StrCpy $SSAlreadyInstalled "SAME"
    ${ElseIf} $SSVersion S<= ${Version}
        ; SS version 3.x is installed (with different minor version or build). Can be upgraded
        StrCpy $SSAlreadyInstalled "V3"
    ${Else}
        ; Some other version is installed
        StrCpy $SSAlreadyInstalled "OTHER"
    ${EndIf}
    ReadRegStr $R0 HKLM "${REGKEY}" "Path"
    ${IfNot} $R0 S== ""
        StrCpy $INSTDIR $R0
    ${EndIf}
    Pop $R0
FunctionEnd


Function DirectoryPageShow
	; If there is already an installation of Sahara, disable the destination folder selection and use the same folder for this installation. 
	; Else let the user select the installation folder
    
    ${If} $SSAlreadyInstalled S== "V2" 
    ${OrIf} $SSAlreadyInstalled S== "V3"
		; Disable the page
		FindWindow $R0 "#32770" "" $HWNDPARENT
		GetDlgItem $R1 $R0 1019
		SendMessage $R1 ${EM_SETREADONLY} 1 0
		EnableWindow $R1 0
		GetDlgItem $R1 $R0 1001
		EnableWindow $R1 0
	${EndIf}
FunctionEnd

Function SetInstallDir
    ${If} $DBSetupOnly S== "true"
        Abort
    ${EndIf}

	${If} $SSAlreadyInstalled S== "NI"
		StrCpy $INSTDIR "$INSTDIR\SchedulingServer"
	${EndIf}
FunctionEnd

Function .onInit
    InitPluginsDir
    File /oname=$PLUGINSDIR\tick.bmp "installer\tick.bmp"
    File /oname=$PLUGINSDIR\cross.bmp "installer\cross.bmp"
    ; Sample data file for installer
    File /oname=$PLUGINSDIR\sampleDataMysql.sql "installer\sampleDataMysql.sql"
    File /oname=$PLUGINSDIR\sampleDataMssql.sql "installer\sampleDataMssql.sql"

	; Splash screen 
	advsplash::show 1000 1000 1000 -1 ..\installer\labshare
 	StrCpy $skipSection "false"
 	StrCpy $SSAlreadyInstalled "-1"
	call CheckSSVersion
	${If} $SSAlreadyInstalled S== "OTHER"
		MessageBox MB_OK|MB_ICONSTOP "A different version of is already installed and there is no upgrade path from the installers version.$\nThis may be because the installed version is newer than version ${Version}.$\n$\nIf you want to install version ${Version}, please uninstall the existing Scheduling Server before$\nreattempting installation."
		Abort 
	${EndIf}
    StrCpy $DBTypeStr "-- Select database --"
    StrCpy $DBType $DBTypeStr
    StrCpy $DBSetupOnly "false"
FunctionEnd

Function checkJREVersion
	; Check the JRE version to be 1.6 or higher
	
	${If} ${Arch} == "x64"
		; If we are a 64 bit installer, we need to use a 64 bit JVM which 
		; will have its keys in the 64 bit registry.
		SetRegView 64
	${EndIf}
	
	ReadRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" CurrentVersion 
	
	${If} ${Arch} == "x64"
		; Restore the registry back to 32 view
		SetRegView 32
	${EndIf}
	
	${If} $0 == ""
	${AndIf} ${Arch} == "x64"
		; It's possible a 32 bit JVM is installed, so we should alert the user to
		; install a 64 bit JVM if this is the case.
		ReadRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" CurrentVersion 
		${If} $0 != ""
			MessageBox MB_OK|MB_ICONSTOP "As this is the 64 bit Scheduing Server, 64 bit Java (version 1.6 or later) needs to installed. Alternatively, the 32 bit Scheduling Server may be used.$\n$\nAborting the installation."
		${Else}
			MessageBox MB_OK|MB_ICONSTOP "Scheduling Server needs Java (version 1.6 or later) installed. None has been detected. $\n$\nAborting the installation."
		${EndIf}
		Abort
	${ElseIf} $0 == ""
		; No version detected
		MessageBox MB_OK|MB_ICONSTOP "Scheduling Server needs Java (version 1.6 or later) installed. None has been detected. $\n$\nAborting the installation."
		Abort
	${ElseIf} $0 S< ${JREVersion} 
		; Earlier version detected
		MessageBox MB_OK|MB_ICONSTOP "Scheduling Server needs Java version ${JREVersion} or later. Detected version is $0. $\n$\nAborting the installation."
		Abort
	${EndIf}
FunctionEnd

; Check if RigClient service is running
Function checkIfServiceInstalled
	;ReadEnvStr $R0 COMSPEC

	; Check if the RigClient service is already installed. (probably need a better way to do this)
	; If the service is installed, the output of 'sc query RigClient' will be like:
	;		SERVICE_NAME: RigClient
        ;		TYPE               : 10  WIN32_OWN_PROCESS
        ;		STATE              : 1  STOPPED
        ;              		          (NOT_STOPPABLE,NOT_PAUSABLE,IGNORES_SHUTDOWN)
        ;		WIN32_EXIT_CODE    : 1077       (0x435)
        ;		SERVICE_EXIT_CODE  : 0  (0x0)
        ;		CHECKPOINT         : 0x0
        ;		WAIT_HINT          : 0x0
	; If the service is not installed, the output will be like:
	;		[SC] EnumQueryServicesStatus:OpenService FAILED 1060:
	;		The specified service does not exist as an installed service.
	; Checking for the word 'FAILED' in the output of the above command
	;
	; - If function "WordReplace" is successful (the word "FAILED" is found in the result), the service is not installed
	; - If the function "WordReplace" is not successful and the errorlevel (value of $R0) is 1 (the word "FAILED" not found), the service is installed
	; - If the function "WordReplace" is not successful and the errorlevel (value of $R0) is anything other than 1 then some error has occured in
	;   executing function "WordReplace"
	
	nsExec::ExecToStack /OEM '"sc" query "${Sahara_SSWindows_Service}"'
	Pop $0	; $0 contains return value/error/timeout
	Pop $1	; $1 contains printed text, up to ${NSIS_MAX_STRLEN}
	
	ClearErrors
	${WordReplace} '$1' 'FAILED' 'FAILED' 'E+1' $R0
	IfErrors 0 Found
	StrCmp $R0 '1' 0 Error
	MessageBox MB_OK|MB_ICONSTOP "'${Sahara_SSWindows_Service}' service is already installed.  please stop the '${Sahara_SSWindows_Service}' service if it is running (Windows Control Panel->Administrative Tools->Services) $\n$\nUse schedulingservice.exe to uninstall the previous version (schedulingservice.exe uninstall)"
	Abort
	Error:
	MessageBox MB_OK|MB_ICONSTOP "Error in detecting if '${Sahara_SSWindows_Service}' service is installed"
	Abort
	Found:
FunctionEnd

; Check if RigClient service is running
!macro checkIfServiceRunning
	; Check if the RigClient service is running. (probably need a better way to do this)
	; If the service is running, the output of 'sc query RigClient' will be like:
	;	SERVICE_NAME: RigClient
	;	        TYPE               : 10  WIN32_OWN_PROCESS
	;	        STATE              : 4  RUNNING
        ;       		                (STOPPABLE,NOT_PAUSABLE,ACCEPTS_SHUTDOWN)
	;	        WIN32_EXIT_CODE    : 0  (0x0)
	;	        SERVICE_EXIT_CODE  : 0  (0x0)
	;	        CHECKPOINT         : 0x0
	;	        WAIT_HINT          : 0x0
	;
	; If the service is installed but not running, the STATE would be 
	;		SERVICE_NAME: RigClient
        ;		TYPE               : 10  WIN32_OWN_PROCESS
        ;		STATE              : 1  STOPPED
        ;              		          (NOT_STOPPABLE,NOT_PAUSABLE,IGNORES_SHUTDOWN)
        ;		WIN32_EXIT_CODE    : 1077       (0x435)
        ;		SERVICE_EXIT_CODE  : 0  (0x0)
        ;		CHECKPOINT         : 0x0
        ;		WAIT_HINT          : 0x0
        ;
        ; If the service is not installed, the STATE would be 
	;		[SC] EnumQueryServicesStatus:OpenService FAILED 1060:
	;		The specified service does not exist as an installed service.
	;
	; Check for the word 'RUNNING' in the output of the above command. If not found, 
	;
	; - If function "WordReplace" is successful (the word "RUNNING" is found in the result), the service is running
	; - If the function "WordReplace" is not successful and the errorlevel (value of $R0) is 1 (the word "RUNNING" not found), the service is stopped or not installed
	; - If the function "WordReplace" is not successful and the errorlevel (value of $R0) is anything other than 1 then some error has occured in
	;   executing function "WordReplace"

	nsExec::ExecToStack /OEM '"sc" query "${Sahara_SSWindows_Service}"'
	Pop $0	; $0 contains return value/error/timeout
	Pop $1	; $1 contains printed text, up to ${NSIS_MAX_STRLEN}

	ClearErrors
	${WordReplace} '$1' 'RUNNING' 'RUNNING' 'E+1' $R0
	IfErrors Errors 0
	MessageBox MB_OK|MB_ICONSTOP "Please stop the '${Sahara_SSWindows_Service}' service before continuing."
	Abort
	Errors:
	StrCmp $R0 '1' NotRunning 0
	MessageBox MB_OK|MB_ICONSTOP "Error is detecting if '${Sahara_SSWindows_Service}' service is installed."
	Abort
	NotRunning:
!macroend

!macro uninstallWindowsService operation
    !insertmacro checkIfServiceRunning
    Push $R1
    ReadRegStr $R1 HKLM "${REGKEY}" "Path"
    ClearErrors
	
	${If} ${Arch} == "x64"
		ExecWait '"$R1\schedulingservice64" uninstall'
	${Else}
		ExecWait '"$R1\schedulingservice" uninstall'
	${EndIf}

    ifErrors 0 WinServiceNoError
    MessageBox MB_ABORTRETRYIGNORE "Error in uninstalling Scheduling Server service.  $\n$\nIf the service is installed, manually \
    uninstall the service from command prompt using: '$R1\schedulingservice uninstall' as admin and press 'Retry'. $\nIf the service is \
    already uninstalled, press 'Ignore'. $\nPress 'Abort' to end the uninstallation" IDABORT AbortUninstall IDIGNORE WinServiceNoError 
    ;TryAgain
	
	${If} ${Arch} == "x64"
		ExecWait '"$R1\schedulingservice64" uninstall'
	${Else}
		ExecWait '"$R1\schedulingservice" uninstall'
	${EndIf}

    ifErrors 0 WinServiceNoError
    MessageBox MB_OK|MB_ICONSTOP "Error in uninstalling Scheduling Server service again. Aborting the ${operation}"
    AbortUninstall:
    Abort
    WinServiceNoError:
    Pop $R1
!macroend

; Disable the section so that user will not be able to select it
; This macro is used mainly for uninstallation. 
; The uninstaller will enable only those sections which are installed. Other sections will be disabled for selection.
!macro disableSection sectionId
	Push $R0
	IntOp $R0 ${SF_UNSELECTED} | ${SF_RO}
	SectionSetFlags ${SectionId} $R0
	Pop $R0
!macroend

; Check if the user has admin rights
!macro checkAdminUser operation
	userInfo::getAccountType
	pop $0
	strCmp $0 "Admin" AdminUser
	MessageBox MB_OK|MB_ICONSTOP "$(^Name) ${operation} requires administrative privileges"
	Abort
	AdminUser:
!macroend

; -----------------------------------------------------------------------------
; --- Install Scheduling Server                                             ---
; -----------------------------------------------------------------------------
Section "Scheduling Server" SchedServer
	!insertmacro checkAdminUser "installation"

    ${If} $SSAlreadyInstalled S== "NI"
	   call checkJREVersion	
	   call checkIfServiceInstalled 

    ${Else}
        ; uninstall the existing service before upgrade
        !insertmacro uninstallWindowsService "upgrade"
        ; sql files were copied to the $INSTDIR in previous version
        ; moving them to schemas directoty to make it consistent with Unix installation
        Delete $INSTDIR\*.sql
        
        ; delete older bundle directory
        RMDir /r $INSTDIR\bundles
        
        SetOutPath $INSTDIR\schemas
        File installer\migrationScriptV2ToV3.sql
        File installer\migrationScriptV2ToV3Postgres.sql
    ${EndIf}
    ; Common steps for installation and upgrade
        
    ; Set output path to the installation directory.
    SetOutPath $INSTDIR\conf 
    File conf\schedulingserver.properties
    File conf\scheduling_service.ini
    ; Copy the component files/directories
    SetOutPath $INSTDIR
    File LICENSE
	
	${If} ${Arch} == "x64"
		File servicewrapper\WindowsServiceWrapper\Release_x64\schedulingservice64.exe
	${Else}
		File servicewrapper\WindowsServiceWrapper\Release\schedulingservice.exe
    ${EndIf}
	
	SetOutPath $INSTDIR\schemas
    File doc\db\schema\*.sql
    
    SetOutPath $INSTDIR\bundles
	File /r /x *.svn bundles\*.*
    SetOutPath $INSTDIR\bin
	File /r /x *.svn bin\*.*
		
    SetOutPath $INSTDIR
    ; Add the Scheduling Server service to the windows services
	
	${If} ${Arch} == "x64"
		ExecWait '"$INSTDIR\schedulingservice64" install'
	${Else}
		ExecWait '"$INSTDIR\schedulingservice" install'
	${Endif}
		
    ifErrors 0 WinServiceInstNoError
    MessageBox MB_OK|MB_ICONSTOP "Error in executing Scheduling Server service."
    ; TODO  Revert back the installed SS in case of error?
    Abort
    WinServiceInstNoError:
    WriteRegStr HKLM "${REGKEY}" Path $INSTDIR
    WriteRegStr HKLM "${REGKEY}" CurrentVersion  ${Version}
    
    StrCpy $finishTitle "Completing $(^Name) Setup Wizard"
    StrCpy $finishText "$(^Name) is installed at $INSTDIR" 
SectionEnd

; -----------------------------------------------------------------------------
; --- Create uninstaller                                                     --
; -----------------------------------------------------------------------------
Section -createUninstaller
	ClearErrors
	EnumRegKey $1 HKLM  "${REGKEY}" 0
	ifErrors 0 createUninstaller
	MessageBox MB_OK|MB_ICONSTOP  "No component selected for installation. Aborting the installation"
	Abort
	createUninstaller:
	SetOutPath $INSTDIR
	WriteUninstaller $INSTDIR\uninstallSchedulingServer.exe
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SchedulingServer" "DisplayName"  "$(^Name)"
	WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\SchedulingServer" "DisplayVersion" "${VERSION}"
	WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\SchedulingServer" "InstallLocation" "$INSTDIR\SchedulingServer"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SchedulingServer" "UninstallString" "$\"$INSTDIR\uninstallSchedulingServer.exe$\""
	WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\SchedulingServer" "NoModify" 1
	WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\SchedulingServer" "NoRepair" 1
SectionEnd

; -----------------------------------------------------------------------------
; --- Functions added for database setup functionality                      ---
; -----------------------------------------------------------------------------

; Function to display installation choice after the license page
Function getInstallChoice
${IfNot} $SSAlreadyInstalled S== "NI" 
    !insertmacro MUI_HEADER_TEXT "Select the installation option" ""
    nsDialogs::Create 1018
    Pop $Dialog
    ${If} $Dialog == error
        Abort
    ${EndIf}
    ${If} $SSAlreadyInstalled S== "V2" 
    ${OrIf} $SSAlreadyInstalled S== "V3"
        ${NSD_CreateLabel} 0 0 100% 15% "Please select one of the following install options:"
        ${NSD_CreateRadioButton} 0 15% 100% 15% "Upgrade the $(^Name) from the existing version $SSVersion to version ${Version}"
        Pop $UpgradeSelected
        ${NSD_CreateRadioButton} 0 30% 100% 15% "Clean install $(^Name) ${Version}"
        Pop $SSInstallClick
        ; By default, select the upgrade option
        ${NSD_SetState} $UpgradeSelected ${BST_CHECKED} 
        
    ${ElseIf} $SSAlreadyInstalled S== "SAME"
        ${NSD_CreateLabel} 0 0 100% 15% "$(^Name) version ${Version} is already installed on this machine.\
        $\nPlease select one of the following install options:"
        ${NSD_CreateRadioButton} 0 15% 100% 15% "Database setup"
        Pop $DBSetupClick
        ${NSD_CreateRadioButton} 0 30% 100% 15% "Exit installation"
        Pop $ExitInstall
        ; By default, select the database setup option
        ${NSD_SetState} $DBSetupClick ${BST_CHECKED}
    ${EndIf}
    nsDialogs::Show 
${EndIf}
FunctionEnd

; This function will be called after getInstallChoice function
Function selectionDone
    ; Check which option is selected
    ${If} $SSAlreadyInstalled S== "V2" 
    ${OrIf} $SSAlreadyInstalled S== "V3"
        ${NSD_GetState} $UpgradeSelected $Checkbox_State
        ${If} $Checkbox_State == ${BST_UNCHECKED} ; Upgrade option not selected
            ${NSD_GetState} $SSInstallClick $Checkbox_State
            ${If} $Checkbox_State == ${BST_CHECKED}
                ; If 'Clean Install' option is selected, abort the installer and display message
                MessageBox MB_ICONINFORMATION|MB_OKCANCEL "To install version ${Version}, please uninstall the existing version of $(^Name) \
                and re-run the installer" IDCANCEL ShowSamePage
                Quit
            ShowSamePage:
                Abort
            ${Else}
                MessageBox MB_OK "Please select one option"
                Abort
            ${EndIf}
        ${EndIf}
    ${ElseIf} $SSAlreadyInstalled S== "SAME"
        ${NSD_GetState} $DBSetupClick $Checkbox_State
        ${If} $Checkbox_State == ${BST_UNCHECKED} ; DB setup option not selected
            ${NSD_GetState} $ExitInstall $Checkbox_State
            ${If} $Checkbox_State == ${BST_CHECKED}
                ; 'Exit installation' option is selected
                MessageBox MB_ICONEXCLAMATION|MB_OKCANCEL "Exiting the $(^Name) installation" IDCANCEL ShowOptionPage
                Quit
            ShowOptionPage:
                Abort
            ${Else}
                MessageBox MB_OK "Please select one option"
                Abort
            ${EndIf}
        ${Else}
            StrCpy $finishTitle "Completing $(^Name) Database Setup Wizard"
            StrCpy $finishText ""      
            ; Instead of GotoDBPage, added a check in all the installer pages
            ; If it is 'Database setup only', other pages will be skipped.
            ; This will allow correctly navigating between pages (specially
            ; using Back button) 
            ;call GotoDBPage
            StrCpy $DBSetupOnly "true" 
        ${EndIf}
    ${EndIf}
FunctionEnd

; Function to confirm whether to proceed with database setup stage
Function confirmDB
    ${If} $DBSetupOnly S== "true"
        Abort
    ${EndIf}     
    
    ${IfNot} $SSAlreadyInstalled S== "NI"
    ${AndIfNot} $SSAlreadyInstalled S== "SAME"
        Abort
    ${EndIf}     
    Var /GLOBAL yesClick
    Var /GLOBAL noClick
    nsDialogs::Create 1018
    Pop $Dialog
    ${If} $Dialog == error
        Abort
    ${EndIf}
    ${NSD_CreateLabel} 0 0 100% 15% "The installer will setup the database for $(^Name). Do you want to continue?"
    ${NSD_CreateRadioButton} 0 15% 100% 15% "Yes"
    Pop $yesClick
    ${NSD_CreateRadioButton} 0 30% 100% 15% "No (Quit the installer)"
    Pop $noClick
    ; By default 'yes' selected
    ${NSD_SetState} $yesClick ${BST_CHECKED}
    nsDialogs::Show
FunctionEnd

; This function will be called after confirmDB function
Function confirmDBPost
    ${NSD_GetState} $yesClick $Checkbox_State
    ${If} $Checkbox_State == ${BST_UNCHECKED}
        ${NSD_GetState} $noClick $Checkbox_State
        ${If} $Checkbox_State == ${BST_CHECKED}
            MessageBox MB_ICONEXCLAMATION|MB_OKCANCEL "Installer will close without setting up the database. $\nYou may run the installer later or setup the database manually." IDCANCEL GoBack
            ; User chose to cancel DB setup. Quit the installer
            Quit
        GoBack:
            ; Go back to the selection page
            Abort            
        ${Else}
            ; No option selected
            MessageBox MB_OK "Please select one option"
            Abort
        ${EndIf}
    ${EndIf}
FunctionEnd

; Start page for DB setup
Function dbSetup
    ${If} $DBSetupOnly S== "true"
        Abort
    ${EndIf}     
    
    ${IfNot} $SSAlreadyInstalled S== "NI"
    ${AndIfNot} $SSAlreadyInstalled S== "SAME"    
        Abort
    ${EndIf}     
    !insertmacro MUI_HEADER_TEXT "Scheduling Server Database setup" ""
    nsDialogs::Create 1018
    Pop $Dialog
    ${If} $Dialog == error
        Abort
    ${EndIf}
    ${NSD_CreateLabel} 0 0 100% 100% "Installer will now start with the database setup. $\n$\n\
    NOTE: Before proceeding with the setup, please ensure that the database \
    is installed (MySQL or Microsoft SQL) and running "
    Pop $InfoLabel
    nsDialogs::Show 
FunctionEnd

;Function for writing messages to log file
Function writeToFile
    Var /GLOBAL textToWrite
    Var /GLOBAL DateVar
    Var /GLOBAL MonthVar
    Var /GLOBAL YearVar
    Var /GLOBAL DayVar
    Var /GLOBAL HourVar
    Var /GLOBAL MinuteVar
    Var /GLOBAL SecondsVar
    Pop $textToWrite ;text to write
    ${GetTime} "" "L" $DateVar $MonthVar $YearVar $DayVar $HourVar $MinuteVar $SecondsVar 
    StrCpy $textToWrite "$\n[DBSetup $DateVar-$MonthVar-$YearVar $HourVar:$MinuteVar:$SecondsVar] $textToWrite" 
    FileOpen $DBSetupLog "$INSTDIR\DatabaseSetup.log" a
    FileSeek $DBSetupLog 0 END 
    FileWrite $DBSetupLog $textToWrite
    FileClose $DBSetupLog
    
FunctionEnd 

!macro writeToLog String
    Push "${String}"
    Call writeToFile
!macroend

; Function to get the database server and login details
Function setupDatabase
    ${IfNot} $SSAlreadyInstalled S== "NI"
    ${AndIfNot} $SSAlreadyInstalled S== "SAME"    
        Abort
    ${EndIf}     
    Var /GLOBAL ComboBox
    Var /GLOBAL GenericControl
    Var /GLOBAL DBAdmin
    Var /GLOBAL DBAdminPass
    Var /GLOBAL DBServer
    Var /GLOBAL DBInstance
    Var /GLOBAL DBUserTemp
    Var /GLOBAL DBPassTemp
    Var /GLOBAL DBServerTemp
    Var /GLOBAL DBInstanceTemp

    !insertmacro MUI_HEADER_TEXT "Scheduling Server Database setup" ""
    
    nsDialogs::Create 1018
    Pop $Dialog

    ${If} $Dialog == error
        Abort
    ${EndIf}
    
    ; Database selection
    ${NSD_CreateGroupBox} 0%  0% 100% 55% "Database server details"
    Pop $InfoLabel
    
    ${NSD_CreateDropList} 5%  10% 30% 10% ""
    Pop $ComboBox
    ${NSD_CB_AddString} $ComboBox "$DBType"
    ${NSD_CB_AddString} $ComboBox "MySQL"
    ${NSD_CB_AddString} $ComboBox "Microsoft SQL"
    ${NSD_CB_SelectString} $ComboBox "$DBType" 
        
    ${NSD_CreateLabel} 5% 25% 30% 15% "Enter servername or IP"
    Pop $GenericControl
    ${NSD_CreateText} 5% 40% 30% 10% "$DBServer"
    Pop $DBServerTemp
    ${NSD_CreateLabel} 40% 25% 50% 15% "Enter the instance name to connect to a named instance (for Microsoft SQL Server)"
    Pop $GenericControl
    ${NSD_CreateText} 40% 40% 30% 10% "$DBInstance"
    Pop $DBInstanceTemp
    ; Database credentials
    ${NSD_CreateGroupBox} 0%  60% 100% 40% "Database administrator login"
    Pop $GenericControl
    ${NSD_CreateLabel} 5%  70% 15% 10% "Username"
    Pop $GenericControl
    ${NSD_CreateText} 25% 70% 30% 10% "$DBAdmin"
    Pop $DBUserTemp
    ${NSD_CreateLabel} 5%  85% 15% 10% "Password"
    Pop $GenericControl
    ${NSD_CreatePassword} 25% 85% 30% 10% "$DBAdminPass"
    Pop $DBPassTemp
    
    nsDialogs::Show
FunctionEnd

; Function to check and get the values entered by user for setupDatabase
Function setupDatabasePost
    ${NSD_GetText} $ComboBox $DBType
    ${If} $DBType S== "$DBTypeStr"
        MessageBox MB_OK|MB_ICONEXCLAMATION "Please select a database"
        Abort
    ${EndIf}
    ${If} $DBType S!= "MySQL"
    ${AndIf} $DBType S!= "Microsoft SQL"
        MessageBox MB_OK|MB_ICONEXCLAMATION 'Invalid database selected.$\nOnly "MySQL" and "Microsoft SQL" databases are supported'
        Abort
    ${EndIf}
    ${NSD_GetText} $DBUserTemp $DBAdmin
    ${NSD_GetText} $DBPassTemp $DBAdminPass
    ${NSD_GetText} $DBServerTemp $DBServer
    ${NSD_GetText} $DBInstanceTemp $DBInstance
 
    ${If} $DBServer S== ""
        MessageBox MB_OK|MB_ICONEXCLAMATION "Please enter database server"
        Abort
    ${EndIf}
    ${If} $DBAdmin S== ""
        MessageBox MB_OK|MB_ICONEXCLAMATION "Please enter database administrator login details"
        Abort
    ${EndIf}
FunctionEnd

Function DDBDirectoryPre
    ${IfNot} $SSAlreadyInstalled S== "NI"
    ${AndIfNot} $SSAlreadyInstalled S== "SAME"    
        Abort
    ${EndIf}   
    StrCpy $DBDirText "Select the location of $DBType database tool" 
    ${If} $DBType S== "MySQL"
         StrCpy $DBDirText "$DBDirText mysql.exe"
    ${ElseIf} $DBType S== "Microsoft SQL"
         StrCpy $DBDirText "$DBDirText sqlcmd.exe"
    ${EndIf}
FunctionEnd

Function DDBDirectoryShow
    FindWindow $0 "#32770" "" $HWNDPARENT
    GetDlgItem $1 $0 1023 
    ShowWindow $1 0
FunctionEnd

; Check the executable directory selected by the user
Function checkDB
    ${If} $DBType S== "MySQL"
        ${IfNot} ${FileExists} $DBDir\mysql.exe
            !insertmacro writeToLog "The executable mysql.exe could not be found in $DBDir. Please select the correct location"
            MessageBox MB_OK|MB_ICONEXCLAMATION "The executable mysql.exe could not be found in $DBDir. Please select the correct location"
            Abort
        ${Else}
            StrCpy $DBExec "$DBDir\mysql.exe"
            nsExec::ExecToStack /OEM '"$DBExec" -h $DBServer -u $DBAdmin --password=$DBAdminPass -e "quit"'
            Pop $0
            Pop $1
            ${IfNot} $0 S== "0"
                !insertmacro writeToLog "Error in connecting to the database. Ensure that the connection are correct - \
                $\n$1" 
                MessageBox MB_OK|MB_ICONEXCLAMATION "Error in connecting to the database. Ensure that the connection details are correct - \
                $\n$1" 
                Abort
            ${EndIf}
        ${EndIf}
    ${ElseIf} $DBType S== "Microsoft SQL"
        ${IfNot} ${FileExists} $DBDir\sqlcmd.exe
            !insertmacro writeToLog "The executable sqlcmd.exe could not be found in $DBDir. Please select the correct location"
            MessageBox MB_OK|MB_ICONEXCLAMATION "The executable sqlcmd.exe could not be found in $DBDir. Please select the correct location"
            Abort
        ${Else}
            ${If} $DBInstance S!= ""
                StrCpy $DBServer "$DBServer\$DBInstance"
            ${EndIf}
            StrCpy $DBExec "$DBDir\sqlcmd.exe"
            nsExec::ExecToStack /OEM '"$DBExec" -S $DBServer -U $DBAdmin -P $DBAdminPass -q "quit"'
            Pop $0
            Pop $1
            ${IfNot} $0 S== "0"
                !insertmacro writeToLog "Error in connecting to the database. Ensure that the connection details are correct - \
                $\n$1" 
                MessageBox MB_OK|MB_ICONEXCLAMATION "Error in connecting to the database. Ensure that the connection details are correct - \
                $\n$1" 
                Abort
            ${EndIf}
        ${EndIf}
    ${EndIf}
FunctionEnd

; Function to get the details of the database to be created
Function getDBDetails
    Var /GLOBAL SmallFont
      
    ${IfNot} $SSAlreadyInstalled S== "NI"
    ${AndIfNot} $SSAlreadyInstalled S== "SAME"    
        Abort
    ${EndIf}     

    nsDialogs::Create 1018
    Pop $Dialog

    ${If} $Dialog == error
        Abort
    ${EndIf}
    ; get the username, password, database name
    ${NSD_CreateGroupBox} 0%  0% 100% 100% "Scheduling Server database details"
    Pop $GenericControl
    ${NSD_CreateLabel} 5% 10% 85% 10% "The database will be created along with the users to access the database"
    Pop $GenericControl
    ${NSD_CreateLabel} 5%  25% 20% 10% "Database Name"
    Pop $GenericControl
    ${NSD_CreateText} 25% 25% 35% 10% "$DBName"
    Pop $DBNameTemp
    ${NSD_CreateLabel} 5%  40% 15% 10% "Username"
    Pop $GenericControl
    ${NSD_CreateText} 25% 40% 35% 10% "$DBUser"
    Pop $DBUserTemp
    ${NSD_CreateLabel} 5%  55% 15% 10% "Host"
    Pop $GenericControl
    ${NSD_CreateComboBox} 25%  55% 35% 10% ""
    Pop $ComboBox
    ${NSD_CB_AddString} $ComboBox "Any Host"
    ${NSD_CB_AddString} $ComboBox "localhost"
    ${NSD_CB_SelectString} $ComboBox "Any Host"
    
    CreateFont $SmallFont "$(^Font)" "7" "520" 
    ${NSD_CreateLabel} 5% 66% 85% 15% "For any host other than $\'localhost$\' or $\'Any host$\', please type the host name/address in the box above"
    Pop $GenericControl
    SendMessage $GenericControl ${WM_SETFONT} $SmallFont 1
    
    ${NSD_CreateLabel} 5% 85% 15% 10% "Password"
    Pop $GenericControl
    ${NSD_CreatePassword} 25% 85% 35% 10% "$DBUserPass"
    Pop $DBPassTemp
    
    nsDialogs::Show
FunctionEnd

Function getDetails
  
    ${NSD_GetText} $DBUserTemp $DBUser
    ${NSD_GetText} $ComboBox $Host
    ${NSD_GetText} $DBPassTemp $DBUserPass
    ${NSD_GetText} $DBNameTemp $DBName
    ; check database name
    ${If} $DBName S== ""
        MessageBox MB_ICONEXCLAMATION|MB_OK "Please enter database name"
        Abort
    ${EndIf}
    ; check user
    ${If} $DBUser S== ""
        MessageBox MB_ICONEXCLAMATION|MB_OK "Please enter username"
        Abort
    ${EndIf}
    ; check password
    ${If} $DBUserPass S== ""
        MessageBox MB_ICONEXCLAMATION|MB_YESNO "User will be created without any password. Do you want to continue?" IDYES ContinueDB
        Abort
    ${EndIf}
    ContinueDB:

    ${If} $Host S== "Any Host"
        StrCpy $Host "%"
    ${EndIf}
    MessageBox MB_ICONINFORMATION|MB_OK "NOTE: Please update the Scheduling Server configuration file with these user and database details"
FunctionEnd

; Function to create the database
Function createDatabase
    ${If} $DBType S== "MySQL"
        ; create database
        nsExec::ExecToStack /OEM '"$DBExec" -h $DBServer -u $DBAdmin --password=$DBAdminPass -e "CREATE DATABASE $DBName"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0"
        ${OrIf} $1 S!= "" ; additional check for cases when the status is 0 but there is an error 
            !insertmacro writeToLog "Error in creating database $DBName. Skipping further setup actions - $\n$1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in creating database $DBName. Skipping further setup actions - $\n$\n$1"
            StrCpy $DBStatus "-1"
        ${Else}
            StrCpy $DBStatus ""
        ${EndIf}
    ${ElseIf} $DBType S== "Microsoft SQL"
        ; create database
        nsExec::ExecToStack /OEM '"$DBExec" -S $DBServer -U $DBAdmin -P $DBAdminPass -Q "CREATE DATABASE $DBName"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0" 
            ${OrIf} $1 S!= ""
            !insertmacro writeToLog "Error in creating database $DBName. Skipping further setup actions - $\n$1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in creating database $DBName. Skipping further setup actions - $\n$\n$1"
            StrCpy $DBStatus "-1"
        ${Else}
            StrCpy $DBStatus ""
        ${EndIf}
    ${EndIf}
FunctionEnd

; Function to create the database user
Function createUser
    ${If} $DBType S== "MySQL"
    ; create user
        nsExec::ExecToStack /OEM '"$DBExec" -h $DBServer -u $DBAdmin --password=$DBAdminPass -e "CREATE USER $\'$DBUser$\'@$\'$Host$\' IDENTIFIED BY $\'$DBUserPass$\'"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0"
        ${OrIf} $1 S!= "" ; additional check for cases when the status is 0 but there is an error 
            !insertmacro writeToLog "Error in creating user $DBUser (this could be because the user already exists).\
            $\nSkipping user creation - $\n$1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in creating user $DBUser (this could be because the user already exists).\
            $\nSkipping user creation - $\n$\n$1"
            StrCpy $UserStatus "-1"
        ${Else}
            StrCpy $UserStatus ""
        ${EndIf}
   ${ElseIf} $DBType S== "Microsoft SQL"
        ; create login
        nsExec::ExecToStack /OEM '"$DBExec" -S $DBServer -U $DBAdmin -P $DBAdminPass -d $DBName -Q "CREATE LOGIN $DBUser WITH PASSWORD=$\'$DBUserPass$\';\
        CREATE USER $DBUser from LOGIN $DBUser"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0"
            ${OrIf} $1 S!= ""
            !insertmacro writeToLog "Error in creating database user/login $DBUser (this could be because the user already exists).\
            $\nSkipping user creation - $\n$1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in creating database user/login $DBUser (this could be because the user already exists).\
            $\nSkipping user creation - $\n$\n$1"
            StrCpy $UserStatus "-1"
        ${Else}
            StrCpy $UserStatus ""            
        ${EndIf}
   ${EndIf}
FunctionEnd

; Function to grant the privileges for the newly created database to the user
Function grantPrivileges
    ${If} $DBType S== "MySQL"
        ; grant all previleges on the created database
        ; set the NO_AUTO_CREATE_USER so that the grant statement does not automatically create the user
        nsExec::ExecToStack /OEM '"$DBExec" -h $DBServer -u $DBAdmin --password=$DBAdminPass -e \
        "set session sql_mode=$\'NO_AUTO_CREATE_USER$\'; GRANT ALL ON $DBName.* to $\'$DBUser$\'@$\'$Host$\'"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0" 
        ${OrIf} $1 S!= "" ; additional check for cases when the status is 0 but there is an error 
            !insertmacro writeToLog "Error in granting privileges to user $DBUser on database $DBName - $\n$1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in granting privileges to user $DBUser on database $DBName - $\n$\n$1"
            StrCpy $PrivilegesStatus "-1"
        ${Else}
            StrCpy $PrivilegesStatus ""            
        ${Endif}
    ${ElseIf} $DBType S== "Microsoft SQL"
        ; assign database roles to the user
        nsExec::ExecToStack /OEM '"$DBExec" -S $DBServer -U $DBAdmin -P $DBAdminPass -d $DBName -Q "EXEC sp_addrolemember $\'db_ddladmin$\', $\'$DBUser$\';\
        EXEC sp_addrolemember $\'db_datareader$\', $\'$DBUser$\'; EXEC sp_addrolemember $\'db_datawriter$\', $\'$DBUser$\'"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0" 
        ${OrIf} $1 S!= ""
            !insertmacro writeToLog "Error in adding $DBUser to the database roles - $\n$1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in adding $DBUser to the database roles - $\n$\n$1"
            StrCpy $PrivilegesStatus "-1"
        ${Else}
            StrCpy $PrivilegesStatus ""               
        ${EndIf}        
    ${EndIf}
FunctionEnd

; Function to create Scheduling Server schema in the database
Function createSchema
    ${If} $DBType S== "MySQL"
        ; create the schema
        nsExec::ExecToStack /OEM '"$DBExec" -h $DBServer -u $DBUser --password=$DBUserPass -D $DBName -e "source $INSTDIR\schemas\mysql-schema.sql"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0"
        ${OrIf} $1 S!= "" ; additional check for cases when the status is 0 but there is an error 
            !insertmacro writeToLog "Error in creating schema in database $DBName. Please execute the script $INSTDIR\schemas\mysql-schema.sql manually.\
            $\nThe error message is - $1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in creating schema in database $DBName. Please execute the script $INSTDIR\schemas\mysql-schema.sql manually.\
             $\n$\nThe error message is - $1"
            StrCpy $SchemaStatus "-1"
        ${Else}
            StrCpy $SchemaStatus ""               
        ${EndIf}
    ${ElseIf} $DBType S== "Microsoft SQL"
        ; create the schema
        nsExec::ExecToStack /OEM '"$DBExec" -S $DBServer -U $DBUser -P $DBUserPass -d $DBName -i $INSTDIR\schemas\mssql-schema.sql'
        Pop $0
        Pop $1
        ${IfNot} $0 S== "0" 
        ${OrIf} $1 S!= ""
            !insertmacro writeToLog "Error in creating schema in database $DBName. Please execute the script $INSTDIR\schemas\mssql-schema.sql manually.\
            $\nThe error message is - $1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in creating schema in database $DBName. Please execute the script $INSTDIR\schemas\mssql-schema.sql manually.\
            $\n$\nThe error message is - $1"
            StrCpy $SchemaStatus "-1"
        ${Else}
            StrCpy $SchemaStatus ""               
        ${EndIf}   
    ${EndIf}    
FunctionEnd

; Function to add sample data in the Scheduling Server database
Function addDataUsingFile
   ${If} $DBType S== "MySQL"
        nsExec::ExecToStack /OEM '"$DBExec" -h $DBServer -u $DBUser --password=$DBUserPass -D $DBName -e "source $PLUGINSDIR\sampleDataMysql.sql"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0"
        ${OrIf} $1 S!= "" ; additional check for cases when the status is 0 but there is an error 
            !insertmacro writeToLog "Error in adding sample data to database $DBName - $\n$1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in adding sample data to database $DBName - $\n$\n$1"
            StrCpy $SampleDataStatus "-1"
        ${Else}
            StrCpy $SampleDataStatus ""               
        ${EndIf}
   ${ElseIf} $DBType S== "Microsoft SQL"
        nsExec::ExecToStack /OEM '"$DBExec" -S $DBServer -U $DBUser -P $DBUserPass -d $DBName -i $PLUGINSDIR\sampleDataMssql.sql'
        Pop $0
        Pop $1
        ${IfNot} $0 S== "0" 
        ${OrIf} $1 S!= ""
            !insertmacro writeToLog "Error in adding sample data to database $DBName - $\n$1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in adding sample data to database $DBName - $\n$\n$1"
            StrCpy $SampleDataStatus "-1"
        ${Else}
            StrCpy $SampleDataStatus ""               
        ${EndIf}
   ${EndIf}
FunctionEnd

Function createDB
    ${IfNot} $SSAlreadyInstalled S== "NI"
    ${AndIfNot} $SSAlreadyInstalled S== "SAME"    
        Abort
    ${EndIf}
    ; $DBStatus - createDatabase status
    ; $UserStatus - createUser status
    ; $PrivilegesStatus - grantPrivileges status
    ; $SchemaStatus - createSchema status
    ; $SchemaStatus - addSampleData setup
    StrCpy $DBStatus "-1"
    StrCpy $UserStatus "-1"
    StrCpy $PrivilegesStatus "-1"
    StrCpy $SchemaStatus "-1"
    StrCpy $SampleDataStatus "-1"
    call createDatabase
    ${If} $DBStatus S== ""
        call createUser
        call grantPrivileges
        ${If} $PrivilegesStatus S== ""
            call createSchema
        ${EndIf}
        ${If} $SchemaStatus S== ""
            call addDataUsingFile
        ${EndIf}
    ${EndIf}
FunctionEnd

Function revertBackPrivileges
    ; MessageBox MB_OK "Reverting back privileges"
    Var /GLOBAL revertStatus
    ${If} $DBType S== "MySQL"
        nsExec::ExecToStack /OEM '"$DBExec" -h $DBServer -u $DBAdmin --password=$DBAdminPass -e "revoke all on $DBName.* from $\'$DBUser$\'@$\'$Host$\'"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0"
        ${OrIf} $1 S!= "" ; additional check for cases when the status is 0 but there is an error 
            !insertmacro writeToLog "Error in revoking the privileges from $DBUser on $DBName - $\n$1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in revoking the privileges from $DBUser on $DBName - $\n$\n$1"
            StrCpy $revertStatus "-1"
        ${EndIf}
    ${ElseIf} $DBType S== "Microsoft SQL"
        nsExec::ExecToStack /OEM '"$DBExec" -S $DBServer -U $DBAdmin -P $DBAdminPass -d $DBName -Q "EXEC sp_droprolemember $\'db_ddladmin$\', $\'$DBUser$\';\
        EXEC sp_droprolemember $\'db_datareader$\', $\'$DBUser$\'; EXEC sp_droprolemember $\'db_datawriter$\', $\'$DBUser$\'"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0" 
        ${OrIf} $1 S!= ""
            !insertmacro writeToLog "Error in removing $DBUser from the database roles - $\n$1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in removing $DBUser from the database roles - $\n$\n$1"
            StrCpy $revertStatus "-1"
        ${EndIf}    
    ${EndIf}
FunctionEnd

Function revertBackUser
    ; MessageBox MB_OK "Reverting back user creation"
    ${If} $DBType S== "MySQL"
        nsExec::ExecToStack /OEM '"$DBExec" -h $DBServer -u $DBAdmin --password=$DBAdminPass -e "drop user $\'$DBUser$\'@$\'$Host$\'"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0"
        ${OrIf} $1 S!= "" ; additional check for cases when the status is 0 but there is an error 
            !insertmacro writeToLog "Error in deleteing the user $DBUser - $\n$1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in deleteing the user $DBUser - $\n$\n$1"
            StrCpy $revertStatus "-1"
        ${EndIf}
    ${ElseIf} $DBType S== "Microsoft SQL"
        nsExec::ExecToStack /OEM '"$DBExec" -S $DBServer -U $DBAdmin -P $DBAdminPass -d $DBName -Q "DROP USER $DBUser; DROP LOGIN $DBUser;"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0"
            ${OrIf} $1 S!= ""
            !insertmacro writeToLog "Error in deleting database user/login $DBUser - $\n$1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in deleting database user/login $DBUser - $\n$\n$1"
            StrCpy $revertStatus "-1"
        ${EndIf}
    ${EndIf}
FunctionEnd

Function revertBackDB
    ; MessageBox MB_OK "Reverting back database"
    ${If} $DBType S== "MySQL"
        nsExec::ExecToStack /OEM '"$DBExec" -h $DBServer -u $DBAdmin --password=$DBAdminPass -e "DROP DATABASE $DBName"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0"
        ${OrIf} $1 S!= "" ; additional check for cases when the status is 0 but there is an error 
            !insertmacro writeToLog "Error in dropping the database $DBName - $\n$1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in dropping database $DBName - $\n$\n$1"
            StrCpy $revertStatus "-1"
        ${EndIf}
    ${ElseIf} $DBType S== "Microsoft SQL"
        nsExec::ExecToStack /OEM '"$DBExec" -S $DBServer -U $DBAdmin -P $DBAdminPass -Q "DROP DATABASE $DBName"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0" 
            ${OrIf} $1 S!= ""
            !insertmacro writeToLog "Error in dropping database $DBName - $\n$1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in dropping database $DBName - $1"
            StrCpy $revertStatus "-1"
        ${EndIf}
    ${EndIf}
FunctionEnd

Function printSummary
    ${IfNot} $SSAlreadyInstalled S== "NI"
    ${AndIfNot} $SSAlreadyInstalled S== "SAME"    
        Abort
    ${EndIf}
    Var /GLOBAL Image
    Var /GLOBAL ImageHandle
    Var /GLOBAL ErrorFlag
    
    StrCpy $ErrorFlag "0"
    GetDlgItem $0 $HWNDPARENT 3
    EnableWindow $0 0 
   
    nsDialogs::Create 1018
    Pop $Dialog

    ${If} $Dialog == error
        Abort
    ${EndIf}
    
    ${NSD_CreateGroupBox} 0%  0% 100% 100% "Database setup summary"
    Pop $GenericControl
    ${NSD_CreateLabel} 10% 10% 80% 10% "Database creation (database $\'$DBName$\')"
    Pop $GenericControl
    ${NSD_CreateBitmap} 5% 10% 5% 10% ""
    Pop $Image
    ${If} $DBStatus S== "" ; Database creation success
        ${NSD_SetImage} $Image $PLUGINSDIR\tick.bmp $ImageHandle
    ${Else}
        StrCpy $ErrorFlag "1"
        ${NSD_SetImage} $Image $PLUGINSDIR\cross.bmp $ImageHandle
    ${EndIf}
    
    ${NSD_CreateLabel} 10% 23% 80% 10% "User creation (User $\'$DBUser$\')"
    Pop $GenericControl
    ${NSD_CreateBitmap} 5% 23% 5% 10% ""
    Pop $Image
    ${If} $UserStatus S== "" ; User creation success
        ${NSD_SetImage} $Image $PLUGINSDIR\tick.bmp $ImageHandle
    ${Else}
        StrCpy $ErrorFlag "1"
        ${NSD_SetImage} $Image $PLUGINSDIR\cross.bmp $ImageHandle
    ${EndIf}
    
    ${NSD_CreateLabel} 10% 36% 80% 10% "Granting privileges to user $\'$DBUser$\'"
    Pop $GenericControl
    ${NSD_CreateBitmap} 5% 36% 5% 10% ""
    Pop $Image
    ${If} $PrivilegesStatus S== "" ; Ganting privileges to user success
        ${NSD_SetImage} $Image $PLUGINSDIR\tick.bmp $ImageHandle
    ${Else}
        StrCpy $ErrorFlag "1"
        ${NSD_SetImage} $Image $PLUGINSDIR\cross.bmp $ImageHandle
    ${EndIf}
    
    ${NSD_CreateLabel} 10% 49% 80% 10% "Creating Scheduling Server database schema in database $\'$DBName$\'"
    Pop $GenericControl
    ${NSD_CreateBitmap} 5% 49% 5% 10% ""
    Pop $Image
    ${If} $SchemaStatus S== "" ; Schema creation success
        ${NSD_SetImage} $Image $PLUGINSDIR\tick.bmp $ImageHandle
    ${Else}
        StrCpy $ErrorFlag "1"
        ${NSD_SetImage} $Image $PLUGINSDIR\cross.bmp $ImageHandle
    ${EndIf}
        
    ${NSD_CreateLabel} 10% 62% 80% 10% "Adding sample data to database $\'$DBName$\'"
    Pop $GenericControl
    ${NSD_CreateBitmap} 5% 62% 5% 10% ""
    Pop $Image
    ${If} $SampleDataStatus S== "" ; Schema creation success
        ${NSD_SetImage} $Image $PLUGINSDIR\tick.bmp $ImageHandle
    ${Else}
        StrCpy $ErrorFlag "1"
        ${NSD_SetImage} $Image $PLUGINSDIR\cross.bmp $ImageHandle
    ${EndIf}

    ${If} $ErrorFlag S== "1"
        ${NSD_CreateLabel} 5% 72% 80% 20% "Please check the log file $INSTDIR\DatabaseSetup.log for errors"
    ${EndIf}
        
    nsDialogs::Show
    
    ${NSD_FreeImage} $ImageHandle
  
FunctionEnd       

Function revertBack
    ${If} $ErrorFlag S== "1" 
    ${AndIf} $DBStatus S== "" ; No steps to revert back if database creation has failed
        ${If} $UserStatus S!= ""
        ${AndIf} $PrivilegesStatus S== ""
            ; User creation failed but granting privileges succeeded
            ; Check with the user whether to revert back the steps.
            MessageBox MB_YESNO|MB_ICONEXCLAMATION "User creation step failed for the user $DBUser (this could be because the user already exists).\
            $\nThe installer can now revert back the database creation step (drop the database $DBName)$\n- OR -\
            $\nyou can ignore the user creation failure and continue. \
            $\n$\nDo you want to revert back the changes?" IDNO FinishSetup
            call revertBackDB
            call revertBackPrivileges
            ${If} $revertStatus S== "-1"
                MessageBox MB_OK|MB_ICONEXCLAMATION "Error in reverting back the steps.\
                $\nPlease check the log file $INSTDIR\DatabaseSetup.log for errors"
            ${Else}
                MessageBox MB_OK|MB_ICONINFORMATION "Successfully dropped the database $DBName"
            ${EndIf}
            Goto FinishSetup
        ${EndIf}
        MessageBox MB_YESNO|MB_ICONEXCLAMATION "Some of the database setup steps could not be completed successfully. \
        The log $\nfile contains more details about the problems.$\n$\nThe installer can now revert back the successfully executed \
        database setup steps $\nand you can re-run the installer after resolving the problems$\n- OR -$\nYou can execute \
        the failed steps manually without reverting back the executed $\nsteps.$\n$\nDo you want to revert back the changes?" IDNO FinishSetup
        ; revert back the changes
        ; DB creation failed - no action as no further steps would be executed
        ; User creation failed - no action as there could be valid reasons for failure
        ;                        (like user already existing)
        ; Granting privileges failed - revert back DB creation and optionally user creation
        StrCpy $revertStatus "0"
        ${If} $PrivilegesStatus S!= ""
            ${If} $UserStatus S== "" ; User creation succeeded
                call revertBackUser
            ${EndIf}
            call revertBackDB
            Goto DisplayStatus
        ${EndIf}
        ; Schema creation failed - revert back DB creation and 
        ; optionally user creation and granting privileges
        ${If} $SchemaStatus S!= "" 
        ${OrIf} $SampleDataStatus S!= ""
            call revertBackPrivileges
            ${If} $UserStatus S== "" ; User creation succeeded
                call revertBackUser
            ${EndIf}
            call revertBackDB
        ${EndIf}
        DisplayStatus:
        ${If} $revertStatus S== "-1"
            MessageBox MB_OK|MB_ICONEXCLAMATION "Error in reverting back the steps.\
            $\nPlease check the log file $INSTDIR\DatabaseSetup.log for errors"
        ${Else}
            MessageBox MB_OK|MB_ICONINFORMATION "Successfully reverted back the setps"
        ${EndIf}
    ${EndIf}    
FinishSetup:
FunctionEnd 

; -----------------------------------------------------------------------------
; --- Uninstall Scheduling Server                                           ---
; -----------------------------------------------------------------------------
Section "un.Scheduling Server" un.SchedulingServer
	!insertmacro checkAdminUser "uninstallation"
	!insertmacro uninstallWindowsService "uninstallation"
	Push $R1
    ReadRegStr $R1 HKLM "${REGKEY}" "Path"
	;Delete the component files/directories
	RMDir /r $R1\bundles 
    RMDir /r $R1\bin 
    RMDir /r $R1\conf
    RMDir /r $R1\cache
    RMDir /r $R1\schemas
    Delete $R1\LICENSE
    Delete $R1\schedulingservice*
    Delete $R1\*.log
    
    Pop $R1
SectionEnd 

; -----------------------------------------------------------------------------
; --- Uninstaller functions                                                 ---
; -----------------------------------------------------------------------------
Function un.onInit

	; Get the Rig Client path
	ReadRegStr $INSTDIR HKLM "${REGKEY}" "Path"

	; Check if the components are installed and enable only the installed components for uninstallation
	Push $R0
    ClearErrors
	ReadRegStr $R0 HKLM "${REGKEY}" "Path"
	${If} $R0 S== ""
	      	!insertmacro disableSection ${un.SchedulingServer}
	${EndIf}
    
    
	StrCpy $DisplayText ""
	StrCpy $NoSectionSelectedUninstall "true"

	Pop $R0

FunctionEnd

Function un.CheckSlectedComponents

	StrCpy $DisplayText ""

	${If} ${SectionIsSelected} ${un.SchedulingServer}
		StrCpy $DisplayText "$DisplayText$\nScheduling Server"
		StrCpy $NoSectionSelectedUninstall "false"
	${EndIf}

	${If} $NoSectionSelectedUninstall S== "false"
		StrCpy $DisplayText "The following is selected for uninstallation. Do you want to continue?$DisplayText" 
		MessageBox MB_YESNO "$DisplayText" IDYES selectionEnd
		StrCpy $NoSectionSelectedUninstall "true"
		Abort
	${Else}
		MessageBox MB_OK "No component selected"
		StrCpy $NoSectionSelectedUninstall "true"
		Abort
	${EndIf}
selectionEnd:
FunctionEnd

Section -un.postactions 
	
    ClearErrors
    DeleteRegKey /IfEmpty HKLM "${REGKEY}"
	; Check if all the components are deleted. If they are, delete the main installation directory and uninstaller
    ClearErrors
	ReadRegStr $R0 HKLM "${REGKEY}" "Path"
	${If} $R0 S== ""
		DeleteRegKey /IfEmpty HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SchedulingServer"
		Delete $INSTDIR\uninstallSchedulingServer.exe
		ClearErrors
        RMDir $INSTDIR
		ifErrors 0 InstDirDeleted
		MessageBox MB_ABORTRETRYIGNORE "Error in deleting the directory $INSTDIR." IDRETRY RetryDelete IDIGNORE InstDirDeleted
		Abort
		RetryDelete:
		RMDir $INSTDIR
		ifErrors 0 InstDirDeleted
		MessageBox MB_OK "Error in deleting the directory $INSTDIR. Directory $INSTDIR will not be deleted."
		InstDirDeleted:
	${EndIf}
SectionEnd

; -----------------------------------------------------------------------------
; --- Descriptions                                                          ---
; -----------------------------------------------------------------------------

; Language strings
LangString DESC_SecSS ${LANG_ENGLISH} "SAHARA Labs Scheduling Server"

; Assign language strings to sections
!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
!insertmacro MUI_DESCRIPTION_TEXT ${SchedServer} $(DESC_SecSS)
!insertmacro MUI_FUNCTION_DESCRIPTION_END

!insertmacro MUI_UNFUNCTION_DESCRIPTION_BEGIN
!insertmacro MUI_DESCRIPTION_TEXT ${un.SchedulingServer} $(DESC_SecSS)
!insertmacro MUI_UNFUNCTION_DESCRIPTION_END
