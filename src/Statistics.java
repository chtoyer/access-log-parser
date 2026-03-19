import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private HashSet<String> pages = new HashSet<>();
    private HashSet<String> invalidPages = new HashSet<>();
    private HashMap<String, Integer> osCounts = new HashMap<>();
    private HashMap<String, Integer> browserCounts = new HashMap<>();
    private int userVisits = 0;
    private int errorCount = 0;
    private HashSet<String> uniqueUserIps = new HashSet<>();

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
    }

    public void addEntry(LogEntry entry) {
        this.totalTraffic += entry.getResponseSize();
        LocalDateTime entryTime = entry.getDateTime();

        if (minTime == null || entryTime.isBefore(minTime)) minTime = entryTime;
        if (maxTime == null || entryTime.isAfter(maxTime)) maxTime = entryTime;
        if (entry.getResponseCode() == 200) {
            pages.add(entry.getPath());
        }
        if (entry.getResponseCode() == 404) {
            invalidPages.add(entry.getPath());
        }

        String os = entry.getUserAgent().getOsType();
        osCounts.put(os, osCounts.getOrDefault(os, 0) + 1);

        String browser = entry.getUserAgent().getBrowser();
        browserCounts.put(browser, browserCounts.getOrDefault(browser, 0) + 1);

        if (!entry.getUserAgent().isBot()) {
            userVisits++;
            uniqueUserIps.add(entry.getIpAddress());
        }
        if (entry.getResponseCode() >= 400 && entry.getResponseCode() < 600) {
            errorCount++;
        }
    }

    private double getHoursDiff() {
        if (minTime == null || maxTime == null) return 0;
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        return hours == 0 ? 1.0 : (double) hours;
    }

    public double getAvgUserVisitsPerHour() {
        return userVisits / getHoursDiff();
    }

    public double getAvgErrorsPerHour() {
        return errorCount / getHoursDiff();
    }

    public double getAvgVisitsPerUser() {
        if (uniqueUserIps.isEmpty()) return 0;
        return (double) userVisits / uniqueUserIps.size();
    }

    public HashSet<String> getPages() {
        return pages;
    }

    public HashSet<String> getInvalidPages() {
        return invalidPages;
    }

    public HashMap<String, Double> getOsStats() {
        return calculateProportions(osCounts);
    }

    public HashMap<String, Double> getBrowserStats() {
        return calculateProportions(browserCounts);
    }

    private HashMap<String, Double> calculateProportions(HashMap<String, Integer> countsMap) {
        HashMap<String, Double> proportions = new HashMap<>();
        int total = 0;
        for (int count : countsMap.values()) {
            total += count;
        }

        if (total > 0) {
            for (Map.Entry<String, Integer> entry : countsMap.entrySet()) {
                proportions.put(entry.getKey(), (double) entry.getValue() / total);
            }
        }
        return proportions;
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null) return 0;
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hours == 0) hours = 1;
        return (double) totalTraffic / hours;
    }
}