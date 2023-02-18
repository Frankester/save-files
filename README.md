# SAVE FILE API
This REST API provides endpoints to store, retrieve, update, and delete files in the cloud using Amazon S3. The API can be used to store any type of file.

## Endpoints
- GET /files: Retrieves a list of all files stored in S3.
- GET /files/{idFile}: Retrieves a specific file based on its ID.
- POST /files: Uploads a file to S3. The file is sent in the request body.
- DELETE /files/{idFile}: Deletes a file from S3 based on its ID.
- PUT /files/{idFile}: Updates the stored metadata of an existing file in S3.

## Configuration
Before using this API, it is necessary to configure the Amazon S3 credentials in the `~/.aws/credentials` file. The values that should be added to the file are shown below:
```
[default]
aws_access_key_id=ACCESS_KEY_ID
aws_secret_access_key=SECRET_ACCESS_KEY
````

Replace ACCESS_KEY_ID and SECRET_ACCESS_KEY with the corresponding values from your Amazon S3 account.

## Running Locally
Clone the project
```bash
$ git clone https://github.com/Frankester/save-files.git
```
Go to the project directory
```bash
$ cd save-files
```
Build the Docker image
```bash
$ ./mvnw spring-boot:build-image -DskipTests
```
Start it with Docker Compose
```bash
$ docker-compose up
```

## Tech Stack

**Database:** MongoDB

**Spring Boot Starters:**  Web y  Data MongoDB

**Other dependencies:** Amazon S3 SDK, Lombok


