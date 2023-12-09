# socket
a client (C) sends a file name (`test.txt`) to a server (S) using Socket programming, and the server then checks for the file and responds accordingly

### Server (S) :

1. **Initialize Socket**: Set up a TCP socket on the server.

2. **Bind and Listen**: Bind the socket to a specific port on the host and listen for incoming connections.

3. **Accept Connection**: Accept a connection from the client.

4. **Receive File Name**: Read the file name sent by the client (e.g., `test.txt`).

5. **Check File Existence**: Check if `test.txt` exists in the current directory.
   - If it exists, open the file.
   - Read the file contents into a buffer.

6. **Send Response**: 
   - If the file exists, send the file contents to the client.
   - If the file does not exist, send an error message indicating the file is not found.

7. **Close Connection**: After sending the file or error message, close the connection.

8. **Clean Up**: Close the socket and perform any necessary cleanup.

### Client (C) :

1. **Initialize Socket**: Set up a TCP socket on the client.

2. **Connect to Server**: Connect the client socket to the server's IP and port.

3. **Send File Name**: Send the file name (e.g., `test.txt`) to the server.

4. **Receive Response**: 
   - If the server sends file contents, save them to a file or display them.
   - If the server sends an error message, display the error to the user.

5. **Close Connection**: Close the socket connection.

6. **Clean Up**: Perform any necessary cleanup operations.

### Considerations:

- **Error Handling**: Both the client and server should include error handling for situations such as connection failures, read/write errors, and unexpected disconnections.
- **Security**: Be aware of security risks, such as buffer overflow vulnerabilities, and ensure safe handling of file paths and data transmission.
- **File Size Handling**: For large files, you may need to implement a loop to send and receive the file in chunks.
- **Platform Differences**: If the server and client are running on different platforms, consider the differences in file path formats and newline characters.
- **Concurrency**: If the server needs to handle multiple clients simultaneously, consider using multi-threading or select/poll mechanisms.
