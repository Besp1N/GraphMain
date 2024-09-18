#include "anomaly.hh"
#include "data_reader.hh"
#include "writer.hh"
#include <chrono>
#include <iostream>
#include <termio.h>
#include <thread>

const SensorData BASELINE_SENSOR_DATA{23.0f, 50.0f};
const SensorData MAX_SENSOR_DATA{29.0f, 43.0f};

std::atomic<bool> anomaly_requested(false);

void set_non_blocking_input() {
  termios oldt, newt;
  tcgetattr(STDIN_FILENO, &oldt); // Save the current settings
  newt = oldt;
  newt.c_lflag &= ~(ICANON | ECHO); // Disable canonical mode and echo
  tcsetattr(STDIN_FILENO, TCSANOW, &newt);
}

void restore_input_settings() {
  termios oldt;
  tcgetattr(STDIN_FILENO, &oldt);
  oldt.c_lflag |= (ICANON | ECHO); // Restore settings
  tcsetattr(STDIN_FILENO, TCSANOW, &oldt);
}

void detect_input() {
  set_non_blocking_input();
  while (true) {
    char ch;
    if (read(STDIN_FILENO, &ch, 1) > 0) {
      if (ch == 'a' || ch == 'A') {
        anomaly_requested.store(true);
      }
      if (ch == 's' || ch == 'S') {
        anomaly_requested.store(false);
      }
    }
    std::this_thread::sleep_for(std::chrono::milliseconds(100));
  }
  restore_input_settings();
}

int main() {
  DatabaseMeasurementWriter m_writer;
  DatabaseNotificatonWriter n_writer;
  DataReader<SensorData> reader(MAX_SENSOR_DATA, BASELINE_SENSOR_DATA, 2, 30);
  std::thread input_thread(detect_input);
  while (true) {
    if (anomaly_requested.load() == true) {
      reader.start_anomaly();
    }
    SensorData data = reader.next();
    std::cout << "Temperature: " << data.temperature
              << "C, Humidity: " << data.humidity << "%" << std::endl;

    m_writer.write(data);

    if (detect_anomaly(data, reader.history)) {
      n_writer.write(Anomaly{KETTLE_DEVICE_ID, "Test Anomaly",
                             AnomalyType::Warning, data.timestamp_sec});
    }

    std::this_thread::sleep_for(std::chrono::seconds(2));
  }
  input_thread
      .detach(); // Detach the input thread since we don't need to join it
  return 0;
}
