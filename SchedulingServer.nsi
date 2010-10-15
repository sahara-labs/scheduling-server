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
 * @date 12th March 2010
 */
 
 ; SchedulingServer.nsi
;
;--------------------------------
!include MUI2.nsh
!include TextFunc.nsh
!include nsDialogs.nsh
!include LogicLib.nsh
!include WordFunc.nsh
!include Sections.nsh

!define SF_UNSELECTED  0


; The name of the installer
Name "Sahara Scheduling Server"

!define REGKEY "SOFTWARE\$(^Name)"

; Sahara Scheduling Server Version
!define Version "1.0"

!define JREVersion "1.6"

; The file to write
OutFile "package/SaharaSchedulingServer.exe"

; The default installation directory
InstallDir "C:\Program Files\Sahara"

BrandingText "$(^Name)"
WindowIcon off
XPStyle on
Var skipSection
;--------------------------------
;Interface Settings
 
!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_BITMAP "InstallerFiles\labshare.bmp"

!define MUI_ICON "InstallerFiles\labshare.ico"
!define MUI_ABORTWARNING
  
!define MUI_UNICON "InstallerFiles\win-install.ico"

!define Sahara_SSWindows_Service "Scheduling Server"

;--------------------------------

; Pages
;
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "License"
Page custom getInstallChoice selectionDone
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
;-------------------------------------------------------------
; Custom pages for database setup and the required variables 
Var Dialog
Var InfoLabel
Var SSInstallClick
Var DBSetupClick
Var Checkbox_State
Var DBPageNumber
Var DBDir
Var DBExec
Var DBUser
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
Page custom printSummary
; End Custom pages
;-------------------------------------------------------------

Var finishTitle
Var finishText
!define MUI_FINISHPAGE_TITLE "$finishTitle"
!define MUI_FINISHPAGE_TEXT "$finishText"
!insertmacro MUI_PAGE_FINISH

!insertmacro MUI_UNPAGE_CONFIRM
!define MUI_PAGE_CUSTOMFUNCTION_LEAVE un.CheckSlectedComponents
!insertmacro MUI_UNPAGE_COMPONENTS
!insertmacro MUI_UNPAGE_INSTFILES

;--------------------------------
;Languages
 
!insertmacro MUI_LANGUAGE "English"

;--------------------------------

Var DisplayText
Var NoSectionSelectedUninstall 
Var SSAlreadyInstalled ;0=Not installed, 1=Installed but different version, 2=installed and same version 

Function DirectoryPagePre
	; If there is already an installation of Sahara, use the same folder for this installation. Else let the user select the installation folder
 	${If} $SSAlreadyInstalled S== "0"
		StrCpy $DirHeaderText "Choose Install Location"
		StrCpy $DirHeaderSubText "Choose the folder in which to install $(^Name)"
	${ElseIf} $SSAlreadyInstalled S== "2" 
		StrCpy $DirHeaderText "Using existing $(^Name) installation folder"
		StrCpy $DirHeaderSubText "One or more components of $(^Name) are already installed on this machine. Installer will use same destination folder"
		
	${EndIf}

FunctionEnd

Function CheckSaharaVersion
	ReadRegStr $R0 HKLM "${REGKEY}" "CurrentVersion"
	${If} $R0 S== ""
		StrCpy $SSAlreadyInstalled "0"
	${ElseIf} $R0 S!=  ${Version} 
		StrCpy $SSAlreadyInstalled "1"
	${Else}
		StrCpy $SSAlreadyInstalled "2"
        ReadRegStr $R0 HKLM "${REGKEY}" "Path"
        StrCpy $INSTDIR $R0
	${EndIf}
FunctionEnd


Function DirectoryPageShow

	; If there is already an installation of Sahara, disable the destination folder selection and use the same folder for this installation. 
	; Else let the user select the installation folder
    
    ${If} $SSAlreadyInstalled S== "2"
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
	${If} $SSAlreadyInstalled S== "0"
		StrCpy $INSTDIR "$INSTDIR\SchedulingServer"
	${EndIf}
FunctionEnd

