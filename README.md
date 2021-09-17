# cp372-a1 

basic idea:
- assignment consists of a document + code
- need to setup a client and a server that can communicate with each other

## client 
- requests a connection to the server
- can send commands to the server (e.g: POST, GET, PIN, etc.)

## server
- can accept multiple connections (multithreaded server)
- maintains the data received from the clients (held in some sort of data structure --> probably hashmap is best suited here)
- can process requests/messages from the client(s)
- needs to be able to handle errors (e.g: commands that don't exist, improper format of commands, etc.)

## other stuff to keep in mind
- link to a1 document/instructions: https://mylearningspace.wlu.ca/d2l/le/content/399850/viewContent/2657282/View
- need to make a GUI for the client (can be pretty simple, like a couple textboxes with associated buttons to send various requests/and other stuff listed in the instructions - not that hard, just need to use some GUI library and setup controllers)
- looks like he wants the document setup in the form of an RFC like the examples he posted

## duedates
- documentation due by **10:30pm EST September 29th**
- code due by **10:30pm EST October 6th**
