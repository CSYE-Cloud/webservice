#!/bin/bash
echo "before install"
#sudo pwd
#sudo ls -al
#sudo systemctl stop tomcat9

#check /home/ubuntu/filename exits
FILE=/home/ubuntu/filename
while [ ! -f "$FILE" ]
do
    sleep 100
    #echo "$FILE exists."
done 
    # echo "$FILE does not exist."
    # sleep 300
