#ifndef MODEL
#define MODEL

#include "../data_reader.hh"
#include <python3.12/pytypedefs.h>
#include <vector>
template <typename T> T cast_py_object(PyObject *obj);

// T - expected return type
template <typename T>
T run_python_script(const char *script, const char *function);
// T - expected input
// R - expected output
template <typename T, typename R> class Model {
  virtual void train();
  virtual R run(T data);
};

class IsolationTreeModel : virtual Model<std::vector<SensorData>, bool> {
public:
  IsolationTreeModel(IsolationTreeModel &m) = delete;

  void train();
  bool run(std::vector<SensorData> data);
};

#endif // !MODEL