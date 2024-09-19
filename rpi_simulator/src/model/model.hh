#ifndef MODEL
#define MODEL

#include "../data_reader.hh"
#include <atomic>
#include <optional>
#include <python3.12/pytypedefs.h>
#include <vector>

// Helpers to differentiate PyList and other PyObjects
template <typename T> struct is_vector : std::false_type {};

template <typename T> struct is_vector<std::vector<T>> : std::true_type {};

template <typename T> inline constexpr bool is_vector_v = is_vector<T>::value;
///////////
template <typename T> T cast_PyObject(PyObject *obj);

template <typename T> std::vector<T> cast_PyList(PyObject *obj);

template <typename... Args> PyObject *build_python_args(Args &&...args);

// T - expected return type
template <typename T, typename... Args>
T run_python_script(const char *script, const char *function, Args &&...args);
// R - expected output
template <typename T, typename R> class Model {
public:
  void train();
  void run(T data);
};

class IsolationTreeModel : virtual Model<const std::vector<SensorData>, bool> {
public:
  IsolationTreeModel() = default;
  void train();
  void run(const std::vector<SensorData> &data);
  std::atomic<bool> job_running{false};
  // takes and consumes the result
  std::optional<bool> take_result();

private:
  std::atomic<std::optional<bool>> result{std::nullopt};
  int data_points_required = 1;
};

// Definitions

#endif // !MODEL