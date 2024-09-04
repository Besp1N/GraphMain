import time
import pandas as pd
import data_downloader.data_download_temp as data_temp_downloader
import data_downloader.data_download_hum as data_hum_downloader
from plots.plot_temperature import plot_temperature
from plots.plot_humidity import plot_humidity
from data_process.absolute_hum_math import calculate_and_save_absolute_humidity

from data_process.data_preprocessing import preprocess_data
from data_process.model_training import train_model, predict_anomalies

import query.query as query


def main():
    while True:
        try:
            data_temp_downloader.save_temp_to_csv(
                query.queries["temp"],
                "results/query_results_temp.csv",
            )

            data_hum_downloader.save_hum_to_csv(
                query.queries["hum"],
                "results/query_results_hum.csv",
            )

            data = preprocess_data(
                'results/query_results_temp.csv',
                'results/query_results_hum.csv'
            )

            model = train_model(data)

            anomalies = predict_anomalies(model, data)
            anomalies.to_csv('results/anomalies.csv', index=False)
            print("Anomalies saved to anomalies.csv")

            calculate_and_save_absolute_humidity(
                'results/query_results_hum.csv',
                'results/query_results_temp.csv',
                'results/absolute_humidity.csv'
            )

            # plot_temperature(data, anomalies)
            # plot_humidity(data, anomalies)

            time.sleep(60)

        except KeyboardInterrupt:
            print("Process interrupted by user. Exiting...")
            break


if __name__ == "__main__":
    main()
