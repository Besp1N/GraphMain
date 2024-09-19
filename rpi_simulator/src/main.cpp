#include "anomaly.hh"
#include "data_reader.hh"
#include "writer.hh"
#include <chrono>
#include <iostream>
#include <ostream>
#include <python3.12/Python.h>
#include <python3.12/pylifecycle.h>
#include <termio.h>
#include <thread>

const SensorData BASELINE_SENSOR_DATA{23.0f, 50.0f};
const SensorData MAX_SENSOR_DATA{29.0f, 43.0f};
const int TIME_PER_READ = 5;

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
  Py_Initialize();
  auto input_thread = std::thread(detect_input);
  DatabaseMeasurementWriter m_writer;
  DatabaseNotificatonWriter n_writer;
  DataReader<SensorData> reader(MAX_SENSOR_DATA, BASELINE_SENSOR_DATA,
                                TIME_PER_READ, 30);
  // termios thread

  while (true) {
    if (anomaly_requested.load() == true) {
      reader.start_anomaly();
    }
    SensorData data = reader.next();
    std::cout << "Temperature: " << data.temperature
              << "C, Humidity: " << data.humidity << "%" << std::endl;

    m_writer.write(data);
    auto anomaly = detect_anomaly(reader.history);

    if (anomaly.has_value()) {
      n_writer.write(Anomaly{KETTLE_DEVICE_ID, "Test Anomaly", anomaly.value(),
                             data.timestamp_sec});
      std::cout << anomaly_type_to_string(anomaly.value()) << std::endl;
    }

    std::this_thread::sleep_for(std::chrono::seconds(TIME_PER_READ));
  }
  Py_Finalize();
  input_thread.detach();
  return 0;
}
