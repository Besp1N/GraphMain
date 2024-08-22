** About this directory
This is a copy of the main solution in src that's temporary.
Since the network this device will be connected to in the future won't have access to AWS due to the firewall. For now, this works since a VPN is used.

You need to create aws_config.py file with connection data:
DB_PARAMS = {
    "password": "",
    "host": "",
    "user": "",
    "dbname": "",
    "sslmode": "require",
    "port": 5432
}
