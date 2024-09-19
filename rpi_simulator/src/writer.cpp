#include "writer.hh"
#include "anomaly.hh"
#include "data_reader.hh"
#include <ctime>
#include <filesystem>
#include <fstream>
#include <iostream>
#include <ostream>
#include <pqxx/pqxx>
#include <stdexcept>
#include <string>
#include <unordered_map>

const std::filesystem::path CONN_STRING_PATH =
    std::filesystem::exists("../connection_string.txt")
        ? "../connection_string.txt"
        : "connection_string.txt";

std::string anomaly_type_to_string(AnomalyType t) {
  static const std::unordered_map<AnomalyType, const char *>
      anomaly_type_to_string = {

          {AnomalyType::Error, "error"}, {AnomalyType::Warning, "warning"}};

  return anomaly_type_to_string.at(t);
}

std::string get_aws_conn_string() {

  std::ifstream file(CONN_STRING_PATH);
  if (!file) {
    throw std::runtime_error("Cannot read the connection string file. Make "
                             "sure it's present in the root dir.");
  }
  std::stringstream buffer;
  buffer << file.rdbuf(); // Read the entire file into the stringstream

  file.close();

  std::string conn_str = buffer.str(); // Convert the stringstream to a string

  return conn_str;
}
/////////////////////////////////////
//  MEASUREMENT WRITER IMPLEMENTATION
/////////////////////////////////////

std::string DatabaseMeasurementWriter::get_connection_string() {
  return get_aws_conn_string();
};
DatabaseMeasurementWriter::DatabaseMeasurementWriter()
    : connection{pqxx::connection(get_connection_string().data())} {
  if (!connection.is_open()) {
    throw std::runtime_error("Connection to the database failed.");
  } else {
    std::cout << "Connection established!" << std::endl;
  }
}
void DatabaseMeasurementWriter::write(SensorData data) {
  std::string query = prepare_query(data);
  if (connection.is_open()) {
    try {
      pqxx::work w(connection);
      w.exec(query);
      w.commit();

    } catch (const std::exception &e) {
      std::cerr << e.what() << std::endl;
      throw;
    }
  } else {
    throw std::runtime_error("DB Connection closed.");
  }
}
std::string DatabaseMeasurementWriter::prepare_query(SensorData data) {
  auto now = std::chrono::system_clock::now();

  // Convert to time_t for formatting
  std::time_t now_time_t = std::chrono::system_clock::to_time_t(now);

  // Convert to tm structure
  std::tm now_tm = *std::gmtime(&now_time_t);

  // Create a stringstream to format the time
  std::stringstream ss;
  ss << std::put_time(&now_tm, "%Y-%m-%dT%H:%M:%S");
  std::string timestamp = ss.str();

  // Use std::format to create the queries
  std::string t_q = std::format("INSERT INTO measurements (timestamp, "
                                "sensor_id, value) VALUES ('{}', {}, {});",
                                timestamp, TEMP_SENSOR_ID, data.temperature);

  std::string h_q = std::format("INSERT INTO measurements (timestamp, "
                                "sensor_id, value) VALUES ('{}', {}, {});",
                                timestamp, HUM_SENSOR_ID, data.humidity);

  return t_q + "\n" + h_q;
}

/////////////////////////////////////
//  NOTIFICATION WRITER IMPLEMENTATION
/////////////////////////////////////

std::string DatabaseNotificatonWriter::get_connection_string() {
  return get_aws_conn_string();
};
DatabaseNotificatonWriter::DatabaseNotificatonWriter()
    : connection{pqxx::connection(get_connection_string().data())} {
  if (!connection.is_open()) {
    throw std::runtime_error("Connection to the database failed.");
  } else {
    std::cout << "Connection established!" << std::endl;
  }
}
void DatabaseNotificatonWriter::write(Anomaly data) {
  std::string query = prepare_query(data);
  if (connection.is_open()) {
    try {
      pqxx::work w(connection);
      w.exec(query);
      w.commit();

    } catch (const std::exception &e) {
      std::cerr << e.what() << std::endl;
      throw;
    }
  } else {

    throw std::runtime_error("DB Connection closed.");
  }
}
std::string DatabaseNotificatonWriter::prepare_query(Anomaly data) {
  std::chrono::seconds duration_since_epoch(data.timestamp_sec);

  // Cast duration to a time_point
  std::chrono::time_point<std::chrono::system_clock> now(duration_since_epoch);

  std::time_t now_time_t = std::chrono::system_clock::to_time_t(now);

  // Convert to tm structure
  std::tm now_tm = *std::gmtime(&now_time_t);

  // Create a stringstream to format the time
  std::stringstream ss;
  ss << std::put_time(&now_tm, "%Y-%m-%dT%H:%M:%S");
  std::string timestamp = ss.str();

  return std::format("INSERT INTO notifications (message, type, created_at, "
                     "device_id) VALUES ('{}', '{}', '{}', {});",
                     data.message, anomaly_type_to_string(data.type), timestamp,
                     data.device_id);
}