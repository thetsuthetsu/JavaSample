package ldap;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import java.sql.Connection;
import java.sql.SQLException;
public abstract class Authenticator {

    public AuthenticationStatus login(Connection con, String username, char[] password)
            throws SQLException {

        //Do the actual login
        return doLogin(username, password);
    }

    /**
     * Authenticator dependent log in process
     *
     * @param username  the user name provided by the user
     * @param password  the password provided by the user
     */
    protected abstract AuthenticationStatus doLogin(String username, char[] password);

    /**
     * Returns the LdapContext attached to the current system context
     * Will raise Naming exception if it is not able to get that context
     *
     * @return
     * @throws NamingException
     */
    public abstract LdapContext getSystemLdapContext() throws NamingException;
}
