package Tail;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class Tail {


    public static void main(String[] args) throws IOException {

        //Парсинг

        Scanner scan = new Scanner(System.in);
        String consoleRead = scan.nextLine();
        CommandLineArgument arguments = new CommandLineArgument(consoleRead.split(" "));

        //Считывание

        List<String> text = new ArrayList<>();
        List<String> newText = new ArrayList<>();
        Map<String, List<String>> filesTexts = new HashMap<>();
        Map<String, List<String>> newFilesTexts = new HashMap<>();

        if (arguments.numStr < 0 && arguments.numChar < 0) {
            System.out.println("Аргумент меньше или равен 0");
            System.exit(1);
        }

        if (arguments.oFile != null && !new File("./src/test/resources/" + arguments.oFile + ".txt").exists()) {
            System.out.println("Выходного файла " + arguments.oFile + " не существует в дирректории resources");
            System.exit(1);
        }

        if (!arguments.inputFiles.contains("")) {
            for (String fileName : arguments.inputFiles) {
                File myFile = new File("./src/test/resources/" + fileName + ".txt");
                if (!myFile.exists()) {
                    System.out.println("Входного файла " + fileName + " не существует в дирректории resources");
                    System.exit(1);
                }
            }
            for (String fileName : arguments.inputFiles) {
                File myFile = new File("./src/test/resources/" + fileName + ".txt");
                text = Files.readAllLines(myFile.toPath(), Charset.forName("Cp1251"));
                filesTexts.put(fileName, text);
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
            //arguments.inputFiles.add("");
            filesTexts.put("", text);
        }

        //Действия для кол-ва строк

        if (arguments.numStr > 0) {
            for (String fileName : arguments.inputFiles) {
                Collections.reverse(filesTexts.get(fileName));
                newText = filesTexts.get(fileName).subList(0, arguments.numStr);
                Collections.reverse(newText);
                newFilesTexts.put(fileName, new ArrayList<>(newText));
                newText.clear();
            }
        }

        //Действия для кол-ва символов

        if (arguments.numChar > 0) {
            for (String fileName : arguments.inputFiles) {
                Collections.reverse(filesTexts.get(fileName));
                int lengthCounter = 0;
                for (String line : filesTexts.get(fileName)) {
                    if (line.length() + lengthCounter <= arguments.numChar) {
                        newText.add(line);
                        lengthCounter += line.length();
                    } else {
                        int lineStart = line.length() - (arguments.numChar - lengthCounter);
                        newText.add(line.substring(lineStart, line.length()));
                        lengthCounter += line.length();
                    }
                    if (lengthCounter >= arguments.numChar) {
                        break;
                    }
                }
                Collections.reverse(newText);
                newFilesTexts.put(fileName, new ArrayList<>(newText));
                newText.clear();
            }
        }

        //Действие когда ничего не подходит

        if (arguments.numChar == 0 && arguments.numStr == 0) {
            for (String fileName : arguments.inputFiles) {
                Collections.reverse(filesTexts.get(fileName));
                newText = filesTexts.get(fileName).subList(0, 10);
                Collections.reverse(newText);
                newFilesTexts.put(fileName, new ArrayList<>(newText));
                newText.clear();
            }
        }

        //Вывод

        //System.out.println(arguments.numChar + " " + arguments.numStr + " " + arguments.inputFiles);

        if (arguments.oFile != null) {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("./src/test/resources/" + arguments.oFile + ".txt"),
                    "Cp1251"))) {
                for (String fileName : arguments.inputFiles) {
                    if (arguments.inputFiles.size() > 1)
                        writer.write(fileName);
                    for (String line : newFilesTexts.get(fileName)) {
                        writer.write("\r\n");
                        writer.write(line);
                    }
                }
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