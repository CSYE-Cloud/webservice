#!/bin/bash

echo "in after install"
# sudo pwd
# sudo ls -al

#sudo kill -9 `sudo lsof -t -i:8080`
pid=$(sudo lsof -i tcp:8080 -t)
echo $pid

# sudo systemctl start webapp.service
# sudo chown ec2-user:ec2-user
#removing previous build ROOT folder
# sudo rm -rf /home/ec2-user/*.jar