Function .onInit
    InitPluginsDir
    File /oname=$PLUGINSDIR\tick.bmp "InstallerFiles\tick.bmp"
    File /oname=$PLUGINSDIR\cross.bmp "InstallerFiles\cross.bmp"
    ; Sample data file for installer
    File /oname=$PLUGINSDIR\sampleDataMysql.sql "InstallerFiles\sampleDataMysql.sql"
    File /oname=$PLUGINSDIR\sampleDataMssql.sql "InstallerFiles\sampleDataMssql.sql"

	; Splash screen 
	advsplash::show 1000 1000 1000 -1 ..\InstallerFiles\labshare
 	StrCpy $skipSection "false"
 	StrCpy $SSAlreadyInstalled "-1"
	call CheckSaharaVersion
	${If} $SSAlreadyInstalled S== "1"
		MessageBox MB_OK|MB_ICONSTOP "A different version of Sahara is already installed on this machine. $\nPlease uninstall the existing Sahara software before continuing the installation"
		Abort 
	${EndIf}
    StrCpy $DBPageNumber "7" ; Number of pages to jump forward from 'getInstallChoice' page
FunctionEnd

Function checkJREVersion
	; Check the JRE version to be 1.6 or higher
	ReadRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" CurrentVersion 
	${If} $0 S< ${JREVersion} 
		MessageBox MB_OK|MB_ICONSTOP "$(^Name) needs JRE version ${JREVersion} or higher. It is currently $0. Aborting the installation."
		Abort ; causes installer to quit.
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
Function un.checkIfServiceRunning
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
	MessageBox MB_OK|MB_ICONSTOP "Please stop the '${Sahara_SSWindows_Service}' service before continuing the uninstallation."
	Abort
	Errors:
	StrCmp $R0 '1' NotRunning 0
	MessageBox MB_OK|MB_ICONSTOP "Error is detecting if '${Sahara_SSWindows_Service}' service is installed"
	Abort
	NotRunning:
FunctionEnd

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

;--------------------------------
; Install Scheduling Server
Section "Sahara Scheduling Server" SchedServer
	!insertmacro checkAdminUser "installation"

	call checkJREVersion	
	call checkIfServiceInstalled 

    ; Set output path to the installation directory.
    SetOutPath $INSTDIR
    
    ; Copy the component files/directories
    File LICENSE
    File servicewrapper\WindowsServiceWrapper\Release\schedulingservice.exe
    File doc\db\schema\*.sql
    
    SetOutPath $INSTDIR\bundles
	File /r /x *.svn bundles\*.*
    SetOutPath $INSTDIR\bin
	File /r /x *.svn bin\*.*
    SetOutPath $INSTDIR\conf 
    File /oname=schedulingserver.properties conf\schedulingserver.properties.win
	File conf\scheduling_service.ini
	
    SetOutPath $INSTDIR
    ; Add the RigClient service to the windows services
    ExecWait '"$INSTDIR\schedulingservice" install'
    ifErrors 0 WinServiceNoError
    MessageBox MB_OK|MB_ICONSTOP "Error in executing schedulingservice.exe"
    ; TODO  Revert back the installed SS in case of error?
    Abort
    WinServiceNoError:
    WriteRegStr HKLM "${REGKEY}" Path $INSTDIR
    WriteRegStr HKLM "${REGKEY}" CurrentVersion  ${Version}
    
    StrCpy $finishTitle "Completing $(^Name) Setup Wizard"
    StrCpy $finishText "$(^Name) is installed at $INSTDIR" 
SectionEnd

;--------------------------------

; Post install actions
Function .onInstSuccess
	ClearErrors
	EnumRegKey $2 HKLM  "${REGKEY}" 0
	ifErrors installEnd createRegKey 
	createRegKey:
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "DisplayName"  "$(^Name)"
	WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "DisplayVersion" "${VERSION}"
	WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "InstallLocation" "$INSTDIR\SchedulingServer"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "UninstallString" "$\"$INSTDIR\uninstallSchedulingServer.exe$\""
	WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "NoModify" 1
	WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "NoRepair" 1
installEnd:
FunctionEnd

;--------------------------------
; Create uninstaller
Section -createUninstaller
	ClearErrors
	EnumRegKey $1 HKLM  "${REGKEY}" 0
	ifErrors 0 createUninstaller
	MessageBox MB_OK|MB_ICONSTOP  "No component selected for installation. Aborting the installation"
	ABort
	createUninstaller:
	SetOutPath $INSTDIR
	WriteUninstaller $INSTDIR\uninstallSchedulingServer.exe
SectionEnd

; --------------------------------------------------
; Functions added for database setup functionality

; Function to display installation choice after the license page
Function getInstallChoice
    !insertmacro MUI_HEADER_TEXT "Select the installaion option" ""
    nsDialogs::Create 1018
    Pop $Dialog
    ${If} $Dialog == error
        Abort
    ${EndIf}
    ${NSD_CreateRadioButton} 0 0 100% 20% "Install Scheduling Server (optionally including database setup)"
    Pop $SSInstallClick
    ${NSD_CreateRadioButton} 0 20% 100% 20% "Database setup only"
    Pop $DBSetupClick
    ; By default, select the SS installation option
    ${NSD_SetState} $SSInstallClick ${BST_CHECKED} 
    nsDialogs::Show 
