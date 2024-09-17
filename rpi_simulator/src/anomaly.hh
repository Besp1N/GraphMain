
// This file provides a definition to anomaly and exposes ONE function:
// detect_anomaly.
#ifndef ANOMALY_DETECTION
#define ANOMALY_DETECTION
#include "data_reader.hh"
#include <vector>
enum class AnomalyType { Warning, Error };

#include <string>
struct Anomaly {
  long device_id;
  std::string message;
  AnomalyType type;
};

bool detect_anomaly(SensorData data, const std::vector<SensorData> &history);

#endif