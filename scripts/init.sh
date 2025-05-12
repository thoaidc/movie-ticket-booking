#!/bin/bash
set -e

echo "Waiting for databases to be fully initialized..."
sleep 10

# Khởi tạo customer database
echo "Initializing customer database..."
mysql -h hdv-customer-db -u root -proot hdv_customer < /scripts/sql/init_customer_db.sql

# Initial movie database
echo "Initializing movie database..."
mysql -h hdv-movie-db -u root -proot hdv_movie < /scripts/sql/init_movie_db.sql

# Initial notification database
echo "Initializing notification database..."
mysql -h hdv-notification-db -u root -proot hdv_notification < /scripts/sql/init_notification_db.sql

# Initial payment database
echo "Initializing payment database..."
mysql -h hdv-payment-db -u root -proot hdv_payment < /scripts/sql/init_payment_db.sql

# Initial seat movie database
echo "Initializing seat movie database..."
mysql -h hdv-seat-movie-db -u root -proot hdv_seat_movie < /scripts/sql/init_seat_movie_db.sql

# Initial ticket booking database
echo "Initializing ticket booking database..."
mysql -h hdv-ticket-booking-db -u root -proot hdv_ticket_booking < /scripts/sql/init_ticket_booking_db.sql

echo "All databases have been initialized successfully!"
