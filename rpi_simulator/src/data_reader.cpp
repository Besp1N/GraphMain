#include "data_reader.hh"
#include <algorithm>
#include <chrono>
#include <iostream>
#include <random>

SensorData SensorData::operator+(SensorData rhs) {
  return SensorData{temperature = this->temperature + rhs.temperature,
                    humidity = this->humidity + rhs.humidity};
}

////////////////////////////////////////
// Generic, numeric value implementation
////////////////////////////////////////

template <typename T>
DataReader<T>::DataReader(T max, T baseline, int time_per_read,
                          int time_to_peak_anomaly)
    : max(max), baseline(baseline), time_per_read(time_per_read),
      time_to_peak_anomaly(time_to_peak_anomaly), reads_since_anomaly_start(0),
      in_anomaly(false), rng(std::random_device{}()),
      normal_dist(0, 0.5), // Noise generator (mean = 0, stddev = 0.5)
      anomaly_dist(5, 1) { // Anomaly generator (mean = 5, stddev = 1)
  history.reserve(history_max_size);
}

template <typename T> DataReader<T>::~DataReader() {}

template <typename T> void DataReader<T>::add_noise(T &value) {
  value += normal_dist(rng);
  // Ensure the value stays within [baseline, max] range
  value = std::clamp(value, baseline, max);
}

template <typename T> T DataReader<T>::next() {
  T reading = baseline;

  // Check if we're simulating an anomaly
  if (in_anomaly) {
    if (reads_since_anomaly_start < time_to_peak_anomaly) {
      // Apply a spike during anomaly
      reading += anomaly_dist(rng);
      ++reads_since_anomaly_start;
    } else {
      in_anomaly = false; // End the anomaly after reaching peak
      reads_since_anomaly_start = 0;
    }
  }

  // Add some random noise to make the data more realistic
  add_noise(reading);

  // Store in history (remove oldest if history is too large)
  if (history.size() >= history_max_size) {
    history.erase(history.begin());
  }
  history.push_back(reading);

  return reading;
}

template <typename T> void DataReader<T>::start_anomaly() {
  in_anomaly = true;
  reads_since_anomaly_start = 0;
}

///////////////////////////
// SensorData specialization
///////////////////////////

DataReader<SensorData>::DataReader(SensorData max, SensorData baseline,
                                   int time_per_read, int time_to_peak_anomaly)
    : max(max), baseline(baseline), time_per_read(time_per_read),
      time_to_peak_anomaly(time_to_peak_anomaly), reads_since_anomaly_start(0),
      in_anomaly(false), rng(std::random_device{}()),
      normal_dist_temp(0.0, 0.1), normal_dist_humidity(0.0, 0.5) {
  history.reserve(history_max_size);
};

DataReader<SensorData>::~DataReader(){};

SensorData DataReader<SensorData>::next() {
  SensorData reading = baseline;
  auto t = std::chrono::system_clock::now();
  reading.timestamp_sec =
      std::chrono::duration_cast<std::chrono::seconds>(t.time_since_epoch())
          .count();
  // If in anomaly state
  if (in_anomaly) {
    if (reads_since_anomaly_start < time_to_peak_anomaly) {
      // Ensure ratio stays between 0 and 1
      double ratio =
          std::min(1.0, (double)reads_since_anomaly_start * time_per_read /
                            (double)time_to_peak_anomaly);

      // Apply noise and linear progress towards the anomaly's max value
      reading.temperature += (max.temperature - baseline.temperature) * ratio;
      reading.humidity += (max.humidity - baseline.humidity) * ratio;
      ++reads_since_anomaly_start;
    } else {
      in_anomaly = false;
      reads_since_anomaly_start = 0;
    }
  }

  // Add noise to both temperature and humidity
  add_noise(reading);

  // Store in history
  if (history.size() >= history_max_size) {
    history.erase(history.begin());
  }
  history.push_back(reading);

  return reading;
}

void DataReader<SensorData>::start_anomaly() {
  if (in_anomaly) {
    return;
  }
  std::cout << "Anomaly started!" << std::endl;
  in_anomaly = true;
  reads_since_anomaly_start = 0;
}

void DataReader<SensorData>::add_noise(SensorData &value) {
  value.temperature += normal_dist_temp(rng);
  value.humidity += normal_dist_humidity(rng);
  value.temperature = value.temperature;

  value.humidity = value.humidity;
}
