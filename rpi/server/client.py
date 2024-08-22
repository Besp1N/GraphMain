import socket
import ssl
import sys

if len(sys.argv) != 3:
    raise ValueError(f"Expected exactly two arguments from stdin, but got {sys.argv[1::]}")

result = '\n'.join(sys.argv[1::])
    
    
    
    # Join the arguments with a newline
    
# Create a regular TCP/IP socket
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Create an SSL context and load the server's self-signed certificate
context = ssl.create_default_context(ssl.Purpose.SERVER_AUTH)
context.check_hostname = False  # Disable hostname checking (optional)
context.verify_mode = ssl.CERT_REQUIRED  # Require certificate
context.load_verify_locations('server.crt')  # Load the server's self-signed certificate

# Wrap the socket with SSL
ssl_socket = context.wrap_socket(client_socket, server_hostname='localhost')

# Connect to the server
ssl_socket.connect(('localhost', 12345))

# Send data to the server
ssl_socket.send(result.encode('utf-8'))

# Receive a response from the server
data = ssl_socket.recv(1024).decode('utf-8')
print(f"Received: {data}")

# Close the connection
ssl_socket.close()

