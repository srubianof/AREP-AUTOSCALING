#!/bin/bash
docker-compose down
echo y | docker volume prune
echo y | docker system prune -a
docker-compose -f docker-compose.yml up -d
docker container ls
