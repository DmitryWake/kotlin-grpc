# kotlin-grpc
## How to start
1. You have to get IntlliJIDEA installed (It may requires JDK and gradlew), PostgreSQL
2. Clone the repository (via gitbash or IDE)
3. Open project via IDE
4. Sync project with Gradle files
5. Build project via console: `./gradlew assemble` or via IDEA (Make Project). It is required to generate build files
6. In database/DatabaseFactory you must specify the data of your database (url, databasePassword)
7. Optional: configure the port of Server in ServerApplication -> SERVER_PORT
8. Run ServerApplication via IDE
9. You are free to use any application to make requests to the server. Or you can use this console application: run client/ClientApplication
