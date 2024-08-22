import socket
import ssl
import sqlite3
import sys

DB_NAME = "../sensors.db"
PASSWORD = sys.argv[1]
# Create a regular TCP/IP socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(('localhost', 12345))
server_socket.listen(5)

# Wrap the socket with SSL
context = ssl.create_default_context(ssl.Purpose.CLIENT_AUTH)
context.load_cert_chain(certfile="server.crt", keyfile="server.key")

ssl_socket = context.wrap_socket(server_socket, server_side=True)
print("Server listening on port 12345...")

def execute_query(query):
    """Connect to SQLite database and execute the query."""
    conn = sqlite3.connect(DB_NAME)
    cursor = conn.cursor()
    try:
        cursor.execute(query)
        if query.strip().lower().startswith("select"):
            rows = cursor.fetchall()
            response = "\n".join(str(row) for row in rows)
        else:
            conn.commit()
            response = "Query executed successfully."
    except sqlite3.Error as e:
        response = f"SQLite error: {e}"
    finally:
        conn.close()
    return response

while True:
    # Accept a client connection
    client_socket, client_address = ssl_socket.accept()
    print(f"Connection from {client_address} has been established!")

    try:
        # Receive data from the client
        data = client_socket.recv(1024).decode('utf-8')
        
        # Process received data
        data = data.split('\n')
        if len(data) != 2:
            client_socket.send("Invalid format: expected password<newline>query".encode('utf-8'))
            continue
        
        password = data[0]
        if password != PASSWORD:
            raise Exception("Wrong password idiot")
        query = data[1]

        # Here you would typically check the password, for simplicity we'll skip that
        # Execute the query
        response = execute_query(query)
    except Exception as e:
        response = f"Error: {e}"

    # Send response to the client
    client_socket.send(response.encode('utf-8'))

    # Close the client socket
    client_socket.close()

# Close the server socket
ssl_socket.close()

