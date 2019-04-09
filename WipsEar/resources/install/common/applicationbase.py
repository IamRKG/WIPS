##############################################################################
##                           SYSTEM_PROPERTIES
## Definition: By setting this variable, a -D parameter will be added to
##             the JVM command line of the application server.
##

SYSTEM_PROPERTIES = []
#Enable eclipseLink to use WebSphere's XML Parser 
SYSTEM_PROPERTIES.append(["eclipse.persistence.xml.platform", "org.eclipse.persistence.platform.xml.jaxp.JAXPPlatform"])
#Allow the FordLoginModule to access FDS for additional user attributes
SYSTEM_PROPERTIES.append(["com.ford.websphere.auth.FordLoginModule.enable", "true"])
#Avoid the APS Out of Memory problem
SYSTEM_PROPERTIES.append(["jaxws.share.dynamic.ports.enable", "true"])
#Avoid the ehcache temporary jar was.policy permissions issue.
SYSTEM_PROPERTIES.append(["net.sf.ehcache.pool.sizeof.AgentSizeOf.bypass", "true"])
SYSTEM_PROPERTIES.append(["com.ford.it.logging.CustomLogger", "com.ford.it.logging.Log4j2Mapper"])
SYSTEM_PROPERTIES.append(["com.ford.it.logging.subpath", "was/logs"])
SYSTEM_PROPERTIES.append(["log4j2.disable.jmx", "true"])

##############################################################################

################################################################################
##                   Dynamic Property Manager (DynaProp)
## Definition:  Set this variable to TRUE if your application uses the 
##              Dynamic Property Manager

USE_DYNAPROP = "TRUE"
################################################################################

##############################################################################
##                           STDOUT_LOG_SETTINGS
## Definition: The setting  defines how WebSphere will manage the
##             standard out log file.
##  NOTE:      In the example below, WebSphere will limit the size of the
##             standard out log file to 10MB.  WebSphere will keep 3 
##             history files along with the active file. So in this case the
##             standard out log files will take at most 40MB of disk space.
##             10MB for the active file and 10MB for each of 3 history files.

STDOUT_LOG_SETTINGS = []
STDOUT_LOG_SETTINGS.append(["rolloverType", "SIZE"])
STDOUT_LOG_SETTINGS.append(["rolloverSize", 10])
STDOUT_LOG_SETTINGS.append(["maxNumberOfBackupFiles", 3])
##############################################################################

################################################################################
##                           WebSphere Trace Logging
## Definition:  If the application needs to enable WebSphere tracing, set the
##              values below. If not overridden, trace will be off by default.
##  NOTE:      In the example below, WebSphere will limit the size of the
##             trace log file to 10MB.  WebSphere will keep 3 
##             history files along with the active file. So in this case the
##             trace log files will take at most 40MB of disk space.
##             10MB for the active file and 10MB for each of 3 history files.
##             The trace file will be available in HTEAM under the
##             "Manage Active Logs" section. The name of the log file will be
##              <app_instance>_trace.log.
##
## Instructions of how to format the startupTraceSpecification string:
## For WebSphere Application Server, V6 and later, a new trace specification
## grammar has been added to better represent the underlying infrastructure:
##
##     LOGGINGSTRING=COMPONENT_LOGGING_STRING[:COMPONENT_LOGGING_STRING]*
##
##     COMPONENT_LOGGING_STRING=COMPONENT_NAME=LEVEL
##
##     LEVEL = all | (finest | debug) | (finer | entryExit) | (fine | event )
##     | detail | config | info | audit | warning | (severe | error) | fatal | off
##
##     COMPONENT_NAME = COMPONENT | GROUP
##
##     Examples:
##       ["startupTraceSpecification", "com.ford.fjftest.*=all"]
##       ["startupTraceSpecification", "com.ford.fjftest.*=all:com.ibm.ws.websvcs.trace.*=debug"]
##
## For more details on trace configuration please reference:
## http://pic.dhe.ibm.com/infocenter/wasinfo/v8r0/index.jsp?topic=%2Fcom.ibm.websphere.nd.multiplatform.doc%2Finfo%2Fae%2Fae%2Frtrb_enabletrc.html
##
TRACE_LOG_SETTINGS = []
TRACE_LOG_SETTINGS.append(["startupTraceSpecification", "*=INFO"])
TRACE_LOG_SETTINGS.append(["maxNumberOfBackupFiles", 3])
TRACE_LOG_SETTINGS.append(["rolloverSize", 20])
################################################################################

