################################################################################
##                         IMPORT BASE PY FILE
## Import other settings from applicationbase.py 
## See the resources/install/common folder for this file.
## Note for the desktop we need to fully qualify this file. 
################################################################################
################################################################################
##from Settings import Settings as Config
##execfile( Config.APPLICATION_DEPLOY_SOURCE + '/applicationbase.py' )

##############################################################################
##                           NONSTANDARD_CLASSPATHS
##  Append additional properties specific to this environment
##############################################################################

NONSTANDARD_CLASSPATHS = []

##############################################################################
##                           SYSTEM_PROPERTIES
##  Append additional properties specific to this environment
##############################################################################

SYSTEM_PROPERTIES = []
##SYSTEM_PROPERTIES.append(["com.ford.websphere.auth.FordLoginModule.enable", "true"])
##SYSTEM_PROPERTIES.append(["connector-config", "/connector-config.xml"])

##CUSTOM_WAR_SETTINGS = []
##CUSTOM_WAR_SETTINGS.append([["earName WipsEar", "warURI WipsWeb.war"], ["virtualHost", DEFAULT_VIRTUAL_HOST], ["classloaderMode", "PARENT_LAST"]])

STDOUT_LOG_SETTINGS = []
STDOUT_LOG_SETTINGS.append(["rolloverType", "SIZE"])
STDOUT_LOG_SETTINGS.append(["rolloverSize", 10])
STDOUT_LOG_SETTINGS.append(["maxNumberOfBackupFiles", 3])

STDERR_LOG_SETTINGS = []
STDERR_LOG_SETTINGS.append(["rolloverType", "SIZE"])
STDERR_LOG_SETTINGS.append(["rolloverSize", 10])
STDERR_LOG_SETTINGS.append(["maxNumberOfBackupFiles", 3])

TRACE_LOG_SETTINGS = []
TRACE_LOG_SETTINGS.append(["startupTraceSpecification", "com.ford.*=all"])
TRACE_LOG_SETTINGS.append(["maxNumberOfBackupFiles", 3])
TRACE_LOG_SETTINGS.append(["rolloverSize", 20])

USE_DYNAPROP = "TRUE"


################################################################################
##     WIPS DB 
################################################################################

DB2_DB_Definition_WipsDB = []
DB2_DB_Definition_WipsDB.append (["name", "DBCONN"])
DB2_DB_Definition_WipsDB.append (["driver", "DB2_TYPE4"])
DB2_DB_Definition_WipsDB.append (["jndiName", "jdbc/DBCONN"])
DB2_DB_Definition_WipsDB.append (["databaseName", "NEDY1"])
DB2_DB_Definition_WipsDB.append (["serverName", "www.nea1.ford.com"])
DB2_DB_Definition_WipsDB.append (["portNumber", 5021])
DB2_DB_Definition_WipsDB.append (["maxConnections", 50])
DB2_DB_Definition_WipsDB.append (["minConnections", 1])
DB2_DB_Definition_WipsDB.append (["progressiveStreaming", 2])

################################################################################
##   DynaProp Database 
################################################################################
DB2_DB_Definition_Dynaprop = []
DB2_DB_Definition_Dynaprop.append(["name", "DynaPropDB"])
DB2_DB_Definition_Dynaprop.append(["driver", "DB2_TYPE4"])
DB2_DB_Definition_Dynaprop.append(["jndiName", "jdbc/fjf/DynaPropJDBC"])
DB2_DB_Definition_Dynaprop.append(["databaseName", "NEDY1"])
DB2_DB_Definition_Dynaprop.append(["serverName", "www.nea1.ford.com"])
DB2_DB_Definition_Dynaprop.append(["portNumber", 5021])
DB2_DB_Definition_Dynaprop.append(["agedTimeout", 1200])
DB2_DB_Definition_Dynaprop.append(["reapTime", 300])
DB2_DB_Definition_Dynaprop.append(["unusedTimeout", 1800])
DB2_DB_Definition_Dynaprop.append(["connectionTimeout", 180])
DB2_DB_Definition_Dynaprop.append(["maxConnections", 50])
DB2_DB_Definition_Dynaprop.append(["minConnections", 1])
DB2_DB_Definition_Dynaprop.append(["statementCacheSize", 50])

################################################################################
##Set DATA_SOURCE_DEFINITIONS variable here
################################################################################

DATA_SOURCE_DEFINITIONS = [DB2_DB_Definition_WipsDB, DB2_DB_Definition_Dynaprop]

################################################################################
## Set IMS Settings & Configuration
################################################################################

USE_EAA_SECURITY = "TRUE"
USE_IMS_CONNECTOR = "TRUE"

IMS_Definition_PROD = []    
IMS_Definition_PROD.append(["name", "IMS"])
IMS_Definition_PROD.append(["jndiName", "eis/ims/RT/IMSB"])
IMS_Definition_PROD.append(["DataStoreName", "IMSB"])
IMS_Definition_PROD.append(["maxConnections", 50])
IMS_Definition_PROD.append(["minConnections", 0])

IMS_CONNECTION_FACTORY_DEFINITIONS = [IMS_Definition_PROD]

################################################################################



