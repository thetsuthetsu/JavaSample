package ldap;

public enum AuthenticationMethod {
    SIMPLE("simple"), // Not encrypted connection
    DIGEST_MD5("DIGEST-MD5"), // Digest MD5 (SASL)
    CRAM_MD5("CRAM-MD5"), // Cram MD5 (SASL)
    KERBEROS("GSSAPI"), // Kerberos
    ANONYMOUS("NONE");

    private final String key;

    AuthenticationMethod(String key) {
        this.key = key;
    }

    /**
     * Returns the string identifier of that authentication method
     *
     * @return
     */

    public String getKey() {
        return this.key;
    }
}
