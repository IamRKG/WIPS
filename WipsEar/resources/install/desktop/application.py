################################################################################
##                         IMPORT BASE PY FILE
## Import other settings from applicationbase.py 
## See the resources/install/common folder for this file.
## Note for the desktop we need to fully qualify this file. 
################################################################################
################################################################################
##from Settings import Settings as Config
##execfile('C:\Projects\accurev\WIPSWeb\WIPSWeb_WMMobile_DEV_REFACTOR\WipsEar\resources\install\common\applicationbase.py' )
##execfile('C:\Projects\accurev\WIPSWeb\WIPSWeb_WMMobile_DEV_REFACTOR\WipsEar\resources\install\desktop\dynapropcfgSymlink.py' )

##############################################################################
##                           NONSTANDARD_CLASSPATHS
##  Append additional properties specific to this environment
##############################################################################

NONSTANDARD_CLASSPATHS = []
NONSTANDARD_CLASSPATHS.append("C:\Projects\accurev\WIPSWeb\WIPSWeb_WMMobile_DEV\WipsProperties\properties")

##############################################################################
##                           SYSTEM_PROPERTIES
##  Append additional properties specific to this environment
##############################################################################

SYSTEM_PROPERTIES = []
##SYSTEM_PROPERTIES.append(["com.ford.websphere.auth.FordLoginModule.enable", "true"])
##SYSTEM_PROPERTIES.append(["connector-config", "/connector-config.xml"])

CUSTOM_WAR_SETTINGS = []
CUSTOM_WAR_SETTINGS.append([["earName WipsEAR", "warURI WipsWeb.war"], ["virtualHost", DEFAULT_VIRTUAL_HOST], ["classloaderMode", "PARENT_LAST"]])

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
TRACE_LOG_SETTINGS.append(["maxNumberOfBackupFiles", 2])
TRACE_LOG_SETTINGS.append(["rolloverSize", 10])

USE_DYNAPROP = "TRUE"


################################################################################
##     WIPS DB 
################################################################################

DB2_DB_Definition_1 = []
DB2_DB_Definition_1.append (["name", "DBCONN"])
DB2_DB_Definition_1.append (["driver", "DB2_TYPE4"])
DB2_DB_Definition_1.append (["jndiName", "jdbc/DBCONN"])
DB2_DB_Definition_1.append (["databaseName", "NEDE1"])
DB2_DB_Definition_1.append (["serverName", "www.neb1.ford.com"])
DB2_DB_Definition_1.append (["portNumber", 5027])
DB2_DB_Definition_1.append (["maxConnections", 500])
DB2_DB_Definition_1.append (["minConnections", 50])
DB2_DB_Definition_1.append (["progressiveStreaming", 2])

################################################################################
##   DynaProp Database 
################################################################################
DB2_DB_Definition_Dynaprop = []
DB2_DB_Definition_Dynaprop.append(["name", "DynaPropDB"])
DB2_DB_Definition_Dynaprop.append(["driver", "DB2_TYPE4"])
DB2_DB_Definition_Dynaprop.append(["jndiName", "jdbc/fjf/DynaPropJDBC"])
DB2_DB_Definition_Dynaprop.append(["databaseName", "NEDX1"])
DB2_DB_Definition_Dynaprop.append(["serverName", "www.neb1.ford.com"])
DB2_DB_Definition_Dynaprop.append(["portNumber", 446])
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

DATA_SOURCE_DEFINITIONS = [DB2_DB_Definition_Dynaprop, DB2_DB_Definition_1]

################################################################################
## Set IMS Settings & Configuration
################################################################################

USE_EAA_SECURITY = "TRUE"
USE_IMS_CONNECTOR = "TRUE"

IMS_Definition_TB = []
IMS_Definition_TB.append(["name", "IMS_TB"])
IMS_Definition_TB.append(["jndiName", "eis/ims/RT/IMSTB"])
IMS_Definition_TB.append(["DataStoreName", "IMSTB"])
IMS_Definition_TB.append(["maxConnections", 50])
IMS_Definition_TB.append(["minConnections", 0])

IMS_Definition_EB = []
IMS_Definition_EB.append(["name", "IMS_EB"])
IMS_Definition_EB.append(["jndiName", "eis/ims/RT/IMSEB"])
IMS_Definition_EB.append(["DataStoreName", "IMSEB"])
IMS_Definition_EB.append(["maxConnections", 50])
IMS_Definition_EB.append(["minConnections", 0])

IMS_CONNECTION_FACTORY_DEFINITIONS = [IMS_Definition_TB, IMS_Definition_EB]
################################################################################




