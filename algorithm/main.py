import matplotlib.pyplot as plt
from data_download_temp import main as download_temp_data
from data_download_hum import main as download_hum_data
from data_preprocessing import preprocess_data
from model_training import train_model, predict_anomalies

def plot_temperature(data, anomalies):
    plt.figure(figsize=(10, 6))
    plt.plot(data['timestamp'], data['temperature'], label='Temperature')
    plt.scatter(anomalies['timestamp'], anomalies['temperature'], color='red', label='Anomalies')
    plt.xlabel('Timestamp')
    plt.ylabel('Temperature')
    plt.title('Temperature Over Time')
    plt.legend()
    plt.savefig('temperature_plot.png')
    plt.show()

def plot_humidity(data, anomalies):
    plt.figure(figsize=(10, 6))
    plt.plot(data['timestamp'], data['humidity'], label='Humidity')
    plt.scatter(anomalies['timestamp'], anomalies['humidity'], color='red', label='Anomalies')
    plt.xlabel('Timestamp')
    plt.ylabel('Humidity')
    plt.title('Humidity Over Time')
    plt.legend()
    plt.savefig('humidity_plot.png')
    plt.show()

def main():
    # Download data
    download_temp_data()
    download_hum_data()

    # Preprocess data
    data = preprocess_data('query_results_temp.csv', 'query_results_hum.csv')

    # Train model
    model = train_model(data)

    # Predict anomalies
    anomalies = predict_anomalies(model, data)

    # Save anomalies to CSV
    anomalies.to_csv('anomalies.csv', index=False)
    print("Anomalies saved to anomalies.csv")

    # Plot temperature and humidity with anomalies
    plot_temperature(data, anomalies)
    plot_humidity(data, anomalies)

if __name__ == "__main__":
    main()