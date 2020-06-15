# Protocol

This protocol is a text based protocol sent over a tcp connection 



## Structure

---

All messages follow a simple structure of: a Command followed by, if applicable; multiple key-value parameters.

this can be shown using a regular expression ot seperate a command into its components.

the only thing that has to be set is the position of the command as it shoud be the first element in the string.

```java
String regex = "([?!][a-zA-z0-9]*:)|([a-zA-z]*:([a-zA-Z0-9\-+\[\]{}_=]+|\".*?\")+)";
```

[A Regex playgound link for this pattern](https://regex101.com/r/KPMh26/1)

This does not define the keywords used for the protocol. These are explained below.

---



## Connection Commands.

---

### !connect:

this signals the server to create a new client that will be connected and other 

### Parameters

* uuid - this is the uuid of the user that is connecting.
* name - the username of the connecting user.
* host - the ip address of the connecting user *(possibly not needed)*

### Example

```
Server: ?request:
Client: !connect: uuid:123456-1234-1234-123456 name:"alice" host:"127.0.0.1"
Server: !success:
```

---

### !disconnect:

This signals th eserver to disconnect the server.

### Parameters

There are no parameters.

### Example

```
Client: !disconnect:
Server: !success:
```

---



## Client Commands.

These are the commands that are sent between the client and the server to coordinate info retrieval and connections.

---

### !client:

This is sent from the server to the client to say someone has connected.

#### Parameters

* name - the clients username
* host - the clients hostname
* uuid - the clients unique identifier

#### Examples

##### Client > Server

```
Server: !client: name:"alice" host:"255.255.255.255" uuid:"123456-1234-1234-123456"
Client: !success:
```

---

### !clientInfo:

This is sent by the client to get the info relating to another client.

#### Parameters.

* uuid - The uuid of the user to get

#### Examples

```
Client: !clientInfo: uuid:"654321-1234-1234-654321"
Server: !success: uuid:"654321-1234-1234-654321" name:bob host:"127.0.0.1"
```

---

### !clientRemove:

This is used by the server to signal a client has disconnected

#### Parameters.

* uuid- the uuid of the user

### Result.

* expects a success with no params

```
Server: !client uuid:654321-1234-1234-654321
Client: !success:
```

---

### !clientupdate:

this is sent from the client to the server to Trigger the server to send each client as if they ave just connected.

#### Parameters.

there are no parameters.

#### Result.

expects a success with no parameters.

#### example

```
Client: !clientupdate:
Server: !Success:
// the server will then send a !client: for each connection
```





---



## Return Messages.

These are sent to signal the end of an exchange.

---

### !success:

This is sent to confirm a message has been received pass data back to the sender as a form of return value or denotes that a message exchange has finished correctly

#### Parameters.

Paramters for this depends on the current exchange many of which don't require a return value.

#### Examples.

```
# Client sending
Server: !client: <params>
Client: !success:

# getting server Info.
New-Connection: !info:
Server: !success: name:exampleServer owner:"noreply@email.com"

```

---

### !error:

This is returned when an error occurs in the connection for example; an unknown command or broken connection.

#### params

theree are no parameters with this as many occurances of this mean the connection will be terminated *(possibly think about an error count)*

#### Examples.

```
# getting info
New-Connection: !data:
Server: !error:

# During receiving clients
Server: !client: nane:"alice" host:"255.255.Aa45.255" uuId:"123456-1234-1234-123456"
Client: !error:
```

---



// todo Finish the rest.

## aux functions

---

### !info:

this is sent to get the infomation about the server so the client can connect.

### Parameter

there are no parameters.

### Example

``` 
New connection: ?info:
Server: !success: name:"billly bobs bill house" owner:"noreply@microsoft.com"
```

---

[Home Page](https://michael-bailey.github.io/java-chat-server)