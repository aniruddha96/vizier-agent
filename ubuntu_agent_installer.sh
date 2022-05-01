chmod +x mvnw
chmod +x vizier-agent-ubuntu.sh
./mvnw clean install
wd=`pwd`
cd ubuntu_agent
execpath='s!REPLACE_WITH_EXEC_PATH!Exec='$wd'/vizier-agent-ubuntu.sh!g'
echo $execpath
cp vizier.desktop vizier-client.desktop
sed -i $execpath vizier-client.desktop

cp vizier-client.desktop to /usr/share/applications

cd /usr/share/applications

update-desktop-database

xdg-mime default vizier.desktop x-scheme-handler/x-vizier-client
