#!/bin/bash

# Function to generate a random alphanumeric string
generate_username() {
    cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 8 | head -n 1
}

# Loop to make the API call 1000 times
for ((i=1; i<=100; i++)); do
    # Generate a random username
    random_username=$(generate_username)

    # cURL command with Basic authentication
    response=$(curl -s -X 'POST' \
    'https://wso2is.centralus.cloudapp.azure.com/scim2/Users' \
    -H 'accept: application/scim+json' \
    -H 'Content-Type: application/scim+json' \
    -u admin:admin \
    -d '{
      "schemas": [],
      "name": {
        "givenName": "Kim",
        "familyName": "Berry"
      },
      "userName": "'"$random_username"'",
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
    }')

    # Extracting user ID from the response
    user_id=$(echo "$response" | jq -r '.id')

    # Storing user ID in user_id.txt
    echo "$user_id" >> user_id.txt

    # Output status
    echo "User $random_username created. User ID: $user_id"
done
