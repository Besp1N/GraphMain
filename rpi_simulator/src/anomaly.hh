
// This file provides a definition to anomaly and exposes ONE function:
// detect_anomaly.
#ifndef ANOMALY_DETECTION
#define ANOMALY_DETECTION
#include "data_reader.hh"
#include <optional>
#include <vector>
enum class AnomalyType { Warning, Error };
std::string anomaly_type_to_string(AnomalyType t);

#include <string>
struct Anomaly {
  long device_id;
  std::string message;
  AnomalyType type;
  long timestamp_sec;
};

std::optional<AnomalyType>
detect_anomaly(const std::vector<SensorData> &history);

#endif