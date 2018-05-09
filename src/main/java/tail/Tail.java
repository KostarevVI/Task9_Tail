package tail;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

public class Tail {


    public static void main(String[] args) {

        //Парсинг

        Scanner scan = new Scanner(System.in);
        String consoleRead;
        CommandLineArgument arguments = new CommandLineArgument(args);

        //Считывание

        List<String> text = new ArrayList<>();
        Map<String, List<String>> filesTexts = new HashMap<>();
        Map<String, List<String>> newFilesTexts;

        if (arguments.numStr < 0 && arguments.numChar < 0) {
            System.out.println("Аргумент меньше 0");
            System.exit(1);
        }

        try {
            if (arguments.oFile != null && !new File(arguments.oFile + ".txt").exists()) {
                File outFile = new File(arguments.oFile + ".txt");
                if (outFile.createNewFile()) {
                    System.out.println("Файл создан: " + outFile.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при создания файла");
            System.exit(1);
        }

        if (!arguments.inputFiles.contains("")) {
            for (String fileName : arguments.inputFiles) {
                File myFile = new File(fileName + ".txt");
                if (!myFile.exists()) {
                    System.out.println("Входного файла " + fileName + " не существует в заданной директории");
                    System.exit(1);
                }
            }
            try {
                for (String fileName : arguments.inputFiles) {
                    File myFile = new File(fileName + ".txt");
                    text = Files.readAllLines(myFile.toPath(), Charset.forName("Cp1251"));
                    filesTexts.put(fileName, text);
                }
            } catch (IOException e) {
                System.out.println("Ошибка чтения из файла");
                System.exit(1);
            }
        } else {
            System.out.println("Ожидаем ввод с командной строки (для завершения введите end)");
            consoleRead = scan.nextLine();
            while (!consoleRead.equalsIgnoreCase("end")) {
                text.add(consoleRead);
                consoleRead = scan.nextLine();
            }
            text.remove("end");
            if (text.isEmpty()) {
                System.out.println("Ничего не введено");
                System.exit(1);
            }
            filesTexts.put("", text);
        }

        TailEdit editedText = new TailEdit(arguments, filesTexts);
        editedText.doTailEdit();
        newFilesTexts = editedText.getNewFilesTexts();

        //Вывод

        if (arguments.oFile != null) {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(arguments.oFile + ".txt"),
                    "Cp1251"))) {
                for (String fileName : arguments.inputFiles) {
                    if (arguments.inputFiles.size() > 1)
                        writer.write(fileName);
                    for (String line : newFilesTexts.get(fileName)) {
                        writer.write("\r\n");
                        writer.write(line);
                    }
                }
            } catch (Exception e) {
                System.out.println("Ошибка при записи в файл");
                System.exit(1);
            }
        } else {
            for (String fileName : newFilesTexts.keySet()) {
                if (arguments.inputFiles.size() > 1)
                    System.out.println(fileName);
                for (String line : newFilesTexts.get(fileName)) {
                    System.out.println(line);
                }
            }
        }
    }
}