#ifndef MODEL
#define MODEL

#include "../data_reader.hh"
#include <vector>

// T - expected input
// R - expected output
template <typename T, typename R> class Model {
  virtual void train();
  virtual R run(T data);
};

class IsolationTreeModel : virtual Model<std::vector<SensorData>, bool> {
public:
  void train();
  bool run();
};

#endif // !MODEL