import RPi.GPIO as GPIO
import time

# Definiowanie numerów pinów
TRIG = 23  # Przykładowy numer pinu TRIG
ECHO = 24  # Przykładowy numer pinu ECHO

def read_distance():
    # Inicjalizacja GPIO
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(TRIG, GPIO.OUT)
    GPIO.setup(ECHO, GPIO.IN)
    
    # Upewnij się, że TRIG jest niski na początku
    GPIO.output(TRIG, False)
    time.sleep(2)
    
    # Wysłanie krótkiego sygnału o wysokim stanie
    GPIO.output(TRIG, True)
    time.sleep(0.00001)
    GPIO.output(TRIG, False)

    # Czekanie na powrót sygnału z limitem czasu
    timeout = time.time() + 1  # 1 sekunda timeoutu
    while GPIO.input(ECHO) == 0:
        puls_start = time.time()
        if time.time() > timeout:
            return "Timeout waiting for pulse start"

    timeout = time.time() + 1  # 1 sekunda timeoutu
    while GPIO.input(ECHO) == 1:
        puls_end = time.time()
        if time.time() > timeout:
            return "Timeout waiting for pulse end"

    # Obliczenie czasu trwania sygnału
    puls_duration = puls_end - puls_start

    # Przeliczenie czasu na odległość
    distance = puls_duration * 17150
    distance = round(distance, 2)

    return distance

if __name__ == "__main__":
    try:
        print(read_distance())
    finally:
        GPIO.cleanup()  # Czyszczenie ustawień GPIO po zakończeniu programu

