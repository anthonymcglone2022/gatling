# What is the gatling repository?

- Contains two performance tests for the [postcodes](https://github.com/anthonymcglone2022/postcodes) repo.
- The test cases simulate X numbers of users per second (doing write or read operations) on a system.
- The cases measure the Mean Response Time (MRT) for each suite of requests.

## Pre-requisites before building the project

- Install [Maven 3](https://maven.apache.org/index.html) and [Java](https://www.oracle.com/java/technologies/downloads/)
- Run the postcodes project. 

## Running the scripts

-  From the project top-level directory, run **mvn gatling:test** on the command line.

