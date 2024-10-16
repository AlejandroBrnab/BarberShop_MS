#!/usr/bin/env bash
#
# Sample usage:
#   ./test_all.bash start stop
#   start and stop are optional
#
#   HOST=localhost PORT=7000 ./test-em-all.bash
#
# When not in Docker
#: ${HOST=localhost}
#: ${PORT=7000}

# When in Docker
: ${HOST=localhost}
: ${PORT=8080}

#array to hold all our test data ids
allTestBarberIds=("185-48-4455")
allTestAppointmentIds=()

function assertCurl() {

  local expectedHttpCode=$1
  local curlCmd="$2 -w \"%{http_code}\""
  local result=$(eval $curlCmd)
  local httpCode="${result:(-3)}"
  RESPONSE='' && (( ${#result} > 3 )) && RESPONSE="${result%???}"

  if [ "$httpCode" = "$expectedHttpCode" ]
  then
    if [ "$httpCode" = "200" ]
    then
      echo "Test OK (HTTP Code: $httpCode)"
    else
      echo "Test OK (HTTP Code: $httpCode, $RESPONSE)"
    fi
  else
      echo  "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode, WILL ABORT!"
      echo  "- Failing command: $curlCmd"
      echo  "- Response Body: $RESPONSE"
      exit 1
  fi
}

function assertEqual() {

  local expected=$1
  local actual=$2

  if [ "$actual" = "$expected" ]
  then
    echo "Test OK (actual value: $actual)"
  else
    echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, WILL ABORT"
    exit 1
  fi
}

#have all the microservices come up yet?
function testUrl() {
    url=$@
    if curl $url -ks -f -o /dev/null
    then
          echo "Ok"
          return 0
    else
          echo -n "not yet"
          return 1
    fi;
}

#prepare the test data that will be passed in the curl commands for posts and puts
function setupTestdata() {

##CREATE SOME BARBER TEST DATA - THIS WILL BE USED FOR THE POST REQUEST
#
body=\
' {
    "address": {
        "streetAddress": "123 Main St",
        "city": "Anytown",
        "province": "Quebec",
        "country": "Canada",
        "postalCode": "A1B2C3"
    },
    "specialties": [
        {
            "specialty": "Haircut"
        },
        {
            "specialty": "Shave"
        }
    ],
    "firstName": "Juaaaaaaaaan",
    "lastName": "Doeeeeeeeeeeee",
    "emailAddress": "john@example.com",
    "salary": 50000.00,
    "phoneNumber": "768-456-9749",
    "dateOfBirth": "2012-01-01",
    "isAvailable": false
}'
    recreateBarber 1 "$body" "185-48-4455"

    body=\
'{
    "address": {
        "streetAddress": "789 Oak St",
        "city": "Somewhere",
        "province": "Quebec",
        "country": "Canada",
        "postalCode": "M1N2O3"
    },
    "firstName": "Aliciaaaaaaaaa",
    "lastName": "Johnsoooooon",
    "emailAddress": "alice@example.com",
    "phoneNumber": "748-107-9665"
}'
    recreateClient 1 "$body"

    body=\
'{
    "description": "MIIID!",
    "score": 0
}'
    recreateReview 1 "$body"
#

##CREATE SOME APPOINTMENT TEST DATA - THIS WILL BE USED FOR THE POST REQUEST
#
body=\
''
recreateBarberAppointment 1 "$body" "48200882-7c7f-4dcb-b5f5-fe6b4ae58251"

} #end of setupTestdata


