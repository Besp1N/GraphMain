import datetime
import sqlite3
from config import CONFIG_MOCK as CONFIG
def clean_db():
    conn = sqlite3.connect('sensors.db')
    cursor = conn.cursor()

    # Example: Delete records older than a week
    one_week_ago = datetime.datetime.now() - datetime.timedelta(weeks=1)
    print(one_week_ago)
    cursor.execute(f"DELETE FROM {CONFIG['table_name']} WHERE timestamp < ?", (one_week_ago,))
    
    conn.commit()
    cursor.close()
    conn.close()
    print("Database cleaned.")

if __name__ == "__main__":
    clean_db()