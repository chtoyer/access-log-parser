import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите путь к файлу:");
        String path = new Scanner(System.in).nextLine();
        File file = new File(path);

        if (!file.exists() || file.isDirectory()) {
            System.out.println("Указанный путь не является файлом или не существует.");
            return;
        }

        int totalLines = 0;
        int maxLength = 0;
        int minLength = Integer.MAX_VALUE;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int length = line.length();

                if (length > 1024) {
                    throw new LineTooLongException("Строка слишком длинная: " + length + " символов");
                }
                totalLines++;
                if (length > maxLength) maxLength = length;
                if (length < minLength) minLength = length;
            }

            if (totalLines == 0) minLength = 0;

            System.out.println("Общее количество строк в файле: " + totalLines);
            System.out.println("Длина самой длинной строки: " + maxLength);
            System.out.println("Длина самой короткой строки: " + minLength);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}