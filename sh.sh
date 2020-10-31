#!/bin/bash
docker-compose down 
docker volume prune 
docker system prune -a 

docker-compose -f docker-compose.yml up -d
docker container ls
