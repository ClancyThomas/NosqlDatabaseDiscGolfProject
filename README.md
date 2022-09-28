# Project Overview

This project was inspired as I was manually calculating statistics for my local disc golf league. I was using an excel spreadsheet and manually entering in the data week after week. Although I was happy to help out, I knew there was a better way. At the time I also wanted to learn about cloud databases so I decided that I would solve my problem while I was learning. It was a fun project that stretched me to learn about new technologies and the pros/cons that come along with them. In hindsight, a relational database probably would have allowed me to complete this project more efficiently, but Oracle NoSQL database worked well enough.

First off, this software is meant to used to help process the league night data that is available on UDisc.com. The data is posted publicly each week for every disc golf league who uses their platform to keep scores. This program has a console/terminal menu that allows the user to upload csv files to a database and also make various queries against the database. Currently this program is optimized and setup for the Teton River Disc Golf league in Rexburg, Idaho.

[Software Demo Video](https://youtu.be/jjIPJWidTac)

# Cloud Database

Oracle NoSQL Cloud Database (Always Free Tier)

This database is just one table that has nine columns. It stores basic data types such as integers and strings.

# Development Environment

* Java 11
* Oracle NoSQL Java SDK
* Intellij 2022.2.2 

# Useful Websites

* [Oracle NoSQL Java SDK](https://github.com/oracle/nosql-java-sdk)
    * Quickstart.java example in the documentation is great for getting started and learning most basic functionality
* [Authentication Configuring](https://docs.oracle.com/en-us/iaas/Content/API/Concepts/sdkconfig.htm)
    * Useful resource that helps with understanding how to setup the authentication 
* [SQL for Oracle NoSQL](https://docs.oracle.com/en/database/other-databases/nosql-database/18.3/sqlfornosql-spec/sqlfornosql-spec.pdf)
    * Explains some of the sql commands that can be used with Oracle NoSQL
* [Oracle NoSQL Documentation](https://docs.oracle.com/en/database/other-databases/nosql-database/22.1/sqlreferencefornosql/index.html)
    * Complete overview of Oracle NoSQL databases and how to work with them
* [MapValue Documention](https://github.com/oracle/nosql-java-sdk/blob/main/driver/src/main/java/oracle/nosql/driver/values/MapValue.java)
    * Explains how to work with the values that are returned by the query requests

# Future Work

* Add a GUI instead of using a console/terminal window
* Add more items to the menu list
* Further investigate why the queries return the columns in a strange order
* Add the ability to change the table to use without restarting the program
