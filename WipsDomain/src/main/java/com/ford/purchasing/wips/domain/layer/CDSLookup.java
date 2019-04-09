package com.ford.purchasing.wips.domain.layer;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchResult;

import com.ford.it.ldap.LDAPLookup;
import com.ford.it.ldap.LDAPLookupException;
import com.ford.it.ldap.LDAPLookupFactory;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.it.properties.PropertyException;
import com.ford.it.properties.PropertyManager;
import com.ford.purchasing.wips.common.layer.WipsConstant;

@SuppressWarnings("javadoc")
public class CDSLookup {
    private static final String CLASS_NAME = CDSLookup.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);
    private static final String GROUP_ID = "WIPSMobile.Ldap.ApplicationGroupId";
    private static final String APP_PWD = "WIPSMobile.Ldap.ApplicationPassword";

    public String getAttributesFromLdapByCdsid(final String cdsId)
            throws LDAPLookupException, NamingException {
    	final String methodName = "getAttributesFromLdapByCdsid";
        String mobileNumber = "";
        LDAPLookup fds = null;
        final LDAPLookupFactory ldapFactory = LDAPLookupFactory.getInstance();
        NamingEnumeration<SearchResult> results = null;
        try {
            fds = ldapFactory.createFDSLookup(
                    PropertyManager.getInstance().getString(GROUP_ID),
                    PropertyManager.getInstance().getString(APP_PWD));
            fds.setAttributes(WipsConstant.getCdsLookupAttributes());
            fds.setSearchCriteria(
                    WipsConstant.LDAP_SEARCH_PREFIX + cdsId
                                  + WipsConstant.CLOSING_BRACKET);
            results = fds.search();

            while (results.hasMore()) {
                final SearchResult resultRecord = results.next();
                mobileNumber = findValueInResults(resultRecord);
            }

        } catch (final PropertyException e) {
        	log.throwing(CLASS_NAME, methodName, e);
            throw new PropertyException();
        } catch (final LDAPLookupException e) {
        	log.throwing(CLASS_NAME, methodName, e);
            throw new LDAPLookupException();
        } catch (final NamingException e) {
        	log.throwing(CLASS_NAME, methodName, e);
            throw new NamingException();
        } finally {
            if (results != null) {
                results.close();
                results = null;
            }
            if (fds != null) {
                fds.closeConnection();
            }
        }
        return mobileNumber;
    }

    private static String findValueInResults(final SearchResult resultRecord)
            throws NamingException {
        final NamingEnumeration<Attribute> attributes =
                (NamingEnumeration<Attribute>)resultRecord.getAttributes()
                        .getAll();
        while (attributes.hasMore()) {
            final Attribute attr = attributes.next();
            final NamingEnumeration<?> values = attr.getAll();
            while (values != null && values.hasMore()) {
                return (String)values.next();
            }
        }
        return null;
    }
}