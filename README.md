# feedback-backend
The backend of the application for feedback analysis.

Files:  
    
    'src/' - folder is for the java project files and packages.  

    .gitignore - defines files and file types to be ignored.  

    .travis.yml - used to manipulate the travis ci pipeline, doesn't need anything really.  

    pom.xml - the Maven build file, defines the build path i.e dependencies and plugins etc...  

    procfile - used by heroku to tell it what the 'dynos' should run (just what it should 'host'/ run,   
                in our case it would be a compiled jar file which starts the spring server).  

    system.properties - used by heroku to specify the version of java we are using.  

Useful Commands:  

 To use Maven to build the project, (which at the moment contains the bare minimum):  

    $ mvn clean install 

 This should build the file, install any dependencies etc, there are also other ways of doing this I think, such as:

    $ mvn test 

 also does the same I think.

 Both will find the MainTest.java JUnit test because:  

   1.  the name ends (or starts) with Test  
   1.  and it is in the 'test/java/' directory  

 After it is found mvn will run the test automatically.  
    Note: this is done by the maven-surefire plugin which is specified in the pom.xml (under plugins), and so is on our build path.  

 This will also happen on the travis-ci pipeline too :D  
 by default travis will run two commands for Maven:  

 First:    

    $ mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V  

 Note: tests are skipped here and run in the other command (below), also it skips building the javadocs  

 Second:     

    $ mvn test -B  

 where the -B flag just removes the coloured output.. I think  

![travis-log-example1](https://github.com/GRP-17/feedback-backend/images/travis-log-example1.png)  
![travis-log-example2](https://github.com/GRP-17/feedback-backend/images/travis-log-example2.png)

p.s the pictures was a experiment with the .md syntax / markdown syntax.

