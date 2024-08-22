from temp import read_temp
from humidity_reader import read_humidity
from hc import read_distance
CONFIG = {
    "table_name": "kettle",
    "interval": 60,
    "sensors": [ {
        "name": "temperature",
        "sensor_id": 3,
        "reader": read_temp
    },
    {
    "name": "humidity",
    "sensor_id": 4,
    "reader": read_humidity,
    },           
    {
    "name": "distance",
    "sensor_id": 5,
    "reader": read_distance,
    }
    ]
    
}

