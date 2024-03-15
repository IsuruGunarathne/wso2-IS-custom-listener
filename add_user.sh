#!/bin/bash

# Check if username parameter is provided
if [ $# -ne 1 ]; then
  echo "Usage: $0 <user_name>"
  exit 1
fi

# Assign username from parameter
user_name=$1

# cURL command with Basic authentication
curl -X 'POST' \
'https://localhost:9443/scim2/Users' \
-H 'accept: application/scim+json' \
-H 'Content-Type: application/scim+json' \
-u admin:admin \
-d '{
  "schemas": [],
  "name": {
    "givenName": "Kim",
    "familyName": "Berry"
  },
  "userName": "'"$user_name"'",
  "password": "MyPa33w@rd",
  "emails": [
    {
      "type": "home",
      "value": "kim@gmail.com",
      "primary": true
    },
    {
      "type": "work",
      "value": "kim@wso2.com"
    }
  ],
  "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User": {
    "employeeNumber": "1234A",
    "manager": {
      "value": "Taylor"
    }
  }
}'
