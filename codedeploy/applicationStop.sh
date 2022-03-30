#!/bin/bash

pid=$(sudo lsof -i tcp:8080 -t)
echo $pid

sudo systemctl stop webapp.service

echo "ending"

