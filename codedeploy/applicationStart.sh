#!/bin/bash
echo whoami 
whoami

echo "printing env"
env
sudo pwd
sudo ls -al
sudo systemctl start webapp.service


#lsof -t -i:8080
