Multi-Threading FTP Server

Introduction:

Most network applications rely on file transfer protocols in one form or another. For instance, the HTTP protocol used on the Web is a generic file transfer protocol. In this project, you will implement an FTP client program and an FTP server program for a simple file transfer. The server could handle one or more file transfers to the client(s) at any given time. The project implements an active FTP app, as described in the project assignment. However, you may choose to implement FTP running in passive mode, which is okay too. The implemented FTP application is assumed to support text file transfer.

Functionality:

The client program presents a command line interface that allows a user to:

Connect to a server
List files located on the server
Get (retrieve) a file from the server
Put (store) a file from the client to the server
Terminate the connection to the server
The server program binds to a port and listens for requests from a client. After a client connects to the server, the server waits for commands. When the client sends a terminate message (quit), the server terminates the connection and waits for the next connection.

Implementation:

You can implement your project using any programming language as two independent programs: an FTP client called ftp_client and an FTP server called ftp_server. The ftp_client program presents a command line interface. The client and server communication needs to be done using TCP sockets.

Commands:

CONNECT <server name/IP address> <server port>: Allows a client to connect to a server.
LIST: Returns a list of the files in the current directory on the server.
RETR <filename>: Allows a client to get a file specified by its filename from the server.
STOR <filename>: Allows a client to send a file specified by its filename to the server.
QUIT: Allows a client to terminate the control connection.
Client/Server Interaction:

To implement the communication between the client and the server, you need to use 2 TCP connections (control and data connections) at both the client and the server.

FTP Server Implementation Details:

The FTPClient first establishes a control connection by parsing the connect command.
The LIST command retrieves the list of files in the directory and sends it over the data connection.
RETR command retrieves a file from the server.
STOR command stores a file on the server.
The CLOSE command terminates the connection between the client and the server.

Project Grading Policy:

Not implementing a multithreaded server leads to a deduction of 20 points.
Not implementing any FTP function correctly leads to a deduction of 20 points.
Late submissions receive a 20% penalty per day, up to a total of 5 days.

Usage:

Compile the server code: gcc -o ftp_server ftp_server.c -lpthread
Compile the client code: gcc -o ftp_client ftp_client.c
Run the server: ./ftp_server
Run the client: ./ftp_client

Contributors:

Hunter Sutton
Jack Wolak
Andrew Slayton
Matteo Ciavaglia.
