set "params=%*"
cd /d "%~dp0" && ( if exist "%temp%\getadmin.vbs" del "%temp%\getadmin.vbs" ) && fsutil dirty query %systemdrive% 1>nul 2>nul || (  echo Set UAC = CreateObject^("Shell.Application"^) : UAC.ShellExecute "cmd.exe", "/k cd ""%~sdp0"" && %~s0 %params%", "", "runas", 1 >> "%temp%\getadmin.vbs" && "%temp%\getadmin.vbs" && exit /B )

SET var=%cd%\..\agent.bat
set var=%var:\=\\%
powershell -Command "(gc test.reg) -replace 'VIZIERPATH', '%var%' | Out-File -encoding ASCII test.reg"

SET vizierjar=%cd%\..\target\vizier-agent2-0.0.1-SNAPSHOT.jar
SET vizierinvokedir=%cd%\..
powershell -Command "(gc ..\agent.bat) -replace 'VIZIERJARPATH', '%vizierjar%' | Out-File -encoding ASCII ..\agent.bat"
powershell -Command "(gc ..\agent.bat) -replace 'VIZIERINVOKEDIR', '%vizierinvokedir%' | Out-File -encoding ASCII ..\agent.bat"

reg import test.reg
cd ..
mvnw clean install

