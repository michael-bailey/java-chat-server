# Protocol

[Home](https://michael-bailey.github.io/java-chat-server)

---

This protocol was created to easily debug connections.

---

## Connections

---

### !connect:

this accepts any order

|params|

uuid

username

address

---

!disconnect:

No params

---

## Return Messages

---

!success:

---

!error:

---



## <u>Commands</u>

These are the commands that are sent between the client and the server to coordinate info retrieval and connections.

---

### !clientUpdate:

This is sent by the client to get the list of other clients connected.

This is also sent by the server each time a connection is created or destroyed.

#### Parameters

The command itself has no parameters but other parts of the whole exchange do. (see !client:).

#### Examples

##### Client > Server

```
Client: !clientUpdate:
Server: !client name:"alice" host:"255.255.255.255" uuid:"123456-1234-1234-123456"
Client: !success:
Server: !client name:"bob" host:"234.234.234.255" uuid:"654321-1234-1234-654321"
Client: !success:
Server: !success:
Client: !success:
```

##### Server > Client

```
Server: !clientUpdate:
Client: !Success:
Server: !client name:"alice" host:"255.255.255.255" uuid:"123456-1234-1234-123456"
Client: !success:
Server: !client name:"bob" host:"234.234.234.255" uuid:"654321-1234-1234-654321"
Client: !success:
Server: !success:
Client: !Success:
```

---

### !clientInfo:

This is sent by the client to get the info relating to another client.

#### Parameters.

* uuid - The uuid of the user to get

#### Examples

```
Client: !clientInfo: uuid:654321-1234-1234-654321
Server: !success: uuid:654321-1234-1234-654321 name:bob host:"127.0.0.1"
```

---

### !client:

This is used by the server to signal the attached info is part of a client. it will expect a success message formt he client

#### Parameters.

* host - this is the hostname/ip of the client
* name - the users name for the client
* uuid- the uuid of the user

expects success after each client sent.

```
Server: !client uuid:654321-1234-1234-654321 name:bob host:"127.0.0.1"
Client: !success:
```

---

// todo Finish the rest.

## aux functions

---

### !info:

!success: name:"billly bobs bill house" owner:"noreply@microsoft.com"