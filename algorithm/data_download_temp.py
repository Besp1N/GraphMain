import aws_config
import psycopg2
import psycopg2.extras
import csv
import os

def execute_query_and_save_to_csv(query, csv_filename):
    conn = None
    cursor = None
    try:
        # Connect to the PostgreSQL database
        conn = psycopg2.connect(**aws_config.DB_PARAMS)

        # Create a cursor with named tuple factory for more accessible results
        cursor = conn.cursor(cursor_factory=psycopg2.extras.NamedTupleCursor)

        # Execute the query
        cursor.execute(query)

        # Fetch all rows from the executed query
        rows = cursor.fetchall()

        # Get the column names from the cursor description
        column_names = [desc[0] for desc in cursor.description]

        # Write the results to a CSV file
        with open(csv_filename, 'w', newline='') as csvfile:
            csvwriter = csv.writer(csvfile)
            csvwriter.writerow(column_names)  # Write the column headers
            for row in rows:
                csvwriter.writerow(row)  # Write the data rows

        print(f"Query results saved to {csv_filename}")

    except Exception as error:
        print(f"Database operation error: {error}")

    finally:
        if cursor is not None:
            cursor.close()
        if conn is not None:
            conn.close()

def main():
    # Define the SQL query with date filter
    query = "SELECT * FROM measurements WHERE sensor_id = 3 AND timestamp >= '2024-08-25T00:00:00'"

    # Define the CSV filename where results will be saved
    csv_filename = "query_results_temp.csv"

    # Execute the query and save the results to a CSV file
    execute_query_and_save_to_csv(query, csv_filename)

if __name__ == "__main__":
    main()