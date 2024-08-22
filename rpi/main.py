import time
import sqlite3
import config  # Assuming config contains DB_PARAMS and CONFIG

def init_db():
    conn = sqlite3.connect('sensors.db')
    cursor = conn.cursor()

    # Create a table based on the config
    table_name = config.CONFIG["table_name"]
    columns = ', '.join([f"{sensor['name']} REAL" for sensor in config.CONFIG["sensors"]])

    # SQL command to create the table
    create_table_query = f"""
    CREATE TABLE IF NOT EXISTS {table_name} (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
        {columns}
    );
    """

    cursor.execute(create_table_query)
    conn.commit()

def insert_sensor_data(cursor):
    """Function to insert data for all sensors at a specific timestamp."""

    # Get the current timestamp
    timestamp = time.time()

    # Collect sensor readings
    sensor_readings = {}
    for sensor in config.CONFIG["sensors"]:
        sensor_name = sensor["name"] # Get the function name (assuming it's unique per sensor)
        sensor_readings[sensor_name] = sensor["reader"]()  # Read the sensor value

    print(f"Values: {sensor_readings}")

    # Construct the SQL query for insertion
    columns = ', '.join(sensor_readings.keys())
    placeholders = ', '.join('?' for _ in sensor_readings)
    values = tuple(sensor_readings.values())

    sql = f'INSERT INTO {config.CONFIG["table_name"]} (timestamp, {columns}) VALUES (?, {placeholders})'

    try:
        cursor.execute(sql, (timestamp, *values))
        print(f"Inserted data at timestamp {timestamp}: {values}")


    except Exception as error:
        print(f"Error during insert: {error}")
        cursor.connection.rollback()


def start_read_loop():
    init_db()
    interval = config.CONFIG['interval']  # Shared interval for all sensors
    conn = None
    try:
        # Connect to SQLite database (local file-based)
        conn = sqlite3.connect('sensors.db')
        cursor = conn.cursor()
        
        while True:
            try:
                insert_sensor_data(cursor)
                conn.commit()  # Commit after each successful insert
            except Exception as e:
                print(e)
                pass
            
            time.sleep(interval)
    
    except sqlite3.DatabaseError as error:
        print(f"Database connection error: {error}")
    
    finally:
        if cursor is not None:
            cursor.close()
        if conn is not None:
            conn.close()

if __name__ == "__main__":
    start_read_loop()
