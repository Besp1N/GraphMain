

#ifndef ANOMALY_DETECTION
#define ANOMALY_DETECTION
enum class AnomalyType { Warning, Error };

#include <string>
struct Anomaly {
  long device_id;
  long timestamp;
  std::string message;
  AnomalyType type;
};
#endif