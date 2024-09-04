queries = {
    "hum": "SELECT * FROM measurements "
           "WHERE sensor_id = 4 "
           "AND timestamp >= '2024-08-25T00:00:00' "
           "ORDER BY timestamp",

    "temp": "SELECT * FROM measurements "
            "WHERE sensor_id = 3 "
            "AND timestamp >= '2024-08-25T00:00:00' "
            "ORDER BY timestamp"
}