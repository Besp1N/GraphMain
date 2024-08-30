import time
from w1thermsensor import W1ThermSensor, SensorNotReadyError

sensor = W1ThermSensor()
max_retries = 5
retry_delay = 1  # seconds
def read_temp():
    for attempt in range(max_retries):
        try:
            temperature_celsius = sensor.get_temperature()
            break;
        except SensorNotReadyError:
            time.sleep(retry_delay)
    return temperature_celsius
if __name__ == "__main__":
    read_temp()
