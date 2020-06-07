# Protocol

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

```

---

!client:

* host
* uuid
* name

expects success after each 

---



## aux functions

---

### !info:

!success: name:"billly bobs bill house" owner:"noreply@microsoft.com"

