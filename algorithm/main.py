import data_downloader.data_download_temp as data_temp_downloader
import data_downloader.data_download_hum as data_hum_downloader
import plots.plot_temperature as plot_temperature
import plots.plot_humidity as plot_humidity

from data_process.data_preprocessing import preprocess_data
from data_process.model_training import train_model, predict_anomalies

import query.query as query


def main():
    data_temp_downloader.save_temp_to_csv(
        query.queries["temp"],
        "query_results_temp.csv"
    )

    data_hum_downloader.save_hum_to_csv(
        query.queries["hum"],
        "query_results_hum.csv"
    )

    data = preprocess_data(
        'query_results_temp.csv',
        'query_results_hum.csv'
    )

    model = train_model(data)

    anomalies = predict_anomalies(model, data)

    anomalies.to_csv('anomalies.csv', index=False)
    print("Anomalies saved to anomalies.csv")

    plot_temperature.plot_temperature(
        data, anomalies
    )

    plot_humidity.plot_humidity(
        data, anomalies
    )


if __name__ == "__main__":
    main()
