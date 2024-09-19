#include "model.hh"
#include <any>
#include <exception>
#include <format>
#include <python3.12/Python.h>
#include <python3.12/listobject.h>
#include <python3.12/object.h>
#include <python3.12/pylifecycle.h>
#include <stack>
#include <stdexcept>
#include <string>
#include <type_traits>
#include <vector>

// DEFINE ALL PYTHON FUNCTION CALLS HERE SO THE COMPILER DOESNT CRY ABOUT IT!

template std::vector<bool>
run_python_script<std::vector<bool>>(const char *script, const char *function,
                                     std::vector<SensorData> &args);
/**
 * General purpose utility for running a python function from a given Python
 * file. A failure to execute will result in a thrown exception.

 * @param script Python file name without .py extension.
 * @param function Python function name conatined in the module
 * @param input List of arguments to the function called in Python.
 * @return Rt - type specified in template
 */
template <typename T, typename... Args>
T run_python_script(const char *script, const char *function, Args &&...args) {
  // Creating object stack to prevent any memory leaks
  std::stack<PyObject *> PyObjectStack;
  // Helper lambda
  auto OnStack = [&PyObjectStack](PyObject *obj) {
    PyObjectStack.push(obj);
    return obj;
  };
  // Helper to clean up Python objects from the stack
  static auto cleanup = [](std::stack<PyObject *> &pyObjStack) {
    while (!pyObjStack.empty()) {
      PyObject *obj = pyObjStack.top();
      Py_DECREF(obj);
      pyObjStack.pop();
    }
  };

  Py_Initialize();

  try {

    // Manually add the current directory to Python's module search path
    PyObject *sysPath = OnStack(PySys_GetObject("path"));
    PyList_Append(sysPath, PyUnicode_FromString("."));
    PyObject *pName = OnStack(PyUnicode_DecodeFSDefault(script));

    if (!pName) {
      throw std::runtime_error("Failed to decode script name.");
    }

    // Init the module
    PyObject *pModule = OnStack(PyImport_Import(pName));

    if (!pModule) {
      throw std::runtime_error(
          std::format("Cannot initialize the .py script: {}.", script));
    }

    // Find the function
    PyObject *pFunc = OnStack(PyObject_GetAttrString(pModule, function));
    if (!pFunc || !PyCallable_Check(pFunc)) {
      throw std::runtime_error("Python function not found or not callable.");
    }

    // Construct py args
    PyObject *pArgs = OnStack(build_python_args(std::forward<Args>(args)...));
    if (!pArgs) {
      throw std::runtime_error("Failed to create argument tuple.");
    }

    // Try to call the function
    PyObject *pValue = OnStack(PyObject_CallObject(pFunc, pArgs));

    if (pValue == nullptr) {
      throw std::runtime_error("Function call failed.");
    }

    // Get the result or throw if cannot cast
    T result;

    if constexpr (is_vector_v<T>) {
      result = cast_PyList<typename T::value_type>(pValue);
    } else {
      result = cast_PyObject<T>(pValue);
    }

    cleanup(PyObjectStack);
    Py_Finalize();
    return result;

  } // If at any point an error is thrown the stack is cleaned up anyways
  catch (const std::exception e) {

    cleanup(PyObjectStack);

    Py_Finalize();

    throw;
  }
}
template <typename T> std::vector<T> cast_PyList(PyObject *obj) {
  if (!PyList_Check(obj)) {
    throw std::runtime_error("Expected a Python list.");
  }

  std::vector<T> result;
  Py_ssize_t size = PyList_Size(obj);

  for (Py_ssize_t i = 0; i < size; ++i) {
    PyObject *item = PyList_GetItem(obj, i); // Borrowed reference
    if (!item) {
      throw std::runtime_error("Failed to get item from Python list.");
    }

    // Recursively cast the item to the appropriate type
    result.push_back(cast_PyObject<T>(item));
  }

  return result;
}

/**
 * Helper function to cast a Python Object to C++ variable.
 * Supports bool, int, double, string
 */
