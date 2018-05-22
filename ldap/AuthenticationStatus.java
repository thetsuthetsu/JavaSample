package ldap;

import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.AuthenticationException;

import javax.naming.CommunicationException;
import javax.security.sasl.SaslException;
import java.io.IOException;
import java.net.ConnectException;

public enum AuthenticationStatus {
    UNKNOWN_ID("User ID is unknown"), //
    SYSTEM_UNKNOWN_ID("System account ID is unknown"), //
    //
    BAD_PASSWORD("User password does not match user ID"), //
    SYSTEM_BAD_PASSWORD("System account password does not match system account ID"), //
    //
    LOCKED_ACCOUNT("User's account is locked"), //
    SYSTEM_LOCKED_ACCOUNT("System account is locked"), //
    //
    EXCESSIVE_ATTEMPTS("User has failed too many times to connect"), //
    SYSTEM_EXCESSIVE_ATTEMPTS("System account has failed too many times to connect"), //
    //
    CONNECTION_ERROR("Error connecting to server"), //
    INVALID_REALM("SASL Realm is invalid"), //
    //
    LOGIN_SUCCESS("User successfuly logged in"), //
    SYSTEM_LOGIN_SUCCESS("System account successfuly logged in"), //
    //
    LOGIN_FAILURE("User did not log in"), //
    SYSTEM_LOGIN_FAILURE("System account did not log in"), //
    //
    INVALID_CREDENTIALS("User ID or password is incorrect"), //
    SYSTEM_INVALID_CREDENTIALS("System account ID or password is incorrect"), //

    INTERNAL_ERROR("An error occured in Console Server");//

    private final String details;

    AuthenticationStatus(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public static AuthenticationStatus parseException(Exception except) {
        if (except instanceof ConnectException) {
            return CONNECTION_ERROR;
        }
        if (except instanceof CommunicationException) {
            return CONNECTION_ERROR;
        }

        if (except instanceof UnknownAccountException) {
            return UNKNOWN_ID;
        }
        if (except instanceof IncorrectCredentialsException) {
            return BAD_PASSWORD;
        }
        if (except instanceof LockedAccountException) {
            return LOCKED_ACCOUNT;
        }
        if (except instanceof ExcessiveAttemptsException) {
            return EXCESSIVE_ATTEMPTS;
        }
        if (except instanceof AuthenticationException //
                || except instanceof javax.naming.AuthenticationException //
                || except instanceof javax.security.sasl.AuthenticationException) {

            if (except.getCause() != null) {
                if (except.getCause() instanceof NumberFormatException) {
                    return CONNECTION_ERROR;
                }
                if (except.getCause() instanceof CommunicationException) {
                    return CONNECTION_ERROR;
                }
                if (except.getCause().getCause() != null) {
                    if (except.getCause().getCause() instanceof ConnectException) {
                        return CONNECTION_ERROR;
                    }
                }
                if (except.getCause().getMessage().startsWith("[LDAP: error code ")) {
                    return INVALID_CREDENTIALS;
                }
                if (except.getCause() instanceof SaslException) {
                    if (except.getCause().getCause() != null) {
                        if (except.getCause().getCause() instanceof IOException) {
                            if (except.getCause().getCause().getMessage().endsWith(",in RealmChoiceCallback")) {
                                return INVALID_REALM;
                                /*System.out.println("Valid realms : "
                                        + except.getCause().getCause().getMessage().split("with choices")[1]
                                                .split(",in RealmChoiceCallback")[0]);*/
                            }
                        }
                    }
                    return CONNECTION_ERROR;
                }
            }
            return INVALID_CREDENTIALS;
        }
        except.printStackTrace();
        return LOGIN_FAILURE;
    }

    public AuthenticationStatus asSystemAccount() {
        switch (this) {
            case BAD_PASSWORD :
                return SYSTEM_BAD_PASSWORD;
            case CONNECTION_ERROR :
                return CONNECTION_ERROR;
            case EXCESSIVE_ATTEMPTS :
                return SYSTEM_EXCESSIVE_ATTEMPTS;
            case INVALID_CREDENTIALS :
                return SYSTEM_INVALID_CREDENTIALS;
            case LOCKED_ACCOUNT :
                return SYSTEM_LOCKED_ACCOUNT;
            case LOGIN_FAILURE :
                return SYSTEM_LOGIN_FAILURE;
            case LOGIN_SUCCESS :
                return SYSTEM_LOGIN_SUCCESS;
            case UNKNOWN_ID :
                return SYSTEM_UNKNOWN_ID;
            default :
                return this;
        }
    }
}
