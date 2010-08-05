/**
 * SAHARA Installer
 * 
 * Sahara installer/uninstallaer script for Windows.  
 
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
 
 ; SaharaInstall.nsi
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

; Sahara Rig Client Version
!define Version "2.0-4"

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
!define MUI_FINISHPAGE_TEXT "$(^Name) is installed at $INSTDIR"
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
 	Push $0
    Push $1
    ${If} $SSAlreadyInstalled S== "NotInstalled"
        ${OrIf} $SSAlreadyInstalled S== "NeedUninstall"
        StrCpy $0 "Choose Install Location"
        StrCpy $1 "Choose the folder in which to install $(^Name)"
    ${ElseIf} $SSAlreadyInstalled S== "SameVersion"
         StrCpy $0 "Using existing $(^Name) installation folder"
         StrCpy $1 "$(^Name) is already installed on this machine. Installer will overwrite the existing installation"
         ReadRegStr $R0 HKLM "${REGKEY}" "Path"
         StrCpy $INSTDIR $R0
    ${ElseIf} $SSAlreadyInstalled S== "Upgrade"
         StrCpy $0 "Using existing $(^Name) installation folder"
         StrCpy $1 "$(^Name) is already installed on this machine. Installer will upgrade the existing installation"
         ReadRegStr $R0 HKLM "${REGKEY}" "Path"
         StrCpy $INSTDIR $R0
    ${ElseIf} $SSAlreadyInstalled S== "Downgrade"
         StrCpy $0 "Using existing $(^Name) installation folder"
         StrCpy $1 "$(^Name) is already installed on this machine. Installer will downgrade the existing installation"
        ReadRegStr $R0 HKLM "${REGKEY}" "Path"
        StrCpy $INSTDIR $R0
    ${EndIf}
    
    StrCpy $DirHeaderText $0
    StrCpy $DirHeaderSubText $1
    
    Pop $0
    Pop $1

FunctionEnd

Function CheckSaharaVersion
    Push $R0
    Push $0
    Push $1
    Push $2
    Push $3
    ReadRegStr $R0 HKLM "${REGKEY}" "CurrentVersion"
    ${If} $R0 S== ""
        StrCpy $SSAlreadyInstalled "NotInstalled"
    ${Else}
        ; Get the existing <Major version>.<Minor Version>
        ; TODO This code assumes the major and minor version are one digit each. 
        StrCpy $0 $R0 3
        StrCpy $1 $R0 "" -1
        ${If} $1 S== ""
            StrCpy $1 "0"
        ${EndIf}
        
        ; $0 - Installed version
        ; $1 - Installed build number
        ; $2 - This version
        ; $3 - This build number
        
        ; Get the <Major version>.<Minor Version> for this product
        StrCpy $2 ${Version} 3
        StrCpy $3 ${Version} "" -1
        ${If} $3 S== ""
            StrCpy $3 "0"
        ${EndIf}
    
        ${If} $0 S== $2 
            ${If} $1 = $3 ; Installling same version and same build
                StrCpy $SSAlreadyInstalled "SameVersion"
            ${ElseIf} $1 > $3  ; Installing same version and older build 
                StrCpy $SSAlreadyInstalled "Downgrade"
            ${Else} ; Installing same version and newer build
                StrCpy $SSAlreadyInstalled "Upgrade"
            ${EndIf}
        ${ElseIf} $0 S== "1.0"
            ; This is an exception. This is the first version delivered
            ; and the user should be able to upgrade to the new version
            ; from version 1.0 without uninstallation
            StrCpy $SSAlreadyInstalled "Upgrade"
        ${ElseIf} $0 S!= $2 ; ; Installing different version   
            StrCpy $SSAlreadyInstalled "NeedUninstall"
        ${EndIf}
    ${EndIf}

Pop $R0
Pop $0
Pop $1
Pop $2
Pop $3
FunctionEnd


Function DirectoryPageShow

	; If there is already an installation of Sahara, disable the destination folder selection and use the same folder for this installation. 
	; Else let the user select the installation folder
    
    ${If} $SSAlreadyInstalled S== "SameVersion"
        ${OrIf} $SSAlreadyInstalled S== "Upgrade"
         ${OrIf} $SSAlreadyInstalled S== "Downgrade"
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
	${If} $SSAlreadyInstalled S== "NotInstalled"
		StrCpy $INSTDIR "$INSTDIR\SchedulingServer"
	${EndIf}
FunctionEnd

Function .onInit
	; Splash screen 
	advsplash::show 1000 1000 1000 -1 labshare
 	StrCpy $skipSection "false"
 	StrCpy $SSAlreadyInstalled "-1"
	call CheckSaharaVersion
	${If} $SSAlreadyInstalled S== "1"
		MessageBox MB_OK|MB_ICONSTOP "A different version of Sahara is already installed on this machine. $\nPlease uninstall the existing Sahara software before continuing the installation"
		Abort 
	${EndIf}

FunctionEnd

Function checkJREVersion
    Push $0
	; Check the JRE version to be 1.6 or higher
	ReadRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" CurrentVersion 
	${If} $0 S< ${JREVersion} 
		MessageBox MB_OK "$(^Name) needs JRE version ${JREVersion} or higher. It is currently $0. Aborting the installation."
		Abort ; causes installer to quit.
	${EndIf}
    Pop $0
FunctionEnd

; Check if SchedulingServer service is running
Function checkIfServiceInstalled
	;ReadEnvStr $R0 COMSPEC

	; Check if the SchedulingServer service is already installed. (probably need a better way to do this)
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
	MessageBox MB_OK "'${Sahara_SSWindows_Service}' service is already installed.  Please stop the '${Sahara_SSWindows_Service}' service if it is running (Windows Control Panel->Administrative Tools->Services) $\n$\nUse schedulingservice.exe to uninstall the previous version (schedulingservice.exe uninstall)"
	Abort
	Error:
	MessageBox MB_OK "Error is detecting if '${Sahara_SSWindows_Service}' service is installed"
	Abort
	Found:
FunctionEnd

; Check if SchedulingServer service is running
!macro checkIfServiceRunning
    ; Check if the SchedulingServer service is running. (probably need a better way to do this)
    ; If the service is running, the output of 'sc query "Scheduling Server"' will be like:
    ;   SERVICE_NAME: Scheduling Server
    ;           TYPE               : 10  WIN32_OWN_PROCESS
    ;           STATE              : 4  RUNNING
        ;                               (STOPPABLE,NOT_PAUSABLE,ACCEPTS_SHUTDOWN)
    ;           WIN32_EXIT_CODE    : 0  (0x0)
    ;           SERVICE_EXIT_CODE  : 0  (0x0)
    ;           CHECKPOINT         : 0x0
    ;           WAIT_HINT          : 0x0
    ;
    ; If the service is installed but not running, the STATE would be 
    ;       SERVICE_NAME: Scheduling Server
        ;       TYPE               : 10  WIN32_OWN_PROCESS
        ;       STATE              : 1  STOPPED
        ;                             (NOT_STOPPABLE,NOT_PAUSABLE,IGNORES_SHUTDOWN)
        ;       WIN32_EXIT_CODE    : 1077       (0x435)
        ;       SERVICE_EXIT_CODE  : 0  (0x0)
        ;       CHECKPOINT         : 0x0
        ;       WAIT_HINT          : 0x0
        ;
        ; If the service is not installed, the STATE would be 
    ;       [SC] EnumQueryServicesStatus:OpenService FAILED 1060:
    ;       The specified service does not exist as an installed service.
    ;
    ; Check for the word 'RUNNING' in the output of the above command. If not found, 
    ;
    ; - If function "WordReplace" is successful (the word "RUNNING" is found in the result), the service is running
    ; - If the function "WordReplace" is not successful and the errorlevel (value of $R0) is 1 (the word "RUNNING" not found), the service is stopped or not installed
    ; - If the function "WordReplace" is not successful and the errorlevel (value of $R0) is anything other than 1 then some error has occured in
    ;   executing function "WordReplace"
    
    nsExec::ExecToStack /OEM '"sc" query "${Sahara_SSWindows_Service}"'
    Pop $0  ; $0 contains return value/error/timeout
    Pop $1  ; $1 contains printed text, up to ${NSIS_MAX_STRLEN}

    ClearErrors
    ${WordReplace} '$1' 'RUNNING' 'RUNNING' 'E+1' $R0
    IfErrors Errors 0
    MessageBox MB_OK|MB_ICONSTOP "Please stop the '${Sahara_SSWindows_Service}' service before continuing."
    Abort
    Errors:
    StrCmp $R0 '1' NotRunning 0
    MessageBox MB_OK|MB_ICONSTOP "Error is detecting if '${Sahara_SSWindows_Service}' service is running"
    Abort
    NotRunning:
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
	MessageBox MB_OK "$(^Name) ${operation} requires administrative privileges"
	Abort
	AdminUser:
!macroend

;--------------------------------
; Install Scheduling Server
Section "Sahara Scheduling Server" SchedServer

    ${If} $SSAlreadyInstalled S== "NeedUninstall"
        MessageBox MB_OK|MB_ICONSTOP "A different version of $(^Name) is already installed on this machine. $\nPlease uninstall the existing $(^Name) software before continuing the installation"
        Abort 
    ${EndIf}
    ${If} $SSAlreadyInstalled S== "Upgrade"
        MessageBox MB_YESNO "$(^Name) will be upgraded to version ${Version}. Do you want to continue?" IDYES ContinueInstall
        MessageBox MB_OK "Installation will be aborted"
        Abort
    ${EndIf}

    ${If} $SSAlreadyInstalled S== "Downgrade"
        MessageBox MB_YESNO "$(^Name) will be downgraded to version ${Version}. Downgrading is not recommended. $\nDo you want to continue?" IDYES ContinueInstall
        MessageBox MB_OK "Installation will be aborted"
        Abort
    ${EndIf}

    ${If} $SSAlreadyInstalled S== "SameVersion"
        MessageBox MB_ICONQUESTION|MB_YESNO "Same version of $(^Name) is already installed. $\nDo you want to repair/overwrite the installed version?" IDYES ContinueInstall
        MessageBox MB_OK "Installation will be aborted"
        Abort
    ${EndIf}
    ContinueInstall:

	!insertmacro checkAdminUser "installation"

	call checkJREVersion	


    ${If} $SSAlreadyInstalled S== "NotInstalled" 
        call checkIfServiceInstalled 
        SetOutPath $INSTDIR\conf 
        File /oname=schedulingserver.properties conf\schedulingserver.properties.win
        File conf\scheduling_service.ini
        ; Set output path to the installation directory.
        SetOutPath $INSTDIR
    
        ; Copy the component files/directories
        File LICENSE
    ${Else}
        !insertmacro checkIfServiceRunning
        ; If modifying the installed component, check for the file SchedulingServer-Version.jar
        ; and delete if it is present. This file is removed from this and future releases
        ${If} ${FileExists} $INSTDIR\bundles\SchedulingServer-Version.jar
            Delete $INSTDIR\bundles\SchedulingServer-Version.jar
        ${EndIF}        
        SetOutPath $INSTDIR
    ${EndIF}

    File doc\db\schema\*.sql
    
    SetOutPath $INSTDIR\bundles
	File /r /x *.svn bundles\*.*
    SetOutPath $INSTDIR\bin
	File /r /x *.svn bin\*.*

	${If} $SSAlreadyInstalled S== "NotInstalled" 
        SetOutPath $INSTDIR
        File servicewrapper\WindowsServiceWrapper\Release\schedulingservice.exe
        ; Add the RigClient service to the windows services
        ExecWait '"$INSTDIR\schedulingservice" install'
        ifErrors 0 WinServiceNoError
        MessageBox MB_OK "Error in executing schedulingservice.exe"
        ; TODO  Revert back the installed SS in case of error?
        Abort
        WinServiceNoError:
    ${EndIF}
    WriteRegStr HKLM "${REGKEY}" Path $INSTDIR
    WriteRegStr HKLM "${REGKEY}" CurrentVersion  ${Version}

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
	;${If} ${SectionIsSelected} ${RigClient}
	;	MessageBox MB_OK "Postinstall actions: $\n$\nUpdate the configuration file $INSTDIR\RigClient\config\rigclient.properties $\n$\nGo to the Windows Control Panel->Administrative Tools->Services and start the RigClient service"
	;${EndIf}
installEnd:
FunctionEnd

;--------------------------------
; Create uninstaller
Section -createUninstaller
	ClearErrors
	/*EnumRegKey $1 HKLM  "${REGKEY}" 0
	ifErrors 0 createUninstaller*/
    ${IfNot} ${SectionIsSelected} SchedServer
	   MessageBox MB_OK|MB_ICONSTOP  "No component selected for installation. Aborting the installation"
	   ABort
    ${EndIf}
	
	SetOutPath $INSTDIR
	WriteUninstaller $INSTDIR\uninstallSchedulingServer.exe
