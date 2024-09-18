#include "model.hh"
#include <format>
#include <python3.12/Python.h>
#include <python3.12/object.h>
#include <python3.12/pylifecycle.h>
#include <stdexcept>
#include <string>
#include <type_traits>
#include <vector>

template bool run_python_script<bool>(const char *script, const char *function);
template int run_python_script<int>(const char *script, const char *function);
template double run_python_script<double>(const char *script,
                                          const char *function);
template std::string run_python_script<std::string>(const char *script,
                                                    const char *function);

/**
 * General purpose utility for running a python function from a given Python
 * file. A failure to execute will result in a thrown exception.
 * @param script Python file name without .py extension.
 * @param function Python function name conatined in the module
 * @param input List of arguments to the function called in Python.
 * @return Rt - type specified in template
 */
template <typename T>
T run_python_script(const char *script, const char *function) {
  Py_Initialize();

  // Manually add the current directory to Python's module search path
  PyObject *sysPath = PySys_GetObject("path");
  PyList_Append(sysPath, PyUnicode_FromString("."));

  PyObject *pName = PyUnicode_DecodeFSDefault(script);
  if (!pName) {
    Py_Finalize();
    throw std::runtime_error("Failed to decode script name.");
  }

  PyObject *pModule = PyImport_Import(pName);
  Py_DECREF(pName); // Safe to decrement here since pModule is obtained.

  if (!pModule) {
    Py_Finalize();
    throw std::runtime_error(
        std::format("Cannot initialize the .py script: {}.", script));
  }

  PyObject *pFunc = PyObject_GetAttrString(pModule, function);
  if (!pFunc || !PyCallable_Check(pFunc)) {
    Py_DECREF(pModule);
    Py_XDECREF(pFunc);
    Py_Finalize();
    throw std::runtime_error("Python function not found or not callable.");
  }

  PyObject *pArgs = PyTuple_Pack(0);
  if (!pArgs) {
    Py_DECREF(pModule);
    Py_DECREF(pFunc);
    Py_Finalize();
    throw std::runtime_error("Failed to create argument tuple.");
  }

  PyObject *pValue = PyObject_CallObject(pFunc, pArgs);
  Py_DECREF(pArgs);

  if (pValue == nullptr) {
    Py_DECREF(pModule);
    Py_DECREF(pFunc);
    Py_Finalize();
    throw std::runtime_error("Function call failed.");
  }

  T result = cast_py_object<T>(pValue);
  Py_DECREF(pValue);
  Py_DECREF(pModule);
  Py_DECREF(pFunc);

  Py_Finalize();
  return result;
}

/**
 * Helper function to cast a Python Object to C++ variable.
 * Supports bool, int, double, string
 */
template <typename T> T cast_py_object(PyObject *obj) {
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
  } else if constexpr (std::is_same_v<T, std::string>) {
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

void IsolationTreeModel::train() {}

bool IsolationTreeModel::run(std::vector<SensorData> data) { return true; }