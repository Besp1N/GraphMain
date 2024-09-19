import joblib
import pandas as pd
# we get: timestamp as seconds, temperature as float, humidity as float


def preprocess_input(input_data):
    # Convert the input data into a pandas DataFrame
    df = pd.DataFrame(input_data)
    
    # Convert timestamp to datetime
    df['timestamp'] = pd.to_datetime(df['timestamp'], unit='s')
    # Extract features like hour and day of week
    df['hour'] = df['timestamp'].dt.hour
    df['day_of_week'] = df['timestamp'].dt.dayofweek
    return df


def run_model(input_data):

    data = preprocess_input(input_data)
    
    model = joblib.load("./models/isolation_forest_model.pkl")
    joblib.load

    features = ['temperature', 'humidity', 'hour', 'day_of_week']

    X = data[features]

    # Predict anomalies
    data['anomaly'] = model.predict(X)

    # -1 indicates anomaly, 1 indicates normal
    anomalies = data[data['anomaly'] == -1]

    # Get the last anomaly
    if not anomalies.empty:
        return anomalies['anomaly'].iloc[-1] == -1
    else:
        return False
    
def train():
    raise Exception("Not implemented yet")    

if __name__ == "__main__":
    run_model([{"timestamp": 100000, "temperature": 10.0, "humidity": 100.0}])