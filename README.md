# chatAppJava

### Objectives : Develop a Client-Server Chat application, in UDP and TCP

This project has a package where you could find the different classes for
TCP and UDP servers and clients. The Main program read from the command line
the port to listen and flags to execute the server or client on localhost,
and has flags to indicate if a server or client, and the protocol to use for
the connection. If no argument is given or -h is passed as a flag, print the help

The main classes in the package are:
* UDPServer: udp server
* UDPClient: udp client
* TCPServer: tcp echo server
* TCPClient: tcp client
* TCPMultiserver: tcp server that echoes to all other client the message sent by
  a client

All those classes implement a launch method that runs the respective loop to send
and/or receive data.

Each class has a main method with their own arguments to pass to them. 

### Minimal examples

To establish connection in UDP:
* Launch UDPServer.java
* To Launch a Client type in terminal $java UDPClient.java 'server address' 'port'

To establish connection in TCP:
* Launch TCPServer.java
* To Launch a Client type in terminal $java TCPClient.java 'server address' 'port'

