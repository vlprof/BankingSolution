# Banking Solution
A simple banking solution that allow users to perform basic banking operations such as creating accounts, making deposits, and transferring funds.

## How to run
1. Using Docker and Docker Compose
   - Install [Docker](https://www.docker.com/)
   - Check if [docker-compose](https://docs.docker.com/compose/install/) is installed, now it's a part of the docker desktop installer. 
   If not, you can install it [manually](https://docs.docker.com/compse/install/linux/#install-the-plugin-manually0)
   - Run in the project's folder in terminal `docker-compose up` command
   
2. Without Docker
   - Install [Postgres](https://www.postgresql.org/download)
   - Install [Maven](https://maven.apache.org/download.cgi)
   - Install JDK 17+ version
   - Open `.evn` file and configure database related properties
   - Run `mvn spring-boot:run` in the terminal
   
You can explore REST API endpoints using [Swagger UI](http://localhost:8080/swagger-ui/index.html) 