################################################################################
## This section defines the shared attributes that are common for all 3 
## DataSources that WIPS defines: WIPS DB, DynaProp DB & DataMaintenance DB
##
## Info for WebSphere datasource connection pool settings can be found at:
## http://pic.dhe.ibm.com/infocenter/wasinfo/v8r0/topic/com.ibm.websphere.nd.multiplatform.doc/info/ae/ae/udat_conpoolset.html
## 
## Info for Microsoft SqlServer driver connection settings can be found at:
## http://msdn.microsoft.com/en-us/library/ms378988.aspx
##
## Info for webSphereDefaultIsolationLevel can be found at:
## http://pic.dhe.ibm.com/infocenter/wasinfo/v8r0/index.jsp?topic=%2Fcom.ibm.websphere.nd.multiplatform.doc%2Finfo%2Fae%2Fae%2Fcdat_isolevel.html
##
################################################################################
WIPS_SQLServer_Shared = []
WIPS_SQLServer_Shared.append(["driver", "MSSQL_4_02206"])
WIPS_SQLServer_Shared.append(["enable2Phase", "false"])
WIPS_SQLServer_Shared.append(["agedTimeout", 1200])
WIPS_SQLServer_Shared.append(["reapTime", 300])
WIPS_SQLServer_Shared.append(["unusedTimeout", 1800])
WIPS_SQLServer_Shared.append(["connectionTimeout", 180])
WIPS_SQLServer_Shared.append(["maxConnections", 50])
WIPS_SQLServer_Shared.append(["minConnections", 1])
WIPS_SQLServer_Shared.append(["purgePolicy", "EntirePool"])
WIPS_SQLServer_Shared.append(["selectMethod", "cursor"])
WIPS_SQLServer_Shared.append(["statementCacheSize", 50])

##### Additional parameters for SQL Server ###
# The number of milliseconds to wait before the database reports a lock time-out.  
WIPS_SQLServer_Shared.append(["lockTimeout", 30])

# The number of seconds the driver should wait before timing out a failed
# connection. A zero value indicates that the timeout is the default system
# timeout, which is specified as 15 seconds.
WIPS_SQLServer_Shared.append(["loginTimeout", 0])

# The network packet size used to communicate with SQL Server, specified in bytes.
# A value of -1 indicates using the server default packet size. The default packetSize 8000.
WIPS_SQLServer_Shared.append(["packetSize", 4096])

# If this property is set to "adaptive", the minimum possible data is buffered when necessary.
# The default mode is "adaptive". When this property is set to "full", 
# the entire result set is read from the server when a statement is executed.
WIPS_SQLServer_Shared.append(["responseBuffering", "adaptive"])

# If the sendStringParametersAsUnicode property is set to "true", 
# String parameters are sent to the server in Unicode format.
# If the sendStringParametersAsUnicode property is set to “false", 
# String parameters are sent to the server in non-Unicode format such as ASCII/MBCS instead of Unicode. 
# The default value for the sendStringParametersAsUnicode property is "true".
WIPS_SQLServer_Shared.append(["sendStringParametersAsUnicode", "false"])

## The value 2 represents a JDBC isolation level of TRANSACTION_READ_COMMITTED
## The default value is 4 that represents TRANSACTION_REPEATABLE_READ 
WIPS_SQLServer_Shared.append(["webSphereDefaultIsolationLevel", 2])

# connectionSharing is a combination of bits representing which connection 
# properties to match based on the current state of the connection. 
# A value of 0 means to match all properties based on the original connection request;
# a value of -1 means to match all properties based on the current state of the connection.
# The default value is 1, which means that the isolation level is matched based on the
# current state of the connection and all other properties are matched based on
# the original connection request.
WIPS_SQLServer_Shared.append(["connectionSharing", 1])

################################################################################
##                          WIPS DB
## First include the shared attributes, then append WIPS DB specific attributes
################################################################################
SQLServer_DB_WIPS = WIPS_SQLServer_Shared[:]
SQLServer_DB_WIPS.append(["name", "WIPS_DB"])
SQLServer_DB_WIPS.append(["jndiName", "jdbc/WIPS_DB"])

################################################################################
##                       DynaProp Database 
## First include the shared attributes, then append Dynaprop DB specific attributes
################################################################################
SQLServer_DB_DynaProp = WIPS_SQLServer_Shared[:]
SQLServer_DB_DynaProp.append(["name", "dynaprop"])
SQLServer_DB_DynaProp.append(["jndiName", "jdbc/fjf/DynaPropJDBC"])

################################################################################
##                    DataMaintenance Database 
## First include the shared attributes, then append DataMaintenance DB specific attributes
################################################################################
SQLServer_DB_DataMaintenance = WIPS_SQLServer_Shared[:]
SQLServer_DB_DataMaintenance.append(["name", "DataMaintenanceDB"])
SQLServer_DB_DataMaintenance.append(["jndiName", "jdbc/fjf/DataMaintenanceDB"])

## DATA_SOURCE_DEFINITIONS - These are set in the environment specific py file
################################################################################