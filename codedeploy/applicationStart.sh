#!/bin/bash
echo whoami 
whoami
#sudo pwd
#sudo ls -al
#cd '/home/ubuntu/csye6225-cloud/webapp'
#sudo cp .env /etc/environment
#sudo cat .env >> /etc/environment
echo "printing env"
env
cd '/home/ubuntu/target'
sudo pwd
sudo ls -al
java -jar spring-boot-data-jpa-0.0.1-SNAPSHOT.jar >> /home/ubuntu/csye6225.log 2> /home/ubuntu/csye6225.log < /home/ubuntu/csye6225.log &
#lsof -t -i:8080
