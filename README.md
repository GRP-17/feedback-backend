# feedback-backend

[![Build Status](https://travis-ci.com/GRP-17/feedback-backend.svg?branch=master)](https://travis-ci.com/GRP-17/feedback-backend)[![codecov](https://codecov.io/gh/GRP-17/feedback-backend/branch/master/graph/badge.svg)](https://codecov.io/gh/GRP-17/feedback-backend)

The backend of the application for feedback analysis.


Useful Commands:  

 Do the following to make Maven build the project, install any dependencies, etc:
    
    $ mvn clean install 
    
 Note that the following will allow you to specify where the pom.xml file is that you want to build. If you run the command from outside the directory (as in the travis file):

    $ mvn test (or install) -f <pom.xml filepath>

 Maven will run the tests automatically.  
​    Note: this is done by the maven-surefire plugin which is specified in the pom.xml (under plugins), and so is on our build path.  

 This will also happen on the travis-ci pipeline too :D  
 by default travis will run two commands for Maven:  

 First:    

    $ mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V  

 Note: tests are skipped here and run in the other command (below), also it skips building the javadocs  

 Second:     

    $ mvn test -B  

 where the -B flag just removes the coloured output.

In order to test the backend without a frontend, open terminal and navigate inside the project folder where test.json is present. Then run the following command:

    curl -X POST  https://feedback-analysis-grp-app.herokuapp.com/feedback -d "@test.JSON" -H "Content-Type:application/json"

Files:      

    'BackendProject/' - folder is for the java project files and packages - should be able to import it into Eclipse as a Java project.  
    
    .gitignore - defines files and file types to be ignored.  
    
    .travis.yml - used to manipulate the travis ci pipeline, doesn't need anything really.  
    
    pom.xml - the Maven build file, defines the build path i.e dependencies and plugins etc...  
    
    procfile - used by heroku to tell it what the 'dynos' should run (just what it should 'host'/ run,   
                in our case it would be a compiled jar file which starts the spring server).  
    
    system.properties - used by heroku to specify the version of java we are using.  