template <typename T> T cast_PyObject(PyObject *obj) {
  if constexpr (std::is_same_v<T, std::vector<std::any>>) {
    return cast_PyObject<std::vector<T>>(obj);
  }
  // BOOL
  if constexpr (std::is_same_v<T, bool>) {
    return PyObject_IsTrue(obj);
    // INT
  } else if constexpr (std::is_same_v<T, int>) {
    if (PyLong_Check(obj)) {
      return (int)PyLong_AsLong(obj);
    } else {
      throw std::runtime_error("Return type mismatch: expected int.");
    }
    // DOUBLE
  } else if constexpr (std::is_same_v<T, double>) {
    if (PyFloat_Check(obj)) {
      return PyFloat_AsDouble(obj);
    } else {
      throw std::runtime_error("Return type mismatch: expected double.");
    }
  } // STRING
  else if constexpr (std::is_same_v<T, std::string> &&
                     !std::is_same_v<T, std::vector<typename T::value_type>>) {
    PyObject *repr = PyObject_Repr(obj);
    PyObject *str = PyUnicode_AsEncodedString(repr, "utf-8", "~E~");
    const char *bytes = PyBytes_AS_STRING(str);
    Py_XDECREF(repr);
    Py_XDECREF(str);
    return std::string(bytes);
  } else {
    throw std::runtime_error("Unsupported return type.");
  }
}

/**
 *  Helper similar to cast_PyObject, but reversed.
 *  Supports  bool, int, double, string. Throws otherwise.
 */
template <typename T> PyObject *cast_to_PyObject(T var) {
  if constexpr (std::is_same_v<T, bool>) {
    return PyBool_FromLong(var);
  } else if constexpr (std::is_same_v<T, int>) {
    return PyLong_FromLong(var);
  } else if constexpr (std::is_same_v<T, double>) {
    return PyFloat_FromDouble(var);
  } else if constexpr (std::is_same_v<T, std::string>) {
    return PyUnicode_FromString(var.c_str());
  } else {
    throw std::runtime_error("Unsupported type for conversion to PyObject.");
  }
}

// SensorData specialization
PyObject *cast_to_PyObject(SensorData var) {
  PyObject *dict = PyDict_New();
  if (!dict) {
    throw std::runtime_error("Failed to create dictionary.");
  }

  PyObject *pyTimestamp = PyLong_FromLong(var.timestamp_sec);
  PyObject *pyHumidity = PyFloat_FromDouble(var.humidity);
  PyObject *pyTemperature = PyFloat_FromDouble(var.temperature);

  PyDict_SetItemString(dict, "timestamp", pyTimestamp);
  PyDict_SetItemString(dict, "humidity", pyHumidity);
  PyDict_SetItemString(dict, "temperature", pyTemperature);

  // Decrease references after inserting into the dictionary
  Py_DECREF(pyTimestamp);
  Py_DECREF(pyHumidity);
  Py_DECREF(pyTemperature);

  return dict;
}
template <typename T> PyObject *cast_to_PyObject(std::vector<T> var) {
  PyObject *list = PyList_New(var.size());
  if (!list) {
    throw std::runtime_error("Failed to create list.");
  }
  for (size_t i = 0; i < var.size(); ++i) {
    PyObject *dict = cast_to_PyObject(var[i]);
    PyList_SetItem(list, i, dict); // Ownership is transferred to the list
  }

  return list;
}

/**
 * Helper function to create Python tuple args for function calls using variadic
 * args.
 */
template <typename... Args> PyObject *build_python_args(Args &&...args) {
  PyObject *pArgs = PyTuple_New(sizeof...(args));
  if (!pArgs) {
    throw std::runtime_error("Failed to create Python tuple.");
  }

  int index = 0;
  // Unpack each argument and convert to PyObject
  (...,
   PyTuple_SetItem(pArgs, index++, cast_to_PyObject(std::forward<Args>(args))));

  return pArgs;
}

// TODO: NOT IMPLEMENTED YET

void IsolationTreeModel::train() {
  throw std::runtime_error("METHOD NOT IMPLEMENTED.");
}

std::vector<bool> IsolationTreeModel::run(const std::vector<SensorData> &data) {
  return run_python_script<std::vector<bool>>("test", "run_model", data);
}
