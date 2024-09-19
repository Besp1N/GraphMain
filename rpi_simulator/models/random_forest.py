import pandas as pd
import joblib
def forecast_future(data, periods=100):
    """
    Forecast future values for temperature, humidity, and AH using the simplified Prophet model.
    """
    # Remove timezone information from 'timestamp'
    data['timestamp'] = data['timestamp'].dt.tz_localize(None)

    # Forecast temperature
    temp_model = joblib.load("random_forest_model.pl=kl")  # Initialize simplified model
    df_temp = data[['timestamp', 'temperature']].rename(columns={'timestamp': 'ds', 'temperature': 'y'})
    temp_model.fit(df_temp)
    future_temp = temp_model.make_future_dataframe(periods=periods, freq='h')
    forecast_temp = temp_model.predict(future_temp)

    # Forecast humidity
    hum_model = simplified_prophet_model()  # Another simplified model for humidity
    df_hum = data[['timestamp', 'humidity']].rename(columns={'timestamp': 'ds', 'humidity': 'y'})
    hum_model.fit(df_hum)
    future_hum = hum_model.make_future_dataframe(periods=periods, freq='h')
    forecast_hum = hum_model.predict(future_hum)

    # Forecast absolute humidity (AH)
    ah_model = simplified_prophet_model()  # Another simplified model for AH
    df_ah = data[['timestamp', 'AH']].rename(columns={'timestamp': 'ds', 'AH': 'y'})
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

    return forecast.tail(periods)

# Example usage
if __name__ == "__main__":
    # Load historical data
    data = pd.read_csv('historical_data.csv', parse_dates=['timestamp'])

    # Forecast future data
    future_data = forecast_future(data, periods=100)
    print(future_data)