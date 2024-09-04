from sklearn.ensemble import IsolationForest


def train_model(data):
    # Define features
    features = ['temperature', 'humidity', 'hour', 'day_of_week']
    X = data[features]

    # Train Isolation Forest model
    model = IsolationForest(contamination=0.01)
    model.fit(X)

    return model


def predict_anomalies(model, data):
    # Define features
    features = ['temperature', 'humidity', 'hour', 'day_of_week']
    X = data[features]

    # Predict anomalies
    data['anomaly'] = model.predict(X)

    # -1 indicates anomaly, 1 indicates normal
    anomalies = data[data['anomaly'] == -1]

    return anomalies