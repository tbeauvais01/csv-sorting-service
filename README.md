# csv-sorting-service
## How to use
1. Build
```mvn package```
2. Run
    1. Run with java ```csv-sorting-service>java -jar target/csv-sorting-service-0.0.1-SNAPSHOT.jar```
        1. Test with ```curl -X POST -H "Content-type: text/csv" --data-binary @post-data.txt "http://localhost:8080/sortCsv/City"```
        2. Where ```post-data.txt``` contains the CSV payload, and ```City``` is the field to sort by
    2. Run with docker
        1. ```docker build --tag=csv-sorting-service:latest .```
        2. ```docker run -p8887:8080 csv-sorting-service:latest```
            1. Test with ```curl -X POST -H "Content-type: text/csv" --data-binary @post-data.txt "http://localhost:8887/sortCsv/City"```
            2. Where ```post-data.txt``` contains the CSV payload, and ```City``` is the field to sort by
            3. NB: the port changes in curl command, as compared to running spring-boot only
    3. Run with docker compose
       1.  ```docker-compose up --build```
            1. Test with ```curl -X POST -H "Content-type: text/csv" --data-binary @post-data.txt "http://localhost:18080/sortCsv/City"```
            2. Where ```post-data.txt``` contains the CSV payload, and ```City``` is the field to sort by
            3. NB: the port changes in curl command, as compared to running spring-boot only, or running with docker
3. Sample test data:
    1. The following are the contents of a sample payload ```post-data.txt``` for use with the previous ```curl``` commands
    The field to sort by should be in the header or first row; ```City``` was used in examples above
                            
            City,State,Motto,Mayor
            Portland,Oregon,The City That Works,Ted Wheeler
            Boston,Massachusetts,It’s All Here,Michelle Wu
            New Orleans,Louisiana,City of Yes,LaToya Cantrell
## TO-DO's
### Features
* Edge Cases: CSV Standard*
    + Handle commas in within double quoted values i.e ```value1, "value "",2""", value3```
    + Handle line breaks within double quoted values i.e ```value1, "value ""\n2""", value3```

### Documentation 
* JavaDoc
* Code comments
* Improve Readme

### Tests
* Fix disabled test
* Add tests for the controller
* Test on Unix and or Mac. It's only been run on Windows.
* Test project in other IDEs. It's only been run on IntelliJ.

### Performance
* Performance improvements
    - Combine  split line and parse line to only scan input once, currently does it twice
    - Large input files

### Refactoring
* Code cleanup

### Dependencies
* Pom is very basic right now, could perhaps restrict used dependencies to needed only

### Error Messages
* Determine appropriate error messages both for logging and response from service
* Default Spring error page

### Exception Handling
* Revisit Exception handling to cover more cases

### Logging
* Add logging

### Metrics
* Consider adding metrics either through framework or possibly logging

### Miscellaneous
* Tag initial version in git
* Parameterize artifact version, currently it appears in multiple places

### HTTP Status of response
* Currently, does not respond with an HTTPStatus

### Thoughts
1. Lot's more to do.
2. Refactoring and scanning the input CSV only once would improve performance on large inputs
