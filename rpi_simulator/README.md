# RPISimulator

## Overview

`RPISimulator` is a C++ project designed for simulating and analyzing sensor data as the dead RaspberrPi in the office would do normally. The project utilizes various libraries including PostgreSQL, and Python for its functionalities. 

## Requirements

- CMake 3.10 or higher
- C++20 Standard
- PostgreSQL
- Python
- Sklearn, Pandas and JobLib Python modules
## Dependencies

1. **PostgreSQL**: Relational database management system.
2. **PostgreSQL**: libpqxx - C++ library for PostgreSQL client.
3. **Python**: Used for scripting and interacting with Python-based models.

## Build Instructions

1. **Clone the repository:**

   ```sh
   git clone <repository-url>
   cd RPISimulator
2. **Create build directory and navigate to it***
``` bash
mkdir build
cd build
```
3. **Run CMake to configure the project***
``` bash
cmake ..
```
4. **Build the project***
``` bash
cmake ..
```

## Running the App
Make sure you are in the root project directory, so that the relative-path Python scripts can be found. Then just launch the executable.
## Project Structure

- src/main.cpp: Entry point for the simulator.
- src/data_reader.cpp: Handles data reading (generating) functionalities.
- src/writer.cpp: Responsible for writing data.
- src/model/model.cpp: Contains model and Python related code.
-src/anomaly.cpp: Implements anomaly detection.

## Linking Libraries

The simulator executable is linked with the following libraries:

- libpqxx: C++ client library for PostgreSQL
- PostgreSQL Libraries
- Python Libraries

