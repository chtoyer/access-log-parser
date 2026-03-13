import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;

public class Comparison {
    public static void main(String[] args) {
        System.out.println("Введите путь к файлу:");
        String path = new Scanner(System.in).nextLine();
        File file = new File(path);

        if (!file.exists() || file.isDirectory()) {
            System.out.println("Указанный путь не является файлом или не существует.");
            return;
        }

        int totalLines = 0;
        int googleBotCount = 0;
        int yandexBotCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 1024) {
                    throw new RuntimeException("Строка длиннее 1024 символов");
                }
                totalLines++;

                int lastQuote = line.lastIndexOf("\"");
                int firstQuote = line.lastIndexOf("\"", lastQuote - 1);

                if (lastQuote != -1 && firstQuote != -1) {
                    String userAgent = line.substring(firstQuote + 1, lastQuote);

                    int openBracket = userAgent.indexOf("(");
                    int closeBracket = userAgent.indexOf(")");

                    if (openBracket != -1 && closeBracket != -1) {
                        String firstBrackets = userAgent.substring(openBracket + 1, closeBracket);
                        String[] parts = firstBrackets.split(";");

                        if (parts.length >= 2) {
                            String fragment = parts[1].trim();
                            String botName = fragment.split("/")[0];
                            if (botName.equals("Googlebot")) googleBotCount++;
                            else if (botName.equals("YandexBot")) yandexBotCount++;
                        }
                    }
                }
            }
            System.out.println("Общее количество запросов: " + totalLines);
            System.out.println("Доля Googlebot: " + (double) googleBotCount / totalLines);
            System.out.println("Доля YandexBot: " + (double) yandexBotCount / totalLines);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}