#!/bin/bash

#sudo systemctl stop tomcat9

#sudo rm -rf /var/lib/tomcat9/webapp/ROOT

#sudo chown tomcat:tomcat /var/lib/tomcat9/webapp/ROOT.war

# cleanup log files
# sudo rm -rf /var/lib/tomcat9/logs/catalina*
# sudo rm -rf /var/lib/tomcat9/logs/*.log
# sudo rm -rf /var/lib/tomcat9/logs/*.txt

echo "in after install"
# sudo pwd
# sudo ls -al

#sudo kill -9 `sudo lsof -t -i:8080`
pid=$(sudo lsof -i tcp:8080 -t)
echo $pid

kill $pid
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/home/ubuntu/cloudwatch_config.json -s
sudo /bin/systemctl status amazon-cloudwatch-agent
sudo /bin/systemctl stop amazon-cloudwatch-agent
sudo /bin/systemctl start amazon-cloudwatch-agent

#sudo kill -9 `sudo lsof -t -i:8080`