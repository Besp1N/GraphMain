#include "anomaly.hh"

const double CRITICAL_THRESHOLD = 27.0;
// degrees per minute
const double CRITICAL_INCREASE = 0.1;

double get_short_term_derivative(SensorData data,
                                 const std::vector<SensorData> &history);
bool detect_anomaly(SensorData data, const std::vector<SensorData> &history) {
  double d_sh = get_short_term_derivative(data, history);
  if (d_sh >= CRITICAL_INCREASE) {
    return true;
  }
  return false;
};

const long SHORT_TERM_DERIVATIVE_SECONDS = 5;
const long IGNORE_SECONDS =
    10; // Amount of time to ignore before considering data

double get_short_term_derivative(const SensorData data,
                                 const std::vector<SensorData> &history) {
  long now = data.timestamp_sec;
  double avg = 0.0;
  int count = 0;

  for (int i = history.size() - 1; i >= 0; i--) {
    // Skip data points that are within the IGNORE_SECONDS window
    if (history[i].timestamp_sec > now - IGNORE_SECONDS) {
      continue;
    }

    // Break if the data is older than the short-term window
    if (history[i].timestamp_sec <=
        now - (SHORT_TERM_DERIVATIVE_SECONDS + IGNORE_SECONDS)) {
      break;
    }

    avg += history[i].temperature;
    count++;
  }

  // If no relevant data was found, return 0 (or some default)
  if (count == 0) {
    return 0.0;
  }

  avg /= count;

  // Calculate rate of change as degrees per minute
  return (data.temperature - avg) *
         ((double)SHORT_TERM_DERIVATIVE_SECONDS / 60.0);
}
