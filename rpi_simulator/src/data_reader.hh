#ifndef DATA_READER
#define DATA_READER

#include <random>
#include <vector>
struct SensorData {
  double temperature;
  double humidity;
  long timestamp_sec;
  SensorData operator+(SensorData rhs);
};
// class responsible for generating believable sensor data/
template <typename T> class DataReader {

public:
  T max;
  T baseline;
  bool in_anomaly; // Whether we're in an anomaly state

  DataReader(T max, T baseline, int time_per_read, int time_to_peak_anomaly);
  ~DataReader();
  T next();
  void start_anomaly();
  std::vector<T> history;

private:
  int time_per_read;             // in seconds
  int time_to_peak_anomaly;      //  in seconds
  int reads_since_anomaly_start; //
  // magic number, doesn't matter for this project
  int history_max_size = 100;

  // random noise stuff
  std::mt19937 rng;
  std::normal_distribution<T> normal_dist;
  std::normal_distribution<T> anomaly_dist;

  void add_noise(T &value);
};

////////////////////////////
// SensorData Specialization
////////////////////////////

template <> class DataReader<SensorData> {
public:
  SensorData max;
  SensorData baseline;

  DataReader(SensorData max, SensorData baseline, int time_per_read,
             int time_to_peak_anomaly);

  ~DataReader();

  SensorData next();

  void start_anomaly();

  std::vector<SensorData> history;

private:
  int time_per_read;
  int time_to_peak_anomaly;
  int reads_since_anomaly_start;
  bool in_anomaly;
  int history_max_size = 100;

  std::mt19937 rng;
  std::normal_distribution<double> normal_dist_temp;
  std::normal_distribution<double> normal_dist_humidity;

  void add_noise(SensorData &value);
};

#endif // !DATA_READER#define DATA_READER