FunctionEnd

; This function will be called after getInstallChoice function
Function selectionDone
    ; Check which option is selected
    ${NSD_GetState} $SSInstallClick $Checkbox_State
    ${If} $Checkbox_State == ${BST_UNCHECKED} ; Install option not selected
        ${NSD_GetState} $DBSetupClick $Checkbox_State
        ${If} $Checkbox_State == ${BST_CHECKED}
            ; If 'Database setup only' option is selected -
            ; 1. Check if Scheduling Server is installed.
            ; 2. If yes, skip the install pages and go to the DB setup page
            ; TODO check the behaviour in case of upgrade
            ${If} $SSAlreadyInstalled S!= "2"
                MessageBox MB_OK|MB_ICONEXCLAMATION "This option can be selected only if the same version of Scheduling Server is \
                already installed on the machine.$\n$\nPlease select the 'Install Scheduling Server' option"
                Abort
            ${EndIf}
            StrCpy $finishTitle "Completing $(^Name) Database Setup Wizard"
            StrCpy $finishText ""      
            Call GotoDBPage
        ${Else}
            MessageBox MB_OK "Please select one option"
            Abort
        ${EndIf}
    ${EndIf}
FunctionEnd

; Function to go to a specified NSIS page 
Function GotoDBPage
    IntCmp $DBPageNumber 0 0 GoToPage GoToPage
    StrCmp $DBPageNumber "X" 0 GoToPage
    StrCpy $DBPageNumber "120"
  GoToPage:
  SendMessage $HWNDPARENT "0x408" "$DBPageNumber" ""
FunctionEnd

; Function to confirm whether to proceed with database setup stage
Function confirmDB
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
            MessageBox MB_ICONEXCLAMATION|MB_OKCANCEL "Installer will be terminated without setting up the database" IDCANCEL GoBack
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
    ${GetTime} "" "LS" $DateVar $MonthVar $YearVar $DayVar $HourVar $MinuteVar $SecondsVar 
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
    Var /GLOBAL ComboBox
    Var /GLOBAL GenericControl
    Var /GLOBAL DBType
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
    ${NSD_CreateComboBox} 5%  10% 30% 10% ""
    Pop $ComboBox
    ${NSD_SetText} $ComboBox "$DBType" 
    ${NSD_CB_AddString} $ComboBox "MySQL"
    ${NSD_CB_AddString} $ComboBox "Microsoft SQL"
    
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
    ${If} $DBType S== ""
        MessageBox MB_OK "Database not selected"
        Abort
    ${Else}
        ${NSD_GetText} $DBUserTemp $DBAdmin
        ${NSD_GetText} $DBPassTemp $DBAdminPass
        ${NSD_GetText} $DBServerTemp $DBServer
        ${NSD_GetText} $DBInstanceTemp $DBInstance
    ${EndIf} 
    ${If} $DBServer S== ""
        MessageBox MB_OK "Please enter database server"
        Abort
    ${EndIf}
FunctionEnd

Function DDBDirectoryPre
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
            !insertmacro writeToLog "The executable mysql.exe could not be found in $DBDir."
            MessageBox MB_OK "The executable mysql.exe could not be found in $DBDir."
            Abort
        ${Else}
            StrCpy $DBExec "$DBDir\mysql.exe"
            nsExec::ExecToStack /OEM '"$DBExec" -h $DBServer -u $DBAdmin --password=$DBAdminPass -e "quit"'
            Pop $0
            Pop $1
            ${IfNot} $0 S== "0"
                !insertmacro writeToLog "Error in connecting to the database. Ensure that the connection are correct - \
                $\n$1" 
                MessageBox MB_OK "Error in connecting to the database. Ensure that the connection details are correct - \
                $\n$1" 
                Abort
            ${EndIf}
        ${EndIf}
    ${ElseIf} $DBType S== "Microsoft SQL"
        ${IfNot} ${FileExists} $DBDir\sqlcmd.exe
            !insertmacro writeToLog "The executable sqlcmd.exe could not be found in $DBDir. Please select the correct location"
            MessageBox MB_OK "The executable sqlcmd.exe could not be found in $DBDir. Please select the correct location"
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
                MessageBox MB_OK "Error in connecting to the database. Ensure that the connection are correct - \
                $\n$1" 
                Abort
            ${EndIf}
        ${EndIf}
    ${EndIf}
