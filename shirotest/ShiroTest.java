package shirotest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiroTest {
    private static Logger logger = LoggerFactory.getLogger(ShiroTest.class);

    public static void main(String[] args) {

        // Using the IniSecurityManagerFactory, which will use the an INI file
        // as the security file.
        Factory<SecurityManager> factory =
                new IniSecurityManagerFactory("auth.ini");

        // Setting up the SecurityManager...
        org.apache.shiro.mgt.SecurityManager securityManager
                = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        Subject user = SecurityUtils.getSubject();

        logger.info("User is authenticated:  " + user.isAuthenticated());

        UsernamePasswordToken token = new UsernamePasswordToken("thetsu", "ovation");

        user.login(token);
        logger.info("[After login] User is authenticated:  " + user.isAuthenticated());

        user.logout();
        logger.info("[After logout] User is authenticated:  " + user.isAuthenticated());

    }
}
