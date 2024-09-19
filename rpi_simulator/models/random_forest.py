import pandas as pd
import joblib
def forecast_future(data, periods=100):
    """
    Forecast future values for temperature, humidity, and AH using the simplified Prophet model.
    """
    # Convert data into a DataFrame
    df = pd.DataFrame(data)

    # Ensure 'timestamp' is a datetime object and remove timezone information
    df['timestamp'] = pd.to_datetime(df['timestamp'], unit='s')
    df['timestamp'] = df['timestamp'].dt.tz_localize(None)

    # Forecast temperature
    temp_model = joblib.load("models/random_forest_model.pkl")  # Initialize simplified model
    df_temp = df[['timestamp', 'temperature']].rename(columns={'timestamp': 'ds', 'temperature': 'y'})
    temp_model.fit(df_temp)
    future_temp = temp_model.make_future_dataframe(periods=periods, freq='h')
    forecast_temp = temp_model.predict(future_temp)

    # Forecast humidity
    hum_model = joblib.load("models/random_forest_model.pkl")  # Another simplified model for humidity
    df_hum = df[['timestamp', 'humidity']].rename(columns={'timestamp': 'ds', 'humidity': 'y'})
    hum_model.fit(df_hum)
    future_hum = hum_model.make_future_dataframe(periods=periods, freq='h')
    forecast_hum = hum_model.predict(future_hum)

    # Forecast absolute humidity (AH)
    ah_model = joblib.load("models/random_forest_model.pkl")  # Another simplified model for AH
    df_ah = df[['timestamp', 'AH']].rename(columns={'timestamp': 'ds', 'AH': 'y'})
    ah_model.fit(df_ah)
    future_ah = ah_model.make_future_dataframe(periods=periods, freq='h')
    forecast_ah = ah_model.predict(future_ah)

    # Combine forecasts into a single DataFrame
    forecast = pd.DataFrame({
        'timestamp': forecast_temp['ds'],
        'temperature': forecast_temp['yhat'],
        'humidity': forecast_hum['yhat'],
        'AH': forecast_ah['yhat']
    })

    # Add hour and day_of_week features for Random Forest
    forecast['hour'] = forecast['timestamp'].dt.hour
    forecast['day_of_week'] = forecast['timestamp'].dt.dayofweek
    
    print(forecast.tail(periods))
    return forecast.tail(periods)
# Example usage
if __name__ == "__main__":
    data = [
    {"timestamp": 100000, "temperature": 10.0, "humidity": 100.0, "AH": 0.02}, 
    {"timestamp": 100001, "temperature": 11.0, "humidity": 99.0, "AH": 0.021}
    ]

    # Forecast future data
    future_data = forecast_future(data, periods=100)
    print(future_data)