FunctionEnd

; Function to get the details of the database to be created
Function getDBDetails
    nsDialogs::Create 1018
    Pop $Dialog

    ${If} $Dialog == error
        Abort
    ${EndIf}
    ; get the username, password, database name
    ${NSD_CreateGroupBox} 0%  0% 100% 100% "Scheduling Server database details"
    Pop $GenericControl
    ${NSD_CreateLabel} 5% 10% 85% 25% "The database will be created along with the user to access the database. \
    $\n$\n*** NOTE: Please add the same user and database details in the Scheduling Server configuration file ***"
    Pop $GenericControl
    ${NSD_CreateLabel} 5%  40% 20% 10% "Database Name"
    Pop $GenericControl
    ${NSD_CreateText} 25% 40% 35% 10% "$DBName"
    Pop $DBNameTemp
    ${NSD_CreateLabel} 5%  60% 15% 10% "Username"
    Pop $GenericControl
    ${NSD_CreateText} 25% 60% 35% 10% "$DBUser"
    Pop $DBUserTemp
    ${NSD_CreateLabel} 5% 80% 15% 10% " Password"
    Pop $GenericControl
    ${NSD_CreatePassword} 25% 80% 35% 10% "$DBUserPass"
    Pop $DBPassTemp
    
    nsDialogs::Show
FunctionEnd

Function getDetails
  
    ${NSD_GetText} $DBUserTemp $DBUser
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
FunctionEnd

; Function to create the database
Function createDatabase
    ${If} $DBType S== "MySQL"
        ; create database
        nsExec::ExecToStack /OEM '"$DBExec" -u $DBAdmin --password=$DBAdminPass -e "CREATE DATABASE $DBName"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0"
        ${OrIf} $1 S!= "" ; additional check for cases when the status is 0 but there is an error 
            !insertmacro writeToLog "Error in creating database $DBName. Skipping further setup actions - $\n$1"
            MessageBox MB_OK "Error in creating database $DBName. Skipping further setup actions. - $\n$\n$1"
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
            MessageBox MB_OK "Error in creating database $DBName. Skipping further setup actions. $\n$\nThe error message is  - $1"
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
        nsExec::ExecToStack /OEM '"$DBExec" -h $DBServer -u $DBAdmin --password=$DBAdminPass -e "CREATE USER $\'$DBUser$\'@$\'%$\' IDENTIFIED BY $\'$DBUserPass$\'"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0"
        ${OrIf} $1 S!= "" ; additional check for cases when the status is 0 but there is an error 
            !insertmacro writeToLog "Error in creating user $DBUser. Skipping user creation - $\n$1"
            MessageBox MB_OK "Error in creating user $DBUser. Skipping user creation - $\n$\n$1"
            StrCpy $UserStatus "-1"
        ${Else}
            StrCpy $UserStatus ""
        ${EndIf}
   ${ElseIf} $DBType S== "Microsoft SQL"
        ; create login
        nsExec::ExecToStack /OEM '"$DBExec" -S $DBServer -U $DBAdmin -P $DBAdminPass -Q "CREATE LOGIN $DBUser WITH PASSWORD=$\'$DBUserPass$\'"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0"
            ${OrIf} $1 S!= ""
            !insertmacro writeToLog "Error in creating user $DBUser. Skipping user creation - $\n$1"
            MessageBox MB_OK "Error in creating user $DBUser. Skipping user creation - $\n$\n$1"
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
        "set session sql_mode=$\'NO_AUTO_CREATE_USER$\'; GRANT ALL ON $DBName.* to $\'$DBUser$\'@$\'%$\'"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0" 
        ${OrIf} $1 S!= "" ; additional check for cases when the status is 0 but there is an error 
            !insertmacro writeToLog "Error in granting privileges to user $DBUser on database $DBName - $\n$1"
            MessageBox MB_OK "Error in granting privileges to user $DBUser on database $DBName - $\n$\n$1"
            StrCpy $PrivilegesStatus "-1"
        ${Else}
            StrCpy $PrivilegesStatus ""            
        ${Endif}
    ${ElseIf} $DBType S== "Microsoft SQL"
        ; change the database ownership to the new user
        nsExec::ExecToStack /OEM '"$DBExec" -S $DBServer -U $DBAdmin -P $DBAdminPass -Q "ALTER AUTHORIZATION ON DATABASE::$DBName TO $DBUser"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0" 
        ${OrIf} $1 S!= ""
            !insertmacro writeToLog "Error in changing ownership of database $DBName to $DBUser - $\n$1"
            MessageBox MB_OK "Error in changing ownership of database $DBName to $DBUser - $\n$\n$1"
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
        nsExec::ExecToStack /OEM '"$DBExec" -h $DBServer -u $DBUser --password=$DBUserPass -D $DBName -e "source $INSTDIR\mysql-schema.sql"'
        Pop $0
        Pop $1
        ${If} $0 S!= "0"
        ${OrIf} $1 S!= "" ; additional check for cases when the status is 0 but there is an error 
            !insertmacro writeToLog "Error in creating schema in database $DBName. Please execute the script $INSTDIR\mysql-schema.sql manually.\
            $\nThe error message is - $1"
            MessageBox MB_OK "Error in creating schema in database $DBName. Please execute the script $INSTDIR\mysql-schema.sql manually.\
             $\n$\nThe error message is - $1"
            StrCpy $SchemaStatus "-1"
        ${Else}
            StrCpy $SchemaStatus ""               
        ${EndIf}
    ${ElseIf} $DBType S== "Microsoft SQL"
        ; create the schema
        nsExec::ExecToStack /OEM '"$DBExec" -S $DBServer -U $DBUser -P $DBUserPass -d $DBName -i $INSTDIR\mssql-schema.sql'
        Pop $0
        Pop $1
        ${IfNot} $0 S== "0" 
        ${OrIf} $1 S!= ""
            !insertmacro writeToLog "Error in creating schema in database $DBName. Please execute the script $INSTDIR\mssql-schema.sql manually.\
            $\nThe error message is - $1"
            MessageBox MB_OK "Error in creating schema in database $DBName. Please execute the script $INSTDIR\mssql-schema.sql manually.\
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
            MessageBox MB_OK "Error in adding sample data to database $DBName - $\n$\n$1"
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
            MessageBox MB_OK "Error in adding sample data to database $DBName - $\n$\n$1"
            StrCpy $SampleDataStatus "-1"
        ${Else}
            StrCpy $SampleDataStatus ""               
        ${EndIf}
   ${EndIf}
