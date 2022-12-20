from flask import Flask, request
#from jwt import JWTManager, create_access_token, get_jwt_identity
import jwt
import database

app = Flask(__name__)

# Configure the JWT manager
secret = '_W4i(INwFLL>TK:]9gLg.Sv%U(NT:6wxZWz3SGA}\ZcpPE4_i]'

# Connect to the database
conn, cursor = database.connect()

# Define a login route
@app.route('/login', methods=['POST'])
def login():
    # Get the username and password from the request
    username = request.json['username']
    password = request.json['password']

    # Verify the user
    user_id = database.verify_user(username, password)
    if user_id is None:
        # If the credentials are incorrect, return an error
        return {'error': 'Invalid username or password'}, 401
    else:
        # If the credentials are correct, create a JWT and return it to the user
        #access_token = encoded(identity=user_id)
        payload = {"user_id":user_id,"username":username}
        encoded_jwt = jwt.encode(payload,secret, algorithm="HS256")

        return {'access_token': encoded_jwt}


# Define a register route
@app.route('/register', methods=['POST'])
def register():
    # Get the username and password from the request
    username = request.json['username']
    password = request.json['password']

    # Register the user
    user_id = database.register_user(username, password)
    if 'error' in user_id:
        return user_id, 400
    else:
        # If the username is available, add it to the user store and return a JWT
        payload = {"user_id":user_id,"username":username}
        encoded_jwt = jwt.encode(payload,secret, algorithm="HS256")
        #access_token = create_access_token(identity=user_id)
        return {'access_token': encoded_jwt}