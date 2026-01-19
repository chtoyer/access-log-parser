import java.util.Scanner;
import java.io.File;

public class Cycle {
    public static void main(String[] args) {
        int count = 0;
        while (true) {
            System.out.println("Введите абсолютный путь к файлу, в формате /.../file.txt (для Mac/Linux) или C:\\...\\file.txt (для Windows) :");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (!fileExists) {
                System.out.println("Ошибка: файла не существует.");
                continue;
            }
            if (isDirectory) {
                System.out.println("Ошибка: путь ведет к папке.");
                continue;
            }
            count++;
            System.out.println("Путь указан верно");
            System.out.println("Это файл номер " + count);
        }
    }
}