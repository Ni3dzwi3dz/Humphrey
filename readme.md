# Humphrey
A cinema ticket booking Rest API

## How to build

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
