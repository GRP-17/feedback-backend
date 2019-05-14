# Feedback Backend

The backend of the feedback analysis application.

## Builds
### Deployment Build [![Build Status](https://travis-ci.com/GRP-17/feedback-backend.svg?branch=master)](https://travis-ci.com/GRP-17/feedback-backend)[![codecov](https://codecov.io/gh/GRP-17/feedback-backend/branch/master/graph/badge.svg)](https://codecov.io/gh/GRP-17/feedback-backend)

### Development Build [![Build Status](https://travis-ci.com/GRP-17/feedback-backend.svg?branch=development)](https://travis-ci.com/GRP-17/feedback-backend)[![codecov](https://codecov.io/gh/GRP-17/feedback-backend/branch/development/graph/badge.svg)](https://codecov.io/gh/GRP-17/feedback-backend)

## Documentation
| Name | URL |
| --- | --- |
| Group Report | https://drive.google.com/open?id=1mbjr1n2SyjgtVKfPI2wD-miIsA2EuCMv |
| Technical Document | https://drive.google.com/open?id=17yByxnY16siJlCuh0ghhaaALpeEsluFE |
| User Manual | https://drive.google.com/open?id=1fTtcK_afQHLy4fY02K0Pn4C1mNZCCNJK |

## Useful Links

| Name | URL |
| --- | --- |
| Trello Board | https://trello.com/segrp17 |
| Document Repository | https://drive.google.com/drive/folders/1CePkA1cSuXFdGF-Pn8NyJPchsSWu4Yc2 |
| Wiki | https://github.com/GRP-17/feedback-backend/wiki |
| Deployment App | https://feedback-analysis-grp-app.herokuapp.com/ |
| Development App | https://feedback-analysis-grp-app-dev.herokuapp.com/ |

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
*JDK 1.8* - the project is written in Java.

*Maven 3.x* - this will handle any building, and dependencies. The Apache Maven build system can be found [here](https://maven.apache.org/).

### Installing
Firstly, inside the project directory, run the following command in order to build the maven project:

    $ mvn clean install 
    
Secondly, in the same directory, run the following command to run the project:

    $ mvn spring-boot:run
    
Congratulations! You should now be able to visit the site at: http://localhost:8080/

## More Useful Commands
In order to test the backend without a frontend, open terminal and navigate inside the project folder (where test.json is) present.:

    curl -X POST  https://feedback-analysis-grp-app.herokuapp.com/feedback -d "@test.JSON" -H "Content-Type:application/json"
 
To skip using test.json, and specify your own:

    curl -v -X POST https://feedback-analysis-grp-app.herokuapp.com/feedback -H 'Content-Type:application/json' -d '{"rating": 5, "text": "Your feedback test here"}'

## Files
### Root Directory
    .gitignore - defines files and file types to be ignored by git 
    .travis.yml - used to manipulate the travis ci pipeline & specify Heroku apps to push to
    system.properties - used by heroku to specify the version of java we are using
### BackendProject/
    BackendProject/ - folder is for the java project files and packages - this can be accessed using Eclipse
    procfile - used by heroku to tell it what the dynos should run (just what it should 'host'/ run;   
               in our case it's a compiled jar file which starts the spring server)  
    pom.xml - the Maven build file, defines the build path i.e dependencies, plugins, etc.    
    
## Related
- [feedback-frontend](https://github.com/GRP-17/feedback-frontend)
- [feedback-component](https://github.com/GRP-17/feedback-component)

    
## Index of Maven Dependencies
|                          Dependency                          |  Version  |                         Description                          |                            Usage                             |
| :----------------------------------------------------------: | :-------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| [spring-boot-starter-web](https://spring.io/projects/spring-boot) | `2.0.0.RELEASE` | Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container. | Used to start the web application. |
| [spring-boot-starter-hateoas](https://spring.io/projects/spring-boot) | `2.0.0.RELEASE` | Starter for building hypermedia-based RESTful web application with Spring MVC and Spring HATEOAS. | Used to start the web application. |
| [spring-boot-starter-data-jpa](https://spring.io/projects/spring-boot) | `2.0.0.RELEASE` | Starter for using Spring Data JPA with Hibernate. | Allows the usage of JPA repositories for data storage. |
| [spring-boot-starter-test](https://spring.io/projects/spring-boot) | `2.0.0.RELEASE` | Starter for testing Spring Boot applications with libraries including JUnit, Hamcrest and Mockito. | Used to provide JUnit testing within the startup. |
| [spring-boot-devtools](https://spring.io/projects/spring-boot) | `2.0.0.RELEASE` | Provides additional development-time features. | Makes development more simplistic. |
| [junit](https://junit.org/junit4/) | `4.12` | JUnit is a simple framework to write repeatable tests. It is an instance of the xUnit architecture for unit testing frameworks. | Used to enable unit testing. |
| [mariadb-java-client](https://mariadb.org/) | `2.2.5` | JDBC driver for MariaDB and MySQL. | Used to allow connections to the MariaDB database. |
| [hibernate-validator](http://hibernate.org/) | `6.0.13.Final` | Hibernate Validator allows to express and validate application constraints. The default metadata source are annotations, with the ability to override and extend through the use of XML. | Used to specify constraints in the data passed through, for example: the min & max rating. |
| [jaxb-api](https://oracle.org/) | `2.3.0` | Provides a fast and convenient way to bind XML schemas and Java representations. | Used to map objects to XML. |
| [ibm-watson](https://www.ibm.com/watson) | `6.9.3` | Client library to use the IBM Watson Services. | Used to analyse sentiments of text. |
| [jackson-databind](https://github.com/FasterXML/jackson) | `2.9.4` | General data-binding functionality for Jackson. | Used to map objects to JSON. |
