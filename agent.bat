set var=%1
set var=%var:x-vizier-client:=%
java -jar VIZIERJARPATH '%var%'
cmd /k
