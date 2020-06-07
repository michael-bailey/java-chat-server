# Java Test Server

this is a test server for the chat client that will allow easy unit testing of the java chat client

this will documet the internal workings of the server.

this does have attached a ui that should allow viewing of server state but that is not a priority.

---

## Dependencies

* Java 13

* JavaFx - used for the User interface (installed via gradle)

  ---

  ## Execution

  to run this program use the grade wrapper

  ### linux

  ```sh
  # to build the project.
  ./gradlew build
  
  # to test the build
  ./gradlew test
  
  # to run via gradle
  ./gradlew run
  
  # to build a full exectuable jar version
  ./gradlew shadowjar
  ```

  ### windows

  ```bat
  # to build the project.
  gradlew.bat build
  
  # to test the build
  gradlew.bat test
  
  # to run via gradle
  gradlew.bat run
  
  # to build a full exectuable jar version
  gradlew.bat shadowjar
  ```

  ---

  ## Documentation Pages

  * [Protocol](https://michael-bailey.github.io/java-chat-server/protocol)
  * [User Interface](https://michael-bailey.github.io/java-chat-server/interface)
  * [Architecture](https://michael-bailey.github.io/java-chat-server/architecture)

  **this Documentation is work in progress (feel free to critisise constructively)**

  #Owners
  [michael-bailey](https://github.com/michael-bailey/)
  [mitch161](https://github.com/mitch161/)

