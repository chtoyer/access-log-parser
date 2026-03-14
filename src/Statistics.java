import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

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
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null) return 0;
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hours == 0) hours = 1;
        return (double) totalTraffic / hours;
    }
}