#!/bin/sh

echo "Waiting for services and gateway to be healthy..."
sleep 45  # Wait 45s for the services to start

MAX_RETRIES=10
RETRY_INTERVAL=10

i=1
while [ $i -le $MAX_RETRIES ]; do
  echo "Attempt $i of $MAX_RETRIES..."

  # Call API and get only HTTP status code
  status=$(curl -s -o /dev/null -w '%{http_code}' -X POST http://hdv-api-gateway:8080/api/seat-availability-service/seats/init)

  if [ "$status" -eq 200 ]; then
    echo "Success on attempt $i (HTTP $status). Init seats API called."
    exit 0
  else
    echo "Failed on attempt $i (HTTP $status). Retrying in $RETRY_INTERVAL s..."
    sleep $RETRY_INTERVAL
  fi

  i=$((i + 1))
done

# If you run the whole round and still fail
echo "All $MAX_RETRIES attempts failed. Exiting with error"
exit 1
