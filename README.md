# Custom AWS AutoScaling 
The idea of AWS Auto Scaling is to allow applications to maintain a steady and predictable performance by automatically adjusting the capacity of the instances used.
To view this behaviour we are going use [Michael's Yaworski](https://github.com/mikeyaworski/PrimeFactorization) implementation of Prime Factorization where given a number n, it will be reduced into a product of only prime numbers and its multiplicity.

In the following figure the architecture is explained. There are two EC2 instances that will be triggered when the CPU reaches 50% or more usage. 

![](https://cdn.discordapp.com/attachments/748398289514397717/772687902672289792/Blank_diagram-5.png)


## Badges
Continuous Integration  [![CircleCI](https://circleci.com/gh/circleci/circleci-docs.svg?style=svg)](https://circleci.com/gh/srubianof/PrimeNumbers)

## Getting Started
The following steps are required in order to get a copy of the project, and be able to run it.
### Prerequisites
You need to have installed the next software to successfully run the project:

* Java recommended Java 8
* Maven
* Git
* Docker
### Installing
This a step by step guide that will tell you how to get a copy of the project and how to execute
        
First get a copy of the repository
```
git clone https://github.com/srubianof/AREP-AUTOSCALING.git
```

## Program execution
For a proper execution of the project, please execute it in a UNIX environment where you will be able to use the provided shell script for a controlled execution:

```
sh sh.sh
```

![](https://cdn.discordapp.com/attachments/748398289514397717/762455756791349278/carbon-2.png)

## AWS Auto Scaling 

To be able to set up a scaled and load-balanced application, follow the next step by step guide. 

1. Open the Amazon EC2  [console](https://console.aws.amazon.com/ec2/) and launch a new instance, suggestion: choose Ubuntu Server 18.04 LTS, t2.micro instance type, and open the port 4567 in security group. 
2. Once the instance is generated, access to it:
```
ssh -i "<YOUR
-KEY>.pem" <YOUR_USER>@ec2<EC2_IP>.compute-1.amazonaws.com
```
3. Install docker using the following commands 
```
sudo apt update
sudo apt install apt-transport-https ca-certificates curl software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"
sudo apt install docker-ce
sudo usermod -aG docker username
```
4. Install docker-compose with the following commands
```
sudo curl -L "https://github.com/docker/compose/releases/download/1.27.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
```
5. To be able to launch our docker compose each time the machine stars, we are going to create a new service that will execute it as show below. 
![](https://cdn.discordapp.com/attachments/748398289514397717/772710385139580958/carbon-15.png)
Once the service is created you must grant permissions, so it can be executed, read and written with chmod 777, and finally enable it.
```
systemctl enable docker-service.service
```
6. Once we have our main machine up and running, we are going to create an Amazon Machine Image (AMI) which will provide the information required to launch a new instance.
![](https://cdn.discordapp.com/attachments/748398289514397717/772711784313782304/Screen_Shot_2020-10-31_at_6.20.14_PM.png)
Then we are going to give it a name
![](https://cdn.discordapp.com/attachments/748398289514397717/772711793323278366/Screen_Shot_2020-10-31_at_6.20.39_PM.png)
7. We are going to create a new launch configuration that is a template that an EC2 Auto Scaling group uses to launch EC2 instances. 
![](https://cdn.discordapp.com/attachments/748398289514397717/772711809970339840/Screen_Shot_2020-10-31_at_6.22.17_PM.png)
![](https://cdn.discordapp.com/attachments/748398289514397717/772711831730913320/Screen_Shot_2020-10-31_at_6.23.43_PM.png)
Is important to select the previously created Key Pair (Ubuntu EC2 key pair)
8. Next create an auto scaling group where a collection of Amazon EC2 instances that are treated as a logical grouping for the purposes of automatic scaling and management. 
![](https://cdn.discordapp.com/attachments/748398289514397717/772713585091674112/Screen_Shot_2020-10-31_at_6.26.20_PM.png)
It's important to select 3 VPCs where the EC2 Auto Scaling will balance our instances
![](https://media.discordapp.net/attachments/748398289514397717/772713766579339274/Screen_Shot_2020-10-31_at_6.29.28_PM.png?width=1344&height=1468)
We are going to select the group size, meaning the amount of new allowed instances, as well as our scaling policies, in this case we are going to set a max 35% CPU usage to trigger a new EC2 instance
![](https://media.discordapp.net/attachments/748398289514397717/772713765275172864/Screen_Shot_2020-10-31_at_6.27.40_PM.png?width=1344&height=1468)
9. A load balancer will be created in order to redirect all the traffic to one IP and according to the previously created policy will instantiate new EC2s as needed

- Select Application Load Balancer 
![](https://cdn.discordapp.com/attachments/748398289514397717/772715065462882304/Screen_Shot_2020-10-31_at_7.11.44_PM.png)
- Select the previously defined VPCs
![](https://media.discordapp.net/attachments/748398289514397717/772715061722087484/Screen_Shot_2020-10-31_at_7.12.19_PM.png?width=1344&height=1468)
- Configure the security group, so the EC2 instances will be able to be accessed
-![](https://media.discordapp.net/attachments/748398289514397717/772715057398153226/Screen_Shot_2020-10-31_at_7.13.24_PM.png?width=1344&height=1468)
- Configure routing, meaning specifying the ports in our VPC 
![](https://media.discordapp.net/attachments/748398289514397717/772715720446050332/Screen_Shot_2020-10-31_at_7.14.10_PM.png?width=1344&height=1468)
10. So all the request are handled in a correct manner, add the load balancer to our Auto Scaling group as shown below 
![](https://cdn.discordapp.com/attachments/748398289514397717/772724641399701514/Screen_Shot_2020-10-31_at_7.16.19_PM.png)

## Load tests and Results
To simulate concurrent users executing the same requests, you can use Newman to run parallel collections with the following command, this will execute 50 requests to our API.
```
newman run collection.postman_collection.json -e environment.postman_environment.json -n 50
```

As shown below each intance behaves different corresponding to the load made, as well we can see that after the CPU reaches 35% in the first instance the following instances are triggered
![](https://cdn.discordapp.com/attachments/748398289514397717/772711827951058944/Screen_Shot_2020-11-01_at_1.32.06_AM.png)
![](https://cdn.discordapp.com/attachments/748398289514397717/772711829331378186/Screen_Shot_2020-11-01_at_1.32.10_AM.png)
![](https://cdn.discordapp.com/attachments/748398289514397717/772711830677094400/Screen_Shot_2020-11-01_at_1.32.14_AM.png)

Thanks to the Auto Scaling feature offered by AWS we can see that the Response time is reduced as more instances are launched as the load increases
![](https://cdn.discordapp.com/attachments/748398289514397717/772711833995182080/Screen_Shot_2020-11-01_at_1.20.15_AM.png)


As the load increases and is gradually reduced, AWS Auto Scaling feature triggers and terminates the instances automatically, the following figures demonstrates how this happens, and the alerts that are launched

CPU USE
![](https://cdn.discordapp.com/attachments/748398289514397717/772711827951058944/Screen_Shot_2020-11-01_at_1.32.06_AM.png)
![](https://cdn.discordapp.com/attachments/748398289514397717/772711829331378186/Screen_Shot_2020-11-01_at_1.32.10_AM.png)
![](https://cdn.discordapp.com/attachments/748398289514397717/772711830677094400/Screen_Shot_2020-11-01_at_1.32.14_AM.png)

ALERTS
![](https://cdn.discordapp.com/attachments/748398289514397717/772727668260208640/Screen_Shot_2020-11-01_at_5.40.48_PM.png)

## Built With
* Java 8
* Docker
* Git - Version-control system
* [Maven](https://maven.apache.org) - Dependency Management
* AWS - Amazon Web Services

## Author

[**Santiago Rubiano Fierro**](https://github.com/srubianof) Software Engineering Student

## License

 This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/srubianof/AREP-LAB-1/blob/master/LICENSE) file for details.
