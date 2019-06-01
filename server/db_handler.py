import sqlite3


class DatabaseHandler:

    def __init__(self, database = None):
        self.__connection = None
        self.__cursor = None
        if database:
            self.open(database)

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.close()

    def open(self, database):
        try:
            self.__connection = sqlite3.connect(database)
            self.__cursor = self.__connection.cursor()

        except sqlite3.Error as error:
            print("Error connecting to database!")

    def close(self):
        if self.__connection:
            self.__connection.commit()
            self.__cursor.close()
            self.__connection.close()

    def query(self, sql, values):
        self.__cursor.execute(sql, values)
        self.__connection.commit()

    def add_finger_print(self, hash, time, song):
        self.query("""INSERT OR IGNORE INTO fingerprints VALUES(?, ?, ?)""",
                   (hash, time, song))

    def get_all_data(self):
        return [row for row in self.__cursor.execute("""SELECT * FROM fingerprints""")]
