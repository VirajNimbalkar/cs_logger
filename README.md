# cs_logger application
### To change embedded database file path, please open application.yaml in resource folder and update HSSQL file DB path
### To Create file log file of 10 GB at specific location open LogCreator from this package com.logger.main.LogCreator
### final long numberLogs = 70000000 + 1; This setting will create 11 GB of log files

# Problems can be solved by various methods 
### Using Producer Consumer one option
### Using Kafka by having log id as partition key
### Using Distributed computing using framework like Apache Ignite

