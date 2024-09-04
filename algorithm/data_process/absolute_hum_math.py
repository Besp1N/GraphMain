import math

import pandas as pd


def calculate_absolute_humidity(RH, T):
    return (217 * RH * 6.112 * math.exp((17.67 * T) / (T + 243.5))) / (100 * (T + 273.15))


rh_df = pd.read_csv('query_results_hum.csv')
t_df = pd.read_csv('query_results_temp.csv')

rh_df.rename(columns={'value': 'RH'}, inplace=True)
t_df.rename(columns={'value': 'T'}, inplace=True)

df = pd.concat([rh_df, t_df], axis=1)

# Assuming 'RH' column is for humidity and 'T' column is for temperature, apply the function
df['AH'] = df.apply(lambda row: calculate_absolute_humidity(row['RH'], row['T']), axis=1)

# Save the resulting DataFrame to a new CSV file
df[['id', 'timestamp', 'AH']].to_csv('absolute_humidity.csv', index=False)

print("Absolute Humidity has been calculated and saved to 'output_AH.csv'.")
