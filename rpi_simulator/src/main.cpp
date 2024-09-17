#include "data_reader.hh"
#include "writer.hh"
#include <chrono>
#include <iostream>
#include <thread>

const SensorData BASELINE_SENSOR_DATA{25.0f, 50.0f};
const SensorData MAX_SENSOR_DATA{29.0f, 43.0f};

int main() {
  DatabaseMeasurementWriter writer;
  DataReader<SensorData> reader(MAX_SENSOR_DATA, BASELINE_SENSOR_DATA, 1, 5);

  for (int i = 0; i < 10; ++i) {
    SensorData data = reader.next();
    std::cout << "Temperature: " << data.temperature
              << "C, Humidity: " << data.humidity << "%" << std::endl;

    if (i == 5) {
      reader.start_anomaly();
    }
    writer.write(data);
    std::this_thread::sleep_for(std::chrono::seconds(1));
  }

  return 0;
}