#USING BARBER TEST DATA - EXECUTE POST REQUEST
function recreateBarber() {
    local testId=$1
    local aggregate=$2

    #create the barber and record the generated barberId
    customerId=$(curl -X POST http://$HOST:$PORT/api/v1/barbers -H "Content-Type:
    application/json" --data "$aggregate" | jq '.barberId')
    allTestBarberIds[$testId]=$barberId
    echo "Added Customer with barberId: ${allTestBarberIds[$testId]}"
}

#USING APPOINTMENT TEST DATA - EXECUTE POST REQUEST
function recreateBarberSale(){
  local testId=$1
  local aggregate=$2
  local barberId=$3

  #create the appoitntment and record the generated appointmentId
  appointmentId=$(curl -X POST http://$HOST:$PORT/api/v1/barbers/$barberId/appointments -H "Content-Type:
  application/json" --data "$aggregate" | jq '.appointmentId')
  allTestAppointmentIds[$testId]=$appointmentId
  echo "Added Appointment with appointmentId: ${allTestAppointmentIds[$testId]}"
}


#don't start testing until all the microservices are up and running
function waitForService() {
    url=$@
    echo -n "Wait for: $url... "
    n=0
    until testUrl $url
    do
        n=$((n + 1))
        if [[ $n == 100 ]]
        then
            echo " Give up"
            exit 1
        else
            sleep 6
            echo -n ", retry #$n "
        fi
    done
}

#start of test script
set -e

echo "HOST=${HOST}"
echo "PORT=${PORT}"

if [[ $@ == *"start"* ]]
then
    echo "Restarting the test environment..."
    echo "$ docker-compose down"
    docker-compose down
    echo "$ docker-compose up -d"
    docker-compose up -d
fi

#try to delete an entity/aggregate that you've set up but that you don't need. This will confirm that things are working
waitForService curl -X DELETE http://$HOST:$PORT/api/v1/barbers/542-21-6474 #POTENTIAL PROBLEM HERE

setupTestdata

#EXECUTE EXPLICIT TESTS AND VALIDATE RESPONSE
#
##verify that a get all barbers works
echo -e "\nTest 1: Verify that a get all barbers works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/barbers -s"
assertEqual 10 $(echo $RESPONSE | jq ". | length")
#
#
## Verify that a normal get by id of earlier posted barber works
echo -e "\nTest 2: Verify that a normal get by id of earlier posted barber works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/barbers/${allTestBarberIds[1]} '${body}' -s"
assertEqual ${allTestBarberIds[1]} $(echo $RESPONSE | jq .barberId)
assertEqual "\"John\"" $(echo $RESPONSE | jq ".firstName")
#
#
## Verify that an update of an earlier posted barber works - put at api-gateway has no response body
echo -e "\nTest 3: Verify that an update of an earlier posted barber works"
body=\
'{
  "firstName":"Christine UPDATED",
  "lastName":"Gerard",
  "emailAddress":"christine@gmail.com",
  "streetAddress": "99 Main Street",
  "city":"Montreal",
  "province": "Quebec",
  "country": "Canada",
  "postalCode": "H3A 1A1",
  "phoneNumbers": [
    {
      "type": "HOME",
      "number": "514-555-5555"
    },
    {
      "type": "WORK",
      "number": "514-555-5556"
    }
  ]
}'
assertCurl 200 "curl -X PUT http://$HOST:$PORT/api/v1/barbers/${allTestBarberIds[1]} -H \"Content-Type: application/json\" -d '${body}' -s"
#
#
## Verify that a delete of an earlier posted barber works
echo -e "\nTest 4: Verify that a delete of an earlier posted barber works"
assertCurl 204 "curl -X DELETE http://$HOST:$PORT/api/v1/barbers/${allTestBarberIds[1]} -s"
#
#
## Verify that a 404 (Not Found) status is returned for a non existing barberId (c3540a89-cb47-4c96-888e-ff96708db4d7)
echo -e "\nTest 5: Verify that a 404 (Not Found) error is returned for a get barber request with a non existing barberId"
assertCurl 404 "curl http://$HOST:$PORT/api/v1/barbers/c3540a89-cb47-4c96-888e-ff96708db4d7 -s"
#
#
## Verify that a 422 (Unprocessable Entity) status is returned for an invalid barberId (c3540a89-cb47-4c96-888e-ff96708db4d)
echo -e "\nTest 6: Verify that a 422 (Unprocessable Entity) status is returned for a get barber request with an invalid barberId"
assertCurl 422 "curl http://$HOST:$PORT/api/v1/barbers/c3540a89-cb47-4c96-888e-ff96708db4d -s"
#
#
## Verify that a get all barber appointments works
echo -e "\nTest 1: Verify that a get all barber appointments works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/barbers/185-48-4455/appointments -s"
assertEqual 2 $(echo $RESPONSE | jq ". | length")
#
#
## Verify that a get barber appointments works
echo -e "\nTest 1: Verify that a get all barber appointments works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/barbers/185-48-4455/appointments/${allTestAppointmentIds[1]} -H \"Content-Type: application/json\" -d '${body}' -s"
assertEqual ${allTestAppointmentIds[1]} $(echo $RESPONSE | jq ".appointmentId")
assertEqual "\"John\"" $(echo $RESPONSE | jq ".barberFirstName")
assertEqual "\"Doe\"" $(echo $RESPONSE | jq ".barberLastName")
#
#
## Verify that an update of an earlier posted barber appointment works
body=\
'json body here'
echo -e "\nTest 1: Verify that an update of an earlier posted barber appointment works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/barbers/185-48-4455/appointments/${allTestAppointmentIds[1]} -H \"Content-Type: application/json\" -d '${body}' -s"
## Verify changed
assertEqual ${allTestAppointmentIds[1]} $(echo $RESPONSE | jq ".appointmentId")
assertEqual "\"Joooooooohn\"" $(echo $RESPONSE | jq ".barberFirstName")
assertEqual "\"Doeeeeee\"" $(echo $RESPONSE | jq ".barberLastName")
#
#
# TODO: ADD verification of vehicle status change to SOLD
#
#
## Verify that a delete of an earlier posted client sale works
echo -e "\nTest 1: Verify that a delete of an earlier posted client sale works"
assertCurl 204 "curl -X http://$HOST:$PORT/api/v1/barbers/185-48-4455/appointments/${allTestSaleIds[1]} -s"
#
#
## Verify 404 not found for existing clientId
echo -e "\nTest 5: Verify that a 404 (Not Found) error is returned for a get client sale request with a non existing customerId"
assertCurl 404 "curl http://$HOST:$PORT/api/v1//barbers/185-48-445995/appointments/${allTestSaleIds[1]} -s"
#
#
## Verify 422 not found invalid clientId
echo -e "\nTest 5: Verify that a 422 (Unprocessable Entity) error is returned for a get client sale request with a invalid customerId"
assertCurl 422 "curl http://$HOST:$PORT/api/v1//barbers/185-4455/appointments/${allTestSaleIds[1]} -s"
#
#
## Verify 404 not found for non existing saleId
echo -e "\nTest 5: Verify that a 404 (Not Found) error is returned for a get client sale request with a non existing customerId"
assertCurl 404 "curl http://$HOST:$PORT/api/v1//barbers/185-48-4455/appointments/c3540a89-cb47-4c96-888e-ff96708db -s"
#
#


if [[ $@ == *"stop"* ]]
then
    echo "We are done, stopping the test environment..."
    echo "$ docker-compose down"
    docker-compose down
fi