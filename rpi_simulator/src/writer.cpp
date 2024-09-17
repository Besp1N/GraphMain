#include "writer.hh"
// #include "anomaly.hh"
#include "data_reader.hh"
#include <ctime>
#include <fstream>
#include <iostream>
#include <ostream>
#include <pqxx/pqxx>
#include <stdexcept>
#include <string>
// #include <unordered_map>

// const std::unordered_map<AnomalyType, const char *> anomaly_type_to_string =
// {

//     {AnomalyType::Error, "error"}, {AnomalyType::Warning, "warning"}};

std::string DatabaseMeasurementWriter::get_connection_string() {
  std::ifstream file("../connection_string.txt");
  if (!file) {
    throw std::runtime_error("Cannot read the connection string file. Make "
                             "sure it's present in the root dir.");
  }
  std::stringstream buffer;
  buffer << file.rdbuf(); // Read the entire file into the stringstream

  file.close();

  std::string conn_str = buffer.str(); // Convert the stringstream to a string

  return conn_str;
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
      std::cout << "Inserted data!" << std::endl;

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
  ss << std::put_time(&now_tm, "%Y-%m-%dT%H:%M");
  std::string timestamp = ss.str();

  // Use std::format to create the queries
  std::string t_q = std::format("INSERT INTO measurements (timestamp, "
                                "sensor_id, value) VALUES ('{}', 3, {});",
                                timestamp, data.temperature);

  std::string h_q = std::format("INSERT INTO measurements (timestamp, "
                                "sensor_id, value) VALUES ('{}', 4, {});",
                                timestamp, data.humidity);

  std::cout << t_q << std::endl; // prints the first query

  return t_q + "\n" + h_q;
}

// void DatabaseNotificatonWriter::write(Anomaly data) {
//   std::cout << "Sending..." << std::endl;
// }
// void test() {
//   try {
//     pqxx::connection C("dbname=your_db user=your_user
//     password=your_password
//     "
//                        "host=your_host port=your_port");
//     if (C.is_open()) {
//       std::string sql =
//           "INSERT INTO your_table (temperature, humidity) VALUES (" +
//           std::to_string(data(0)) + ", " + std::to_string(data(1)) + ");";

//       pqxx::work W(C);
//       W.exec(sql);
//       W.commit();
//       std::cout << "Data inserted successfully" << std::endl;
//     } else {
//       std::cerr << "Failed to open PostgreSQL connection" << std::endl;
//     }
//   } catch (const std::exception &e) {
//     std::cerr << e.what() << std::endl;
//   }
// }