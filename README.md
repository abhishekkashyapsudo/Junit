# Junit Repository   
  
To run the application: Right click on EBrokerApplication and run it as a spring boot application. 

  => Application will be started on port **8082**.  
  => The application requires a mysql database with username and password as **root** and database name as **ebroker**.  
  => The required tables will be created automatically.  
  => Use **initial_data.sql** under **src/main/resources** to add initial data into database.  
  
  
#Endpoints


  => **/equity/available**: List all the avilable equities.  
  => **/equity/trader**: Returns the trader details along with the avilable equities for him.   
  => **equity/buy?id=4&quantity=10**: Buy the passed quantity of equity with passed id. It returns the updated trader details.   
  => **equity/sell?id=4&quantity=1**: Sells the passed quantity of equity with passed id. It returns the updated trader details.    
  
  => **equity/addFunds?amount=10000**: Adds the passed amount to trader's funds. It returns the updated trader details.    
  
  
#Validations: A user friendly message is retruned in the following cases.

  => **/equity/available**:   
    => No validations.  
    
  => **/equity/trader**:   
    => Returns an error message if trader is not present in the database.  
     
  => **equity/buy?id=4&quantity=10**:   
    => Time must be between 10-3 on a weekday(Except Saturday and Sunday).  
    => Trader with id 1 must exist.    
    => Equity with passed id must exist.  
    => Quantity should be greater than 0.  
    => Trader must have sufficient funds.  
    
  => **equity/sell?id=4&quantity=1**:   
    => Time must be between 10-3 on a weekday(Except Saturday and Sunday).  
    => Trader with id 1 must exist.  
    => Equity with passed id must exist.  
    => Quantity should be greater than 0.  
    => Equity should be available with the trader.  
    => Sufficient amount of the equity should be present.  
     
  => **equity/addFunds?amount=10000**:   
     => Amount should be greater than 0.  

# Test Coverage

Right now there are two types of tests that are written. 
  => **Unit tests** : 99% code Coverage   
  => **Integration tests** : 98% code Coverage   
    => Simply use maven install and the test_reports will be generated under **target/site**.   
      => **jacoco-unit-test-coverage-report** will have the report for unit tests.  
      => **jacoco-integration-test-coverage-report** will have the report for integartion tests.  
  
