import adafruit_dht
import board

# Set the sensor and GPIO pin
dhtDevice = adafruit_dht.DHT11(board.D27)  # Replace D4 with the GPIO pin you are using

def read_humidity():
    try:
        humidity = dhtDevice.humidity
        if humidity is not None:
            return humidity
        else:
            return None
    except RuntimeError as error:
        print(f"RuntimeError: {error.args[0]}")
        return None

if __name__ == "__main__":
    read_humidity()

