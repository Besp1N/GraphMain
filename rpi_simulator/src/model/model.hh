#ifndef MODEL
#define MODEL

#include "../data_reader.hh"
#include <vector>

class Model {
  void get_training_data();
  virtual void train();
  std::string get_model_file();
};

class IsolationTreeModel : virtual Model {
public:
  std::vector<SensorData> get_training_data();
  // Notes: This deletes private training_data freeing much memory.
  void train();
  std::string get_model_file();

private:
  std::vector<SensorData> training_data;
};

#endif // !MODEL