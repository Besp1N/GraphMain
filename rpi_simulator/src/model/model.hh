#ifndef MODEL
#define MODEL

#include "../data_reader.hh"
#include <python3.12/pytypedefs.h>
#include <vector>

template <typename T> T cast_PyObject(PyObject *obj);
template <typename T> PyObject *cast_to_PyObject(T var);
template <typename... Args> PyObject *build_python_args(Args &&...args);

// T - expected return type
template <typename T, typename... Args>
T run_python_script(const char *script, const char *function, Args &&...args);
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