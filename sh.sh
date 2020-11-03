#!/bin/bash
sudo aa-remove-unknown
sudo docker-compose down
echo y | sudo docker volume prune
echo y | sudo docker system prune -a
sudo docker-compose -f docker-compose.yml up -d
sudo docker container ls
