cmake_minimum_required(VERSION 3.10)
project(RPISimulator)

# Set C++ Standard
set(CMAKE_CXX_STANDARD 20)


# PostgreSQL
find_package(PostgreSQL REQUIRED)
include_directories(${PostgreSQL_INCLUDE_DIRS})

find_package(PythonLibs REQUIRED)
include_directories(${PYTHON_INCLUDE_DIRS})

# Include directories for Dlib

# Add the source files
add_executable(simulator src/main.cpp src/data_reader.cpp src/writer.cpp src/model/model.cpp src/anomaly.cpp  )

# Link libraries
target_link_libraries(simulator 
-lpqxx
  ${PostgreSQL_LIBRARIES} 
  ${PYTHON_LIBRARIES}
)

# Display some information for debugging
message(STATUS "PostgreSQL Include Dirs: ${PostgreSQL_INCLUDE_DIRS}")
message(STATUS "PostgreSQL Libraries: ${PostgreSQL_LIBRARIES}")
message(STATUS "Dlib Include Dirs: ${dlib_INCLUDE_DIRS}")
