import os
from MusicBot.db_handler import DatabaseHandler
from MusicBot.shazam import FingerPrint


if __name__ == '__main__':
    handler = DatabaseHandler('fingerprint.db')
    files = [f for f in os.listdir('./data/') if 'wav' in f]

    for file in files:
        fp = FingerPrint('./data/'+file, handler)
        fp.create_fingerprint()
