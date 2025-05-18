#!/bin/bash
set -e

echo "Waiting for databases to be fully initialized..."
sleep 10

echo "Initializing movie database..."
mysql -h hdv-movie-db -u root -proot < /scripts/sql/init_movie_db.sql

echo "Initializing customer database..."
mysql -h hdv-customer-db -u root -proot < /scripts/sql/init_customer_db.sql

echo "Initializing notification database..."
mysql -h hdv-notification-db -u root -proot < /scripts/sql/init_notification_db.sql

echo "Initializing payment database..."
mysql -h hdv-payment-db -u root -proot < /scripts/sql/init_payment_db.sql

echo "Initializing seat movie database..."
mysql -h hdv-seat-availability-db -u root -proot < /scripts/sql/init_seat_availability_db.sql

echo "Initializing ticket booking database..."
mysql -h hdv-ticket-booking-db -u root -proot < /scripts/sql/init_ticket_booking_db.sql

echo "All databases have been initialized successfully!"
