#!/bin/bash

IP=$1

#0
curl -X POST http://$IP:8080/clear
echo

#6
curl -d "courseName=Informatyka&capacity=0.5" -X POST http://$IP:8080/api/course
echo

#6
curl -d "courseName=Informatyka&capacity=0.5" -X POST http://$IP:8080/api/course
echo

#6
curl -d "courseName=Metaloznastwo&capacity=0.75" -X POST http://$IP:8080/api/course
echo

#5
curl  -X GET http://$IP:8080/api/course
echo

#9
curl -X GET http://$IP:8080/api/course/0/fill
echo

#9
curl -X GET http://$IP:8080/api/course/1/fill
echo

#9
curl -X GET http://$IP:8080/api/course/2/fill
echo

#1
curl -d "courseName=Informatyka&name=Konrad&surname=Filek&condition=Sick&birthDate=2000&points=20" -X POST http://$IP:8080/api/student
echo 

#1
curl -d "courseName=Informatyka&name=Konrad&surname=Filek&condition=Sick&birthDate=2000&points=20" -X POST http://$IP:8080/api/student
echo

#3
curl -X GET http://$IP:8080/api/student/0/grade?courseName=Informatyka
echo

#4
curl -X GET http://$IP:8080/api/student/csv
echo

#1
curl -d "courseName=Informatyka&name=Tom&surname=Papillo&condition=Sick&birthDate=2000&points=20" -X POST http://$IP:8080/api/student
echo 

#4
curl -X GET http://$IP:8080/api/student/csv
echo

#2
curl -X DELETE http://$IP:8080/api/student/1?courseName=Informatyka
echo

#2
curl -X DELETE http://$IP:8080/api/student/2?courseName=Informatyka
echo

#8
curl -X GET http://$IP:8080/api/course/0/students
echo

#8
curl -X GET http://$IP:8080/api/course/1/students
echo

#10
curl -d "id=0&rating=3.5" -X POST http://$IP:8080/api/rating
echo

#10
curl -d "id=0&rating=-3.5" -X POST http://$IP:8080/api/rating
echo

#7
curl -X DELETE http://$IP:8080/api/course/0
echo

#7
curl -X DELETE http://$IP:8080/api/course/5
echo
