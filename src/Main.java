import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Statistics stats = new Statistics();

        while (true) {
            System.out.println("Введите путь к файлу:");
            String path = scanner.nextLine();
            File file = new File(path);

            if (!file.exists() || file.isDirectory()) {
                System.out.println("Указанный путь не является файлом или не существует.");
                return;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.length() > 1024) {
                        throw new RuntimeException("Строка слишком длинная: " + line.length() + " символов");
                    }
                    LogEntry entry = new LogEntry(line);
                    stats.addEntry(entry);
                }
                System.out.println("\nСписок несуществующих страниц (404):");
                if (stats.getInvalidPages().isEmpty()) {
                    System.out.println("- Страниц с ошибкой 404 не найдено");
                } else {
                    for (String page : stats.getInvalidPages()) {
                        System.out.println("- " + page);
                    }
                }

                System.out.println("Средний объем трафика за час: " + stats.getTrafficRate());
                System.out.println("Статистика операционных систем (доли):");
                Map<String, Double> osStatistics = stats.getOsStats();
                for (Map.Entry<String, Double> entry : osStatistics.entrySet()) {
                    System.out.printf("- %s: %.4f%n", entry.getKey(), entry.getValue());
                }

                System.out.println("\nСтатистика браузеров (доли):");
                Map<String, Double> browserStats = stats.getBrowserStats();
                for (Map.Entry<String, Double> entry : browserStats.entrySet()) {
                    System.out.printf("- %s: %.4f%n", entry.getKey(), entry.getValue());
                }

                System.out.println("Среднее кол-во посещений (люди) в час: " + stats.getAvgUserVisitsPerHour());
                System.out.println("Среднее кол-во ошибок в час: " + stats.getAvgErrorsPerHour());
                System.out.println("Среднее кол-во посещений на одного пользователя: " + stats.getAvgVisitsPerUser());

                System.out.println("Пиковая посещаемость сайта (в секунду): " + stats.getMaxVisitsPerSecond());
                System.out.println("Максимальная посещаемость одним пользователем: " + stats.getMaxVisitsPerUser());
                System.out.println("\nСписок сайтов-рефереров (домены):");
                if (stats.getRefererDomains().isEmpty()) {
                    System.out.println("- Ссылающиеся сайты не найдены");
                } else {
                    for (String domain : stats.getRefererDomains()) {
                        System.out.println("- " + domain);
                    }
                }

            } catch (Exception ex) {
                System.out.println("Ошибка: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}