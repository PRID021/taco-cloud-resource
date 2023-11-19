# Configuration
When using docker first create docker image using "(sudo) docker build -t your_image_name ."
Then using "(sudo) docker-compose up" to start and create necessary database.

When using local sql database, remember create database before the spring application kick in, so that Hibernate can check and 
create tables and constrains key for the app.
