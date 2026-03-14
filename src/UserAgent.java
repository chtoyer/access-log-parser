public final class UserAgent {
    private final String osType;
    private final String browser;

    public UserAgent(String userAgentString) {
        if (userAgentString.contains("Windows")) this.osType = "Windows";
        else if (userAgentString.contains("Macintosh") || userAgentString.contains("Mac OS")) this.osType = "macOS";
        else if (userAgentString.contains("Linux")) this.osType = "Linux";
        else this.osType = "Other";

        if (userAgentString.contains("Edge")) this.browser = "Edge";
        else if (userAgentString.contains("Firefox")) this.browser = "Firefox";
        else if (userAgentString.contains("Chrome") && !userAgentString.contains("Edge")) this.browser = "Chrome";
        else if (userAgentString.contains("Opera") || userAgentString.contains("OPR")) this.browser = "Opera";
        else this.browser = "Other";
    }
    public String getOsType() { return osType; }
    public String getBrowser() { return browser; }
}