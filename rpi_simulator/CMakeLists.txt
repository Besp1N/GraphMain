cmake_minimum_required(VERSION 3.10)
project(RPISimulator)

# Set C++ Standard
set(CMAKE_CXX_STANDARD 23)

# Dlib and other dependencies
find_package(OpenBLAS REQUIRED)
find_package(LAPACK REQUIRED)

# Dlib - Installed via pacman
find_package(dlib REQUIRED)

# PostgreSQL
find_package(PostgreSQL REQUIRED)
include_directories(${PostgreSQL_INCLUDE_DIRS})



# Include directories for Dlib
include_directories(${dlib_INCLUDE_DIRS})

# Add the source files
add_executable(simulator src/main.cpp src/data_reader.cpp src/writer.cpp)

# Link libraries
target_link_libraries(simulator 
-lpqxx
  ${PostgreSQL_LIBRARIES} 
  OpenBLAS::OpenBLAS 
  LAPACK::LAPACK 
  dlib::dlib 
)

# Display some information for debugging
message(STATUS "PostgreSQL Include Dirs: ${PostgreSQL_INCLUDE_DIRS}")
message(STATUS "PostgreSQL Libraries: ${PostgreSQL_LIBRARIES}")
message(STATUS "Dlib Include Dirs: ${dlib_INCLUDE_DIRS}")
