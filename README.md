# Feedback Backend

The backend of the feedback analysis application.

## Deployment Build
[![Build Status](https://travis-ci.com/GRP-17/feedback-backend.svg?branch=master)](https://travis-ci.com/GRP-17/feedback-backend)[![codecov](https://codecov.io/gh/GRP-17/feedback-backend/branch/master/graph/badge.svg)](https://codecov.io/gh/GRP-17/feedback-backend)

## Development Build
[![Build Status](https://travis-ci.com/GRP-17/feedback-backend.svg?branch=development)](https://travis-ci.com/GRP-17/feedback-backend)[![codecov](https://codecov.io/gh/GRP-17/feedback-backend/branch/development/graph/badge.svg)](https://codecov.io/gh/GRP-17/feedback-backend)

## Useful Commands
### Maven
 Do the following to make Maven build the project, install any dependencies, etc:
    
    $ mvn clean install 
    
 Note: the following will allow you to specify where the pom.xml file is that you want to build. If you run the command from outside the directory (as in the travis file):

    $ mvn test (or install) -f <pom.xml filepath>

 Maven will run the tests automatically. Note: this is done by the maven-surefire plugin which is specified in the pom.xml (under plugins), and so is on our build path.  

 This will also happen on the travis-ci pipeline too, by default travis will run two commands for Maven:  

 First (install, skips tests and building javadocs):    

    $ mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V  

 Second (run tests, where -B removes the coloured output):     

    $ mvn test -B  

### Curl
In order to test the backend without a frontend, open terminal and navigate inside the project folder (where test.json is) present.:

    curl -X POST  https://feedback-analysis-grp-app.herokuapp.com/feedback -d "@test.JSON" -H "Content-Type:application/json"
 
To skip using test.json, and specify your own:

    curl -v -X POST https://feedback-analysis-grp-app.herokuapp.com/feedback -H 'Content-Type:application/json' -d '{"rating": 5, "text": "Your feedback test here"}'

## Files
### Root Directory
    .gitignore - defines files and file types to be ignored by git 
    .travis.yml - used to manipulate the travis ci pipeline & specify Heroku apps to push to
    system.properties - used by heroku to specify the version of java we are using.  
    
### BackendProject/
    BackendProject/ - folder is for the java project files and packages - should be able to import it into Eclipse as a Java                        project.  
    procfile - used by heroku to tell it what the dynos should run (just what it should 'host'/ run;   
               in our case it's a compiled jar file which starts the spring server)  
    pom.xml - the Maven build file, defines the build path i.e dependencies, plugins, etc.    
