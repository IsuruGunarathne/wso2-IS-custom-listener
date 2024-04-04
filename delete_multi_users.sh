#!/bin/bash

# Check if user_id.txt exists
if [ ! -f "user_id.txt" ]; then
  echo "Error: user_id.txt not found."
  exit 1
fi

# Loop through each user ID in user_id.txt and make DELETE request
while IFS= read -r user_id; do
  response=$(curl -s -X 'DELETE' \
  "https://wso2is.centralus.cloudapp.azure.com/scim2/Users/$user_id" \
  -u admin:admin \
  -H 'accept: */*')
  
  echo "Deleted user with ID: $user_id"
done < "user_id.txt"

# Remove user_id.txt
rm user_id.txt