# Humphrey
A cinema ticket booking Rest API

## Basic info
Project was written in  Scala 2.13.4, and uses Akka v2.7.0, and Akka-Http 10.4.0
Database operations are managed by Slick 3.4.1. Database of choice is SQLite.
For the sake of simplicity, project uses database as a file.

## How to build
Project utilises [SBT Native Packager](https://sbt-native-packager.readthedocs.io/en/stable/index.html).
So, in order to run the app, we should execute following commands. First, let's create
an executable for the project, by running in the root project folder:  
` sbt stage `  
This should create executable in ./target/universal/stage/bin. Now it's time to
create dockerfile, by running:   
` sbt docker:stage `  

This should create a dockerfile in ./target/docker/stage. So, next stage is building
a Docker image, by running:
` sbt docker:publishLocal`

The last step will be running the docker container, with port 9090 exposed:  
` docker run -p 9090:9090 humphrey:0.1.0`

## Testing
In the root folder, two scripts were prepared - one for Linux, one for windows.
Please note, that the linux one can fail because of line continuation characters and quote type 
your system uses - please change them, if it is the case.
Also, please remember about making the bash script executable by chmod.

## Endpoints

### /screenings  
Returns list of all screenings in database. Results can be filtered using start and end date 
Dates should be entered in YYYYMMDDhhmmss format. So example request would be:  
` /screenings/?startDate=20221125190000&endDate=20221126190000  ` 

### /screenings/{screeningId}
Returns details about a single screening - movie title, director, screening time 
and avaiable seats.
On the seat map, ones that already have been booked, are marked with 'X'. The numbered
ones are avaiable for booking.
Screenings are identified by screening Id's, that are displayed in the all screenings response

### /orders
This accepts only POST requests. Sample request body is:

` {"name":"Grzegorz",
   "surname":"Brzęczyszczykiewicz",
   "screeningId":2,
   "seats":[ 
            [3,1,"n"], 
            [3,2,"n"]]
   } ` 

Where name and surname are credentials, under which the order will be stored.
screeningId is the number identifying the screening, and seats is a list containing tuples
in format (row number, seat number, ticket type)


## Assumptions i took
* This is a cinema for nerds. Everyone finds it natural, that seats are numbered from 0
* All rows are equal in size
