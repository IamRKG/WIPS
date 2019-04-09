package com.ford.purchasing.wips.inbound.filter;

import java.io.IOException;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;
import com.ford.purchasing.wips.inbound.layer.common.UserSessionCacheManager;

/**
 * This class validates the user session for all the requests except for the Login
 * request, if the request is invalid then return the HTTP status code as 401 - UnAuthorized.
 */
public class WipsAuthorizationFilter implements javax.servlet.Filter {

    private static final String CLASS_NAME = "WipsAuthorizationFilter";
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    public static final String AUTHENTICATION_HEADER = "Authorization";
    private static final String WIPS_NEW_LOGIN = "Login";
    private static final String LTERM = "lterm";

    @Inject
    @Default
    private UserSessionCacheManager userSessionCache;

    @Override
    public void init(final FilterConfig arg0) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
            final FilterChain filter) throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        final String authorizationToken = httpServletRequest
                .getHeader(AUTHENTICATION_HEADER);
        final String ltermToken = httpServletRequest.getParameter(LTERM);
        final HttpServletResponse httpServletResponse =
                (HttpServletResponse)response;
        final String lterm = authorizationToken != null ? authorizationToken : ltermToken;
        if (lterm != null) {
            if (lterm.equalsIgnoreCase(WIPS_NEW_LOGIN)) {
                filter.doFilter(request, response);
            } else {
                if (validateUserSession(lterm)) {
                    filter.doFilter(request, response);
                } else {
                    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        } else {
            log.info(
                    "Bad Request found for the URL -->" + httpServletRequest.getRequestURL()
                     + httpServletRequest.getQueryString());
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public void destroy() {
    }

    protected boolean validateUserSession(final String lterm) {
        return this.userSessionCache.isValidUserSession(WipsUtil.decrpyt(lterm));
    }

}