FunctionEnd

Function createDB

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

Function printSummary
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
; End functions added for database setup functionality
; --------------------------------------------------

;--------------------------------
; Uninstall Scheduling Server
Section "un.Sahara Scheduling Server" un.SchedulingServer
	!insertmacro checkAdminUser "uninstallation"
	call un.checkIfServiceRunning
	Push $R1
	ReadRegStr $R1 HKLM "${REGKEY}" "Path"
	ClearErrors
	ExecWait '"$R1\schedulingservice" uninstall'
	ifErrors 0 WinServiceNoError
	MessageBox MB_ABORTRETRYIGNORE "Error in uninstalling Scheduling Server service.  $\n$\nIf the service is installed, manually uninstall the service from command prompt using: '$R1\schedulingservice uninstall' as admin and press 'Retry'. $\nIf the service is already uninstalled, press 'Ignore'. $\nPress 'Abort' to end the uninstallation" IDABORT AbortUninstall IDIGNORE WinServiceNoError 
	;TryAgain
	ExecWait '$R1\schedulingservice uninstall'
	ifErrors 0 WinServiceNoError
	MessageBox MB_OK|MB_ICONSTOP "Error in uninstalling Scheduling Server service again. Aborting the uninstallation"
	AbortUninstall:
	Abort
	WinServiceNoError:

	;Delete the component files/directories
	RMDir /r $R1\bundles 
    RMDir /r $R1\bin 
    RMDir /r $R1\conf
    RMDir /r $R1\cache
    Delete $R1\LICENSE
    Delete $R1\schedulingservice*
    Delete $R1\*.sql
    Delete $R1\DatabaseSetup.log
    
    Pop $R1
SectionEnd 

;--------------------------------
; Uninstaller functions
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
		StrCpy $DisplayText "Following sections are selected for uninstallation. Do you want to continue?$DisplayText" 
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
		DeleteRegKey /IfEmpty HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)"
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

;--------------------------------


;Descriptions

; Language strings
LangString DESC_SecSS ${LANG_ENGLISH} "Sahara Scheduling Server"

; Assign language strings to sections
!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
!insertmacro MUI_DESCRIPTION_TEXT ${SchedServer} $(DESC_SecSS)
!insertmacro MUI_FUNCTION_DESCRIPTION_END

!insertmacro MUI_UNFUNCTION_DESCRIPTION_BEGIN
!insertmacro MUI_DESCRIPTION_TEXT ${un.SchedulingServer} $(DESC_SecSS)
!insertmacro MUI_UNFUNCTION_DESCRIPTION_END

;--------------------------------

