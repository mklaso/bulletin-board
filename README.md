# bulletin-board
- Mulithreaded client server application, that uses TCP/IP to communicate over sockets, and uses a custom protocol supporting CRUD like operations to manipulate data stored on a central bulletin board system
- Uses 3-way handshake to establish secure connection for communication between clients and server
- Once users (clients) have connected to the server, they can create notes and add them to the bulletin board
- While the server is running, all clients are able to see the notes/data added to the bulletin board, regardless if the original note creator has disconnected from the server (data persists)

## client 
- requests a connection to the server through a combination of port numbers + IP address
- can send commands to the server (e.g: POST, GET, PIN, etc.), similar idea as CRUD operations
- uses a GUI to allow users to create notes that can be added to a bulletin board like system held on the server
- notes can have a height, width, colour, can be pinned to the board, hold a message, amongst other things

## server
- upon running the server, a number of arguments are given to create the bulletin board (height, width, supported colours, etc.), as well as a port number for the server to run on
- can accept any number of client connections should they wish to connect
- maintains the data created and received from the clients on a bulletin board type system
- can process supported requests/messages from the client(s)
- ensures valid data is received from the client by enforcing a custom protocol; rejects and displays error messages if client request could not be satisfied
