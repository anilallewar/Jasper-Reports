The "ant.properties" file contains the build specific parameters; you would not have to change any of them except the 
"tomcat.dir" which should point to tomcat or any other web server that you want to use to point the servlet-api jar
and for deployment.
 
**************************************************************************************************************************

Also set the correct connection parameters in the "com.anil.dao.ConnectionFactory" class to use the driver and URL that
you want to use.
TODO
Move the connection parameters to a properties file. 

**************************************************************************************************************************
Default ANT task will compile the reports and the source, it will also generate a deployable war file  named reports.war.

To execute the generated servlets, deploy the war file and point the browser to 
	http://localhost:8080/reports/<servlet url pattern>

Substitute <servlet url pattern> with the appropriate url pattern from the <servlet-mapping> element in web.xml

**************************************************************************************************************************

Before any of the examples for this chapter can be executed, the flightstats database needs to be created and populated. 

Refer to the MySQL web site at http://www.mysql.com for MySQL installation instructions.

Additionally, before all the ANT targets can be successfully built, the MySQL JDBC driver needs to be downloaded from 		            
	http://dev.mysql.com/downloads/connector/j/. 

The driver location in the filesystem must be specified in the mysql.jbc.jar property in ant.properties. 

Once MySQL is installed, create a new, empty database named "flightstats".

Extract flightstats.zip from this folder into any directory. This ZIP file contains a single file called flightstats_myisam.sql,

To load the data in this file into the newly created database, issue the following command:

	"mysql -u USER -p flightstats < flightstats_myisam.sql". 

Substitute USER with a user that can create tables.

**************************************************************************************************************************

EmptyDSReportFill executable can be used to fill all report templates not requiring a database connection.

To execute it, an ANT target is provided, it can be used as illustrated in the following example:

ant -Drpt=PlainTextExportDemoReport fillReport

The value of the rpt property must match the report name.

**************************************************************************************************************************

DbConnectionReportFill executable can be used to fill all report templates requiring a database connection.

It requires an instance of MySQL containing the flightstats database to be running on the local host (download flightstats.zip for details).

To execute it, an ANT target is provided, it can be used as illustrated in the following example:

ant -Drpt=BarChartReportDemo fillDbReport

The value of the rpt property must match the report name.

**************************************************************************************************************************

To view generated reports, the type the following on the console:

ant -Drpt=BarChartReportDemo view

The value for the rpt parameter must match the name of the report to view.

**************************************************************************************************************************

To execute all ExportDemo executables, an ANT target is provided. It can be used as illustrated in the following example:

ant -Drpt=BarChartReportDemo -Dclass=PdfExportDemo export

Substitute the value of the class property to match the class filename to execute.