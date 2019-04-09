##############################################################################
# Rather than have each developer copy the dynaprop.cfg file to their 
# c:\proj\<serverName>\was\fjf folder whenever the dynaprop password changes, 
# we set up a symlink to it. This allows one copy of the dynaprop.cfg file
# to be checked into AccuRev that all the developers localhost setup points to. 
#
# The location of the physical dynaprop.cfg file should be in the 
# same directory as the application.py (typically AppEAR/resources/install/desktop.
#
# Note: A symlink set-up is only valid if the DEPLOYED_ENVIRONMENT is set to 
# DESKTOP.  So this script will bypass setting up the symlink for any other 
# DEPLOYED_ENVIRONMENT setting.
##############################################################################

import os
import sys
from java.lang import Runtime

if DEPLOYED_ENVIRONMENT == 'DESKTOP':

    DYNAPROP_CFG_PHYSICAL = os.path.normpath('%s/dynaprop.cfg' % (APPLICATION_DEPLOY_SOURCE))
    DYNAPROP_CFG_SYMLINK = os.path.normpath(DYNA_PROP['filename'])
    if os.path.exists(DYNAPROP_CFG_PHYSICAL):
        DYNAPROP_CFG_SYMLINK_DIR = os.path.dirname(DYNAPROP_CFG_SYMLINK)
        if not os.path.exists(DYNAPROP_CFG_SYMLINK_DIR):
            print "Making %s" % (DYNAPROP_CFG_SYMLINK_DIR)
            os.mkdir(DYNAPROP_CFG_SYMLINK_DIR)
        else:
            print "Already got %s" % (DYNAPROP_CFG_SYMLINK_DIR)
        print 'Making symlink from %s to %s' % (DYNAPROP_CFG_SYMLINK, DYNAPROP_CFG_PHYSICAL)
        if os.path.exists(DYNAPROP_CFG_SYMLINK):
            os.remove(DYNAPROP_CFG_SYMLINK)
        # os.system is borked on this ancient version of Jython :-(
        # http://stackoverflow.com/questions/4652512/running-external-commands-with-control-over-the-input-output-in-jython-2-1
        process = Runtime.getRuntime().exec('cmd /c mklink "%s" "%s"' % (DYNAPROP_CFG_SYMLINK, DYNAPROP_CFG_PHYSICAL))
        process.waitFor();
        mklinkExitValue = process.exitValue()
        if mklinkExitValue != 0:
            sys.exit('Exit value from mklink was %d - you must run the DSC as Administrator' % (mklinkExitValue))
    else:
        print 'Warning: No sign of %s' % (DYNAPROP_CFG_PHYSICAL)
    
else: 
     print 'Warning: Ignoring DynaPropSymLink setup since not DESKTOP environment. Current environment is %s' % (DEPLOYED_ENVIRONMENT)
