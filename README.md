# csv-sorting-service

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

### HTTP Status of response
* Currently does not respond with an HTTPStatus

### Thoughts

1. First Item
2. Second Item