SectionEnd

;--------------------------------
; Uninstall Scheduling Server
Section "un.Sahara Scheduling Server" un.SchedulingServer
	!insertmacro checkAdminUser "uninstallation"
    !insertmacro checkIfServiceRunning
	Push $R1
	ReadRegStr $R1 HKLM "${REGKEY}" "Path"
	ClearErrors
	ExecWait '"$R1\schedulingservice" uninstall'
	ifErrors 0 WinServiceNoError
	MessageBox MB_ABORTRETRYIGNORE "Error in uninstalling Scheduling Server service.  $\n$\nIf the service is installed, manually uninstall the service from command prompt using: '$R1\schedulingservice uninstall' as admin and press 'Retry'. $\nIf the service is already uninstalled, press 'Ignore'. $\nPress 'Abort' to end the uninstallation" IDABORT AbortUninstall IDIGNORE WinServiceNoError 
	;TryAgain
	ExecWait '$R1\schedulingservice uninstall'
	ifErrors 0 WinServiceNoError
	MessageBox MB_OK "Error in uninstalling Scheduling Server service again. Aborting the uninstallation"
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
		MessageBox MB_ICONEXCLAMATION|MB_YESNO "$DisplayText" IDYES selectionEnd
		Abort
	${Else}
		MessageBox MB_OK "No compoennts selected"
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

