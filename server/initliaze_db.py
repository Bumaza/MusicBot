from os import remove, path
import sqlite3

if path.exists('fingerprint.db'):
    remove('fingerprint.db')
connection = sqlite3.connect('fingerprint.db')
cursor = connection.cursor()
cursor.execute('''CREATE TABLE fingerprints (integer hashtag primary key,
                                        time float, 
                                        song text)''')
connection.commit()
connection.close()