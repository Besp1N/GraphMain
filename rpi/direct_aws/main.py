import time
import psycopg2
import psycopg2.extras
import aws_config
import config  

def insert_sensor_data(sensor, cursor):
    """Function to insert data for a specific sensor."""
    sensor_id = sensor['sensor_id']
    read_sensor = sensor['reader']

    timestamp = time.time()
    value = read_sensor()
    print(f"Sensor {sensor_id}: Read value {value}")

    if value is None:
        raise Exception(f"{sensor_id}: value is none: {timestamp}")
    
    sql = 'INSERT INTO measurements (timestamp, value, sensor_id) VALUES (to_timestamp(%s), %s, %s)'
    params = (timestamp, value, sensor_id)
    
    try:
        cursor.execute(sql, params)
        print(f"Sensor {sensor_id}: INSERTED DATA - {value}C at {timestamp}")
    except Exception as error:
        print(f"Sensor {sensor_id}: Error during insert: {error}")
        cursor.connection.rollback()


def main():
    interval = config.CONFIG['interval']  # Shared interval for all sensors
    conn = None
    try:
        conn = psycopg2.connect(**aws_config.DB_PARAMS)
        
        cursor = conn.cursor(cursor_factory=psycopg2.extras.NamedTupleCursor)
        
        while True:
            for sensor in config.CONFIG['sensors']:
                try:
                    insert_sensor_data(sensor, cursor)
                    conn.commit()
                except Exception as e:
                    print(f"Error processing sensor {sensor['sensor_id']}: {e}")
            
            time.sleep(interval)
    
    except (Exception, psycopg2.DatabaseError) as error:
        print(f"Database connection error: {error}")
    
    finally:
        if cursor is not None:
            cursor.close()
        if conn is not None:
            conn.close()


if __name__ == "__main__":
    main()

