#!/bin/bash
# sudo systemctl stop tomcat9
#sudo kill -9 `sudo lsof -t -i:8080`
# sudo rm -rf webapp/
# sudo rm -rf codedeploy/
# sudo rm -f appspec.yml
#sudo kill -9 `sudo lsof -t -i:8080`

pid=$(sudo lsof -i tcp:8080 -t)
echo $pid

kill $pid

# sudo ls -al
sudo rm -rf target/
sudo rm -rf codedeploy/
sudo rm -f appspec.yml
sudo rm -f cloudwatch_config.json
# sudo ls -al
echo "ending"
# sudo pwd
# sudo ls -al
