import matplotlib.pyplot as plt


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
