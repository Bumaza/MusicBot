from MusicBot.db_handler import DatabaseHandler
from flask import Flask, request, jsonify

app = Flask(__name__)


# root
@app.route("/")
def index():
    return "This is root!!!!"


# GET
@app.route('/users/<user>')
def hello_user(user):
    return "Hello %s!" % user


# GET
@app.route('/database')
def get_database_data():
    handler = DatabaseHandler('fingerprint.db')
    return '\n'.join("{0} {1} {2}\n".format(str(row[0]), str(row[1]), str(row[2])) for row in handler.get_all_data())

# POST
@app.route('/api/identify/song', methods=['POST'])
def identify_song():
    json = request.get_json()
    print(json)
    if len(json['data']) == 0:
        return jsonify({'error': 'invalid input'})

    return jsonify({'status': "OK"})


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)