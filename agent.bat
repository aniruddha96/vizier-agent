set var=%1
set var=%var:x-vizier-client:=%
cd /d VIZIERINVOKEDIR
java -jar VIZIERJARPATH '%var%'
cmd /k
