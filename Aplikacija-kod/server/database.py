import hashlib
import uuid
import mysql.connector as mysql


conn = mysql.connect(
        user='blagoje',
        password='takovo123_O',
        host='127.0.0.1',
        database='users.db'
    )


def connect():
    # # Connect to the database
    # conn = mysql.connector.connect(
    #     host='127.0.0.1',
    #     user='blagoje',
    #     password='takovo123_O',
    #     database='users.db',
    #     auth_plugin='mysql_native_password'
    # )
    cursor = conn.cursor()
    

    # Create the users table if it does not exist
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS users (
            id varchar(50) PRIMARY KEY,
            username text(20) NOT NULL,
            password text(50) NOT NULL
        )
    ''')

    return conn, cursor

def close(conn):
    # Close the connection
    conn.close()

def register_user(username, password):
    cursor = conn.cursor()
    # Hash the password
    hashed_password = hashlib.sha256(password.encode()).hexdigest()

    # Generate a unique identifier for the user
    user_id = str(uuid.uuid4())

    # Check if the username is already in use
    cursor.execute('''SELECT * FROM users WHERE username=%s''', (username,))
    if cursor.fetchone() is not None:
        return {'error': 'Username already in use'}, 400

    # If the username is available, add it to the user store and return a JWT
    cursor.execute('''INSERT INTO users (id, username, password) VALUES (%s, %s, %s)''', (user_id, username, hashed_password))
    conn.commit()
    return user_id

def verify_user(username, password):
    cursor = conn.cursor()
    # Hash the password
    hashed_password = hashlib.sha256(password.encode()).hexdigest()

    # Check if the username and password are correct
    cursor.execute('''SELECT * FROM users WHERE username=%s AND password=%s''', (username, hashed_password))
    user = cursor.fetchone()
    if user is not None:
        return user[0]
    else:
        return None