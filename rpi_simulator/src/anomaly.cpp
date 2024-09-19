#include "anomaly.hh"
#include "model/model.hh"
#include <iostream>
#include <optional>

const double CRITICAL_THRESHOLD = 27.0;
// degrees per minute
const double CRITICAL_INCREASE = 3.0;

extern double get_short_term_derivative(const std::vector<SensorData> &history);

/**
 * main function stringing together various systems into a pipeline of
 * predictions
 */
std::optional<AnomalyType>
detect_anomaly(const std::vector<SensorData> &history) {
  static IsolationTreeModel model;
  // A single model between calls.

  // first check if temp is critical
  if (history.at(history.size() - 1).temperature > CRITICAL_THRESHOLD) {
    return AnomalyType::Error; // return CRITICAL
  }
  // secondly: check if unnatural increase - emit warning

  double d_sh = get_short_term_derivative(history);
  if (d_sh >= CRITICAL_INCREASE) {
    return AnomalyType::Warning;
  }

  // run the shitbox model that detect serious anomalies
  std::cout << model.job_running.load() << std::endl;
  if (!model.job_running.load()) {
    std::cout << "Calling model.run" << std::endl;
    model.run(history);
  }
  // Don't run the model :(
  // std::optional<bool> isol_for_result = model.take_result();
  // if (isol_for_result.has_value() && isol_for_result.value()) {
  //   return AnomalyType::Error;
  // }
  // take the result or skip

  return std::nullopt;
};

const long SHORT_TERM_DERIVATIVE_SECONDS = 30;
const long IGNORE_SECONDS =
    10; // Amount of time to ignore before considering data

double get_short_term_derivative(const std::vector<SensorData> &history) {
  long now = history.at(history.size() - 1).timestamp_sec;
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

  avg /= count; // * 5
  // Calculate rate of change as degrees per minute
  return (history.at(history.size() - 1).temperature - avg) * 60. /
         (double)SHORT_TERM_DERIVATIVE_SECONDS;
}
