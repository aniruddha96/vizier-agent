set var=%1
set var=%var:x-vizier-client:=%
cd /d D:\github\vizier-agent\windows_installer\..
java -jar D:\github\vizier-agent\windows_installer\..\target\vizier-agent2-0.0.1-SNAPSHOT.jar %1
cmd /k

