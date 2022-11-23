@echo off

echo Getting all screenings information: & echo.
echo Request URL is http://localhost:9090/screenings/ & echo.
pause
curl --location --request GET "http://localhost:9090/screenings/"
echo.

echo Press any key to continue. Next, we will get all screenings beetween 25.11.2022 19:00 and 26.11.2022 19:00 & echo.
echo Request URL will be: "http://localhost:9090/screenings/?startDate=20221125190000&endDate=20221126190000" & echo.
pause
curl --location --request GET "http://localhost:9090/screenings/?startDate=20221125190000&endDate=20221126190000"
echo.

echo Now, we choose screening no.2 and request for detailed info. & echo.
echo Request url is http://localhost:9090/screenings/2 & echo.
pause
curl --location --request GET http://localhost:9090/screenings/2
echo.

echo In case, there is no screening in the database, by chosen id, system should return proper information & echo.
echo Request url is http://localhost:9090/screenings/22 & echo.
pause
curl --location --request GET "http://localhost:9090/screenings/22"
echo.

echo Our first attempt to book tickets should fail, because someone already booked seats we chose & echo.
echo Request url is http://localhost:9090/orders & echo.
pause
curl --location --request POST "http://localhost:9090/orders" ^
--header "Content-Type: application/json" ^
--data-raw "{\"name\":\"Grzegorz\",\"screeningId\":2,\"seats\":[[3,1,\"n\"],[3,2,\"n\"]],\"surname\":\"Brzęczyszczykiewicz\"}"
echo.

echo Now, let`s move one row up. These seats should be ok & echo.
echo Request url is http://localhost:9090/orders & echo.
pause
curl --location --request POST "http://localhost:9090/orders" ^
--header "Content-Type: application/json" ^
--data-raw "{\"name\":\"Grzegorz\",\"screeningId\":2,\"seats\":[[4,1,\"n\"],[4,2,\"n\"]],\"surname\":\"Brzęczyszczykiewicz\"}"
echo.

echo In case, the screening is in 15 minutes, or less, or in the past, error message will be displayed & echo.
echo Request url is http://localhost:9090/orders, screeningId is 9 & echo.
pause
curl --location --request POST "http://localhost:9090/orders" ^
--header "Content-Type: application/json" ^
--data-raw "{\"name\":\"Grzegorz\",\"screeningId\":9,\"seats\":[[3,1,\"n\"],[3,2,\"n\"]],\"surname\":\"Brzęczyszczykiewicz\"}"