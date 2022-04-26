#!/bin/sh
unzip -d ../mac_agent/ ../mac_agent/temp.app.zip
echo 'export VIZIERPATH=$(pwd)/../abc/arget/vizier-agent2-0.0.1-SNAPSHOT.jar' >> ~/.zshenv
source ~/.zshenv

chmod +x ../mvnw
cd ..
./mvnw clean install