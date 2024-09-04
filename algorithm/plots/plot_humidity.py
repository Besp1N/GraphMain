import matplotlib.pyplot as plt


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