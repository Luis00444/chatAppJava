# chatAppJava

### Objectives : Develop a Client-Server Chat application, in UDP and TCP

This project has a package where you could find the different classes for
TCP and UDP servers and clients. The Main program reads from the command line
the port to listen and flags to execute the server or client,
and has flags to indicate the address for the client, and the protocol to use for
the connection. If no argument is given or -h is passed as a flag, it prints the help.

To establish connection in UDP:
* Launch UDPServer.java
* To Launch a Client type in terminal $java UDPClient.java 'server address' 'port'

To establish connection in TCP:
* Launch TCPServer.java or TCPMultiserver.java
* To Launch a Client type in terminal $java TCPClient.java 'server address' 'port'


