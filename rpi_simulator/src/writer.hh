#ifndef WRITER
#define WRITER
#include "anomaly.hh"
#include "data_reader.hh"
#include <pqxx/pqxx>

const int KETTLE_DEVICE_ID = 3;
const int TEMP_SENSOR_ID = 3;
const int HUM_SENSOR_ID = 3;
template <typename T> class Writer {
public:
  virtual void write(T data) = 0;
};

class DatabaseMeasurementWriter : virtual Writer<SensorData> {
public:
  DatabaseMeasurementWriter();
  void write(SensorData data);
  std::string prepare_query(SensorData data);

private:
  std::string get_connection_string();
  pqxx::connection connection;
};

class DatabaseNotificatonWriter : Writer<Anomaly> {
public:
  DatabaseNotificatonWriter();
  void write(Anomaly data);
  std::string prepare_query(Anomaly data);

private:
  std::string get_connection_string();
  pqxx::connection connection;
};

#endif // !WRITER