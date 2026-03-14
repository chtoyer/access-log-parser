import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class LogEntry {
    private final String ipAddress;
    private final LocalDateTime dateTime;
    private final HttpMethod method;
    private final String path;
    private final int responseCode;
    private final int responseSize;
    private final String referer;
    private final UserAgent userAgent;

    public LogEntry(String line) {
        String[] parts = line.split(" ");
        this.ipAddress = parts[0];

        String dateTimeStr = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);
        this.dateTime = java.time.ZonedDateTime.parse(dateTimeStr, formatter).toLocalDateTime();
        String[] quoteParts = line.split("\"");

        if (quoteParts.length >= 2) {
            String[] requestParts = quoteParts[1].split("\\s+");
            this.method = HttpMethod.valueOf(requestParts[0].toUpperCase());
            this.path = requestParts[1];
        } else {
            this.method = null;
            this.path = null;
        }

        int firstQuoteEnd = line.indexOf("\"", line.indexOf("\"") + 1);
        int secondQuoteStart = line.indexOf("\"", firstQuoteEnd + 1);
        if (firstQuoteEnd != -1 && secondQuoteStart != -1) {
            String middle = line.substring(firstQuoteEnd + 1, secondQuoteStart).trim();
            String[] middleParts = middle.split("\\s+");
            this.responseCode = Integer.parseInt(middleParts[0]);
            this.responseSize = middleParts[1].equals("-") ? 0 : Integer.parseInt(middleParts[1]);
        } else {
            this.responseCode = 0;
            this.responseSize = 0;
        }

        this.referer = (quoteParts.length >= 4) ? quoteParts[3] : "-";
        String uaString = (quoteParts.length >= 6) ? quoteParts[5] : "-";
        this.userAgent = new UserAgent(uaString);
    }
    public String getIpAddress() { return ipAddress; }
    public LocalDateTime getDateTime() { return dateTime; }
    public HttpMethod getMethod() { return method; }
    public String getPath() { return path; }
    public int getResponseCode() { return responseCode; }
    public int getResponseSize() { return responseSize; }
    public String getReferer() { return referer; }
    public UserAgent getUserAgent() { return userAgent; }
}