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
    private HashMap<String, Integer> osCounts = new HashMap<>();

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
        String os = entry.getUserAgent().getOsType();
        if (osCounts.containsKey(os)) {
            osCounts.put(os, osCounts.get(os) + 1);
        } else {
            osCounts.put(os, 1);
        }
    }
    public HashSet<String> getPages() {
        return pages;
    }
    public HashMap<String, Double> getOsStats() {
        HashMap<String, Double> osStats = new HashMap<>();
        int totalOs = 0;

        for (int count : osCounts.values()) {
            totalOs += count;
        }
        for (Map.Entry<String, Integer> entry : osCounts.entrySet()) {
            osStats.put(entry.getKey(), (double) entry.getValue() / totalOs);
        }
        return osStats;
    }
    public double getTrafficRate() {
        if (minTime == null || maxTime == null) return 0;
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hours == 0) hours = 1;
        return (double) totalTraffic / hours;
    }
}