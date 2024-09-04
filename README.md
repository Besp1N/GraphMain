
# GraphMain

A IoT Predictime Maintenace Project. Created during Internship in GlobalLogic Poland company.


## Authors

- [@Kacper Karabinowski](https://github.com/Besp1N)
- [@Klaudiusz Petryk](https://github.com/PendolinoVoyager)
- [@Tsimur Khudayarau](https://github.com/RooDie10)
- [@Sabina Kubiyeva](https://github.com/ChersobiusSignatus)


## Installation

Install our project 

```bash
  git clone https://github.com/Besp1N/GraphMain
```

Go to backend directory and create resource diredcotry, and application properties to connect to your database.

```bash
  cd backend/src/main/java
  md resources
  cd resources
  touch application.properties
```

Open the application.properties file and put code bellow

```bash
    spring.datasource.url=jdbc:postgresql://change_me
    spring.datasource.username=change_me
    spring.datasource.password=change_me
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

Replace change_me with your data and install dependencies

```bash
    mvn clear install
```

Run the project

```bash
    mvn spring-boot:run
```

To run frontend app

```bash
    cd graphmain-fe
    npm i 
    npm run dev
```

To run algorithm

firstly set up conda environment with python 3.8 and install dependencies

```bash
    conda create -n graphmain python=3.8
    conda activate graphmain
    pip install -r requirements.txt
```

then run the algorithm

```bash
    cd algorithm
    put AWS config in aws_config.py
    python3 data_download_hum.py
    python3 data_download_temp.py
    python3 main.py
```
