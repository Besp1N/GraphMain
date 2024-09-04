import pandas as pd
from sklearn.preprocessing import StandardScaler

def preprocess_data(temp_csv, hum_csv):
    # Load data
    temp_data = pd.read_csv(temp_csv)
    hum_data = pd.read_csv(hum_csv)

    # Ensure correct column names
    temp_data.columns = ['id', 'timestamp', 'temperature', 'sensor_id_temp']
    hum_data.columns = ['id', 'timestamp', 'humidity', 'sensor_id_hum']

    # Merge data on timestamp
    data = pd.merge(temp_data, hum_data, on='timestamp', suffixes=('_temp', '_hum'))

    # Handle missing values
    data = data.ffill()

    # Feature engineering
    data['timestamp'] = pd.to_datetime(data['timestamp'])
    data['hour'] = data['timestamp'].dt.hour
    data['day_of_week'] = data['timestamp'].dt.dayofweek

    # Ensure the columns exist
    if 'temperature' in data.columns and 'humidity' in data.columns:
        # Normalize data
        scaler = StandardScaler()
        data[['temperature', 'humidity']] = scaler.fit_transform(data[['temperature', 'humidity']])
    else:
        raise KeyError("Columns 'temperature' and 'humidity' must be present in the data")

    return data