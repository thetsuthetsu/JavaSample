package ldap;

import java.io.Serializable;

public class AuthenticatorSettings implements Serializable {
    private String userIdAttribute = "uid";
    private String userBaseDn = "ou=People";
    private AuthenticationMethod authenticationMethod = AuthenticationMethod.SIMPLE;
    private String protocol = "ldap";
    private String ldapHostname = "";
    private int ldapPort = 389;

    private String adminLogin = "";
    private String adminPassword = "";
    private String userClass = "person";
    private boolean useSubtree = false;
    private boolean generateTemplate = true;
    private String realm = "";
    private String userRemarkAttribute = "";

    //Groups
    private String groupClass = "";
    private String groupRemarkAttribute = "";
    private String groupBaseDn = "";
    private String groupIdAttribute = "";
    private String groupUserListAttribute = "";
    private String userGroupAttribute = "";
    private boolean useSync = false;

    //Kerberos
    private String kerberosHostname = "";

    /**
     * Empty constructor <br>
     * Unsafe : make sure you have set all fields before using
     */
    public AuthenticatorSettings() {
    }

    /**
     * Get the full URL to the server <br>
     * like : ldap://ldap.server.com:389
     * @return
     */
    public String getLdapUrl() {
        return protocol + "://" + ldapHostname + ":" + ldapPort;
    }

    /**
     * Returns the authentication method
     * @return
     */
    public AuthenticationMethod getAuthenticationMethod() {
        return authenticationMethod;
    }

    public void setAuthenticationMethod(AuthenticationMethod authenticationMethod) {
        this.authenticationMethod = authenticationMethod;
    }

    public static final String TEMPLATE_USERDN_PLACE_HOLDER = "{userDNpart}";
    public static final String TEMPLATE_GROUPDN_PLACE_HOLDER = "{groupDNpart}";

    /**
     * Returns the template created from the userIdAttribute and userBaseDn
     * fields
     * @return
     */
    public String getTemplate(String userDnTemplate, String groupDnTemplate) {
        String template = userIdAttribute + "={0}," + userBaseDn;

        template = template.replace(TEMPLATE_USERDN_PLACE_HOLDER, userDnTemplate);
        template = template.replace(TEMPLATE_GROUPDN_PLACE_HOLDER, groupDnTemplate);

        return template;
    }

    /**
     * The LDAP server hostname
     * @return
     */
    public String getLdapHostname() {
        return ldapHostname;
    }

    public void setLdapHostname(String ldapHostname) {
        this.ldapHostname = ldapHostname;
    }

    /**
     * The KDC server hostname
     * @return
     */
    public String getKerberosHostname() {
        return kerberosHostname;
    }

    public void setKerberosHostname(String kerberosHostname) {
        this.kerberosHostname = kerberosHostname;
    }

    /**
     * The user base DN
     * @return
     */
    public String getUserBaseDn() {
        return userBaseDn;
    }

    public void setUserBaseDn(String userBaseDn) {
        this.userBaseDn = userBaseDn;
    }

    /**
     * The user ID attribute
     * @return
     */
    public String getUserIdAttribute() {
        return userIdAttribute;
    }

    public void setUserIdAttribute(String userIdAttribute) {
        this.userIdAttribute = userIdAttribute;
    }

    /**
     * The LDAP server port
     * @return
     */
    public int getLdapPort() {
        return ldapPort;
    }

    public void setLdapPort(int ldapPort) {
        this.ldapPort = ldapPort;
    }

    /**
     * The protocol used to connect to LDAP server usually ldap or ldaps
     * @return
     */
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * Subtree search on/off flag
     * @param useSubTree
     */
    public void setUseSubTree(boolean useSubTree) {
        this.useSubtree = useSubTree;
    }

    public boolean getUseSubTree() {
        return useSubtree;
    }

    /**
     * User class : The class all users are based on <br>
     * Note : Only used for user sub tree
     * @param userClass
     */
    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public String getUserClass() {
        return userClass;
    }

    /**
     * The login name of the admin user <br>
     * Note : Only used for user sub tree
     * @return
     */
    public void setAdminLogin(String adminLogin) {
        this.adminLogin = adminLogin;
    }

    public String getAdminLogin() {
        return adminLogin;
    }

    /**
     * The password of the admin user <br>
     * Note : Only used for user sub tree
     * @return
     */
    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    /**
     * If true, get the template from getTemplate, if false, use {0}
     * @param generateTemplate
     */
    public void setGenerateTemplate(boolean generateTemplate) {
        this.generateTemplate = generateTemplate;
    }

    public boolean isGenerateTemplate() {
        return generateTemplate;
    }

    /**
     * The realm name
     * @param realm
     */
    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getRealm() {
        return realm;
    }

    /**
     * The user remark field attribute
     * @param userRemarkAttribute
     */
    public void setUserRemarkAttribute(String userRemarkAttribute) {
        this.userRemarkAttribute = userRemarkAttribute;
    }

    public String getUserRemarkAttribute() {
        return userRemarkAttribute;
    }

    /**
     * The class name representing the groups
     * @return
     */
    public String getGroupClass() {
        return groupClass;
    }

    public void setGroupClass(String groupClass) {
        this.groupClass = groupClass;
    }

    /**
     * The groups remark attributes
     * @return
     */
    public String getGroupRemarkAttribute() {
        return groupRemarkAttribute;
    }

    public void setGroupRemarkAttribute(String groupRemarkAttribute) {
        this.groupRemarkAttribute = groupRemarkAttribute;
    }

    /**
     * The groups base DN
     * @return
     */
    public String getGroupBaseDn() {
        return this.groupBaseDn;
    }

    public void setGroupBaseDn(String groupBaseDn) {
        this.groupBaseDn = groupBaseDn;
    }

    /**
     * The groups ID attribute
     * @return
     */
    public String getGroupIdAttribute() {
        return this.groupIdAttribute;
    }

    public void setGroupIdAttribute(String groupIdAttribute) {
        this.groupIdAttribute = groupIdAttribute;
    }

    /**
     * The groups user list attribute
     * (there is none for dynamic groups)
     * @return
     */
    public String getGroupUserListAttribute() {
        return groupUserListAttribute;
    }

    public void setGroupUserListAttribute(String groupUserListAttribute) {
        this.groupUserListAttribute = groupUserListAttribute;
    }

    public String getUserGroupAttribute() {
        return userGroupAttribute;
    }

    public void setUserGroupAttribute(String userGroupAttribute) {
        this.userGroupAttribute = userGroupAttribute;
    }

    public boolean getUseSync() {
        return useSync;
    }

    public void setUseSync(boolean useSync) {
        this.useSync = useSync;
    }

    @Override
    public String toString() {
        String out = "";
        out += "userIdAttribute = " + userIdAttribute + "\r\n";
        out += "userBaseDn = " + userBaseDn + "\r\n";
        out += "authenticationMethod = " + authenticationMethod + "\r\n";
        out += "protocol = " + protocol + "\r\n";
        out += "ldapHostname = " + ldapHostname + "\r\n";
        out += "ldapPort = " + ldapPort + "\r\n";

        out += "adminLogin = " + adminLogin + "\r\n";
        //out += "adminPassword = " + adminPassword + "\r\n";
        out += "userClass = " + userClass + "\r\n";
        out += "useSubtree = " + useSubtree + "\r\n";
        out += "useSync = " + useSync + "\r\n";
        out += "generateTemplate = " + generateTemplate + "\r\n";
        out += "realm = " + realm + "\r\n";
        out += "userRemarkAttribute = " + userRemarkAttribute + "\r\n";

        //Groups
        out += "groupClass = " + groupClass + "\r\n";
        out += "groupRemarkAttribute = " + groupRemarkAttribute + "\r\n";
        out += "groupBaseDn = " + groupBaseDn + "\r\n";
        out += "groupIdAttribute = " + groupIdAttribute + "\r\n";
        out += "groupUserListAttribute = " + groupUserListAttribute + "\r\n";
        out += "userGroupAttribute = " + userGroupAttribute + "\r\n";

        //Kerberos
        out += "kerberosHostname = " + kerberosHostname + "\r\n";

        return out;
    }
}
