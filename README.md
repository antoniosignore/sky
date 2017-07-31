# Parental Control Service

The task for this assignment is to create a parental control service for a video on demand platform.

## Installation
This project requires JDK 8 and maven for building and testing it. 

Install Java on linux using this command:

    sudo apt-get install openjdk-8-jdk
    
Install maven    
    
    sudo apt-get install maven

## Design
The `ParentalControlService` requires a reference to a `MovieService`. 

Because there was no requirements for the rating strings to be dynamic, I preferred a dry design with a RatingEnum,
containing the mapping between the allowed levels, a mnemonic enum and an integer. 

This avoids the need for comparator because the decision to allow or not a user is done by a simple integer compare op.

Note that this assumption keeps the code simple and small. 

Hovewer is makes not extendible in terms of adding without recompilation different levels. 

In the specs it was not clear so I went for the most dry design. 

In case there are different levels then this solution at least restrict the changes to the RatingEnum only class.


## Reference Implementation
A reference implementation with a command line interface (CLI) is provided with a `MockMovieService`.

The Mock movie name are :

            Movie U
            Movie PG
            Movie VM_12":
            Movie VM_15":
            Movie VM_18":

For production and system testing of course testing in needed with an actual MovieService.

### Running the Command Line Client
The CLI can be started by typing at terminal:
 
    java -jar target/parental.control-0.1.0.jar
    
or you can run : run.sh as long as you have execution rights

	chmod a+x run.sh
	./run.sh
   
### unit test code coverage

Coverage is execute by Cobertura and it cover 100% of the sample (except the mock movie service and main.)


### Maven site

Execute the :

    mvn site
    
The resulting web site is under target/site/index.html
 
 
   
Can it go to production: of course not as long it has not been gone through appropriate dev-qa.
 
    
