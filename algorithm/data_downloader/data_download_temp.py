from config import aws_config
import psycopg2
import psycopg2.extras
import csv
import os


def save_temp_to_csv(query, csv_filename):
    conn = None
    cursor = None
    try:
        conn = psycopg2.connect(**aws_config.DB_PARAMS)
        cursor = conn.cursor(cursor_factory=psycopg2.extras.NamedTupleCursor)

        cursor.execute(query)
        rows = cursor.fetchall()
        column_names = [desc[0] for desc in cursor.description]

        with open(csv_filename, 'w', newline='') as csvfile:
            csvwriter = csv.writer(csvfile)
            csvwriter.writerow(column_names)
            for row in rows:
                csvwriter.writerow(row)

        if rows:
            print(f"New records added to {csv_filename}")

    except Exception as error:
        print(f"Database operation error: {error}")

    finally:
        if cursor is not None:
            cursor.close()
        if conn is not None:
            conn.close()
