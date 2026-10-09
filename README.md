# Java Multi-Threaded Dictionary Application

A lightweight, multi-threaded client-server dictionary system built in Java. 
This application allows clients to connect over a network to search, add, update, and delete word definitions in real time, 
while providing a desktop interface (GUI) on the server to monitor all data.

This code is based on the tutor given in the LMS.
How to run the code.

Step1:
Since I already make a file in the code to save the data.
So Server:
> java –jar dictionary-server-1.0-jar-with-dependencies.jar <port>

Step2:
Since I made the server address is server localhost, user only need to input port.
Client:
> java –jar dictionary-client-1.0-jar-with-dependencies.jar localhost <server-port> or
> java –jar dictionary-client-1.0-jar-with-dependencies.jar <server-port>
