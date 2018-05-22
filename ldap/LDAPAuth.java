package ldap;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.ldap.JndiLdapContextFactory;
import org.apache.shiro.realm.ldap.JndiLdapRealm;
import org.apache.shiro.subject.Subject;

import javax.naming.NameClassPair;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

public class LDAPAuth extends Authenticator {
    protected final JndiLdapRealm realm;
    private final JndiLdapContextFactory factory;
    protected final AuthenticatorSettings settings;

    protected LDAPAuth(AuthenticatorSettings settings) {
        this.settings = settings;

        //Create the realm
        realm = new JndiLdapRealm();

        //Create the context factory
        factory = new JndiLdapContextFactory();

        //Give the context factory to the realm
        realm.setContextFactory(factory);

        //Set the security manager
        SecurityManager securityManager = new DefaultSecurityManager(realm);
        SecurityUtils.setSecurityManager(securityManager);
    }

    @Override
    /**
     * Do the login process to ldap
     * @param username - The user id
     * @param password - The user password
     */
    public AuthenticationStatus doLogin(String username, char[] password) {

        //Block empty password
        if (password.length == 0) {
            return AuthenticationStatus.BAD_PASSWORD;
        }

        setupConnection();
        //Sub tree login doesn't use the template (it will provide a full DN)
        realm.setUserDnTemplate("{0}");
        return subtreeLogin(username, password);
    }

    /**
     * Set the parameters used by the authenticator
     * to make a complete connection to the LDAP
     * server
     */
    protected void setupConnection() {
        // Context settings
        factory.setAuthenticationMechanism(settings.getAuthenticationMethod().getKey());

        // If the user has set a realm, we use it, otherwise fallback to "any realm mode"
        if (settings.getRealm() != null) {
            if (!settings.getRealm().trim().isEmpty()) {
                Map<Object, Object> env = factory.getEnvironment();
                env.put("java.naming.security.sasl.realm", settings.getRealm());
                factory.setEnvironment(env);
            }
        }

        factory.setUrl(settings.getLdapUrl());
    }

    /**
     * Set the system account parameters used by the
     * authenticator to get an administrative view
     * of the server
     */
    protected void setupSystemConnection() {
        realm.setUserDnTemplate("{0}");
        //Set the admin (system) account credidentials 
        factory.setSystemUsername(settings.getAdminLogin());
        factory.setSystemPassword(settings.getAdminPassword());
    }

    /**
     * Search users in the branches under search base
     * Try login against each user found until one matches
     *
     * @param username
     * @param password
     * @return
     */
    private AuthenticationStatus subtreeLogin(String username, char[] password) {
        setupSystemConnection();

        try {
            //Get the system context
            LdapContext c = realm.getContextFactory().getSystemLdapContext();

            //Search for a user with specified username
            String searchString = "(&(objectclass=" + settings.getUserClass() + ")(" + settings.getUserIdAttribute()
                    + "=" + username + "))";
            //Get a list of users found
            ArrayList<String> usersFound = new ArrayList<String>();
            try {
                Enumeration<SearchResult> e = c.search(settings.getUserBaseDn(), searchString, new SearchControls(
                        SearchControls.SUBTREE_SCOPE, 0l, 0, null, false, false));
                while (e.hasMoreElements()) {
                    NameClassPair w = e.nextElement();
                    usersFound.add(w.getNameInNamespace());
                }
            } catch (NamingException e1) {
            }

            if (usersFound.size() == 0) {
                //No user with that username
                return AuthenticationStatus.UNKNOWN_ID;
            } else {
                //Try each found user in turn
                for (String userDN : usersFound) {
                    if (simpleLogin(userDN, password).equals(AuthenticationStatus.LOGIN_SUCCESS)) {
                        //We found one correct user/password match
                        return AuthenticationStatus.LOGIN_SUCCESS;
                    }
                }
            }
            //Password didn't match any of the users
            return AuthenticationStatus.BAD_PASSWORD;
        } catch (Exception e2) {
            return AuthenticationStatus.parseException(e2).asSystemAccount();
        }
    }

    /**
     * Log in users directly under the search base DN
     *
     * @param username
     * @param password
     * @return
     */
    private AuthenticationStatus simpleLogin(String username, char[] password) {

        //Create a new user
        /*
         * Warning: Do not use SecurityUtils.getSubject()!
         *
         * It caches the resulting Subject using a ThreadLocal.
         * Subject contains a SecurityManager, and the cached copy will not be
         * updated when we recreate LDAPAuthenticator after settings have changed.
         * Even though we call SecurityUtils.setSecurityManager(securityManager),
         * the cached Subject's SecurityManager is not updated, so stale information
         * continues to be used.
         */
        Subject currentUser = (new Subject.Builder()).buildSubject();

        //Create a token
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        //Use shiro's cache feature
        token.setRememberMe(true);

        AuthenticationStatus status = null;
        //Try to log in
        try {
            currentUser.login(token);
        } catch (Exception e) {
            status = AuthenticationStatus.parseException(e);
        }

        if (status == null) {
            if (currentUser.isAuthenticated()) {
                status = AuthenticationStatus.LOGIN_SUCCESS;
            } else {
                status = AuthenticationStatus.LOGIN_FAILURE;
            }
        }

        //Clear password
        clearPassword(password);
        //Logout the user
        currentUser.logout();
        //Return the status
        return status;
    }

    private void clearPassword(char[] password) {
        for (int i = 0; i < password.length; i++) {
            password[i] = '\0';
        }
    }

    /**
     * Returns the LdapContext attached to the current system context
     * Will raise Naming exception if it is not able to get that context
     *
     * @return
     * @throws NamingException
     */
    @Override
    public LdapContext getSystemLdapContext() throws NamingException {
        setupConnection();
        setupSystemConnection();
        LdapContext systemContext = realm.getContextFactory().getSystemLdapContext();
        return systemContext;
    }
}
