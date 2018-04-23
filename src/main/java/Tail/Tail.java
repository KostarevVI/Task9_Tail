package Tail;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

        if (arguments.numChar == 0 && arguments.numStr == 0 &&
                arguments.oFile == null && arguments.inputFiles.get(0).equals("")) {
            System.out.println("Строка пустая");
            System.out.println("Введите строки в консоль:");
            System.out.println("Ожидаем ввод с командной строки (для завершения введите end)");
            consoleRead = scan.nextLine();
            while (!consoleRead.equalsIgnoreCase("end")) {
                text.add(consoleRead);
                consoleRead = scan.nextLine();
            }
            text.remove("end");
            int counter = 0;
            if (text.isEmpty()) {
                System.out.println("Ничего не введено");
                System.exit(0);
            }
            Collections.reverse(text);
            for (String line : text) {
                if (counter < 10) {
                    newText.add(line);
                    counter++;
                } else {
                    break;
                }
            }
            Collections.reverse(newText);
            newFilesTexts.put("From console", newText);
        }

        if (arguments.numChar > 0 && arguments.numStr > 0) {
            System.out.println("Вызваны оба аркумента -c -n (нелья так)");
            System.exit(0);
        }

        if (arguments.oFile != null && !new File("./" + arguments.oFile + ".txt").exists()) {
            System.out.println("Выходного файла " + arguments.oFile + " не существует в корневой дирректории");
            System.exit(0);
        }

        if (arguments.inputFiles != null && !arguments.inputFiles.isEmpty() &&
                (arguments.numStr > 0 || arguments.numChar > 0)) {
            for (String fileName : arguments.inputFiles) {
                File myFile = new File("./" + fileName + ".txt");
                if (!myFile.exists()) {
                    System.out.println("Входного файла " + fileName + " не существует в корневой дирректории");
                    System.exit(0);
                }
            }
            for (String fileName : arguments.inputFiles) {
                File myFile = new File("./" + fileName + ".txt");
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
                System.exit(0);
            }
            arguments.inputFiles.add("From console");
            filesTexts.put("From console", text);
        }

        //Действия для кол-ва строк

        if (arguments.numStr > 0) {
            for (String fileName : arguments.inputFiles) {
                Collections.reverse(filesTexts.get(fileName));
                int counter = 0;
                for (String line : filesTexts.get(fileName)) {
                    if (counter < arguments.numStr) {
                        newText.add(line);
                        counter++;
                    } else {
                        break;
                    }
                }
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

        //Вывод

        //System.out.println(arguments.numChar + " " + arguments.numStr + " " + arguments.inputFiles);

        if (arguments.oFile != null) {
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("./" + arguments.oFile + ".txt"), "Cp1251"));
            for (String fileName : arguments.inputFiles) {
                writer.write(fileName);
                for (String line : newFilesTexts.get(fileName)) {
                    writer.write("\r\n");
                    writer.write(line);
                }
                writer.flush();
            }
        } else {
            for (String fileName : arguments.inputFiles) {
                if (arguments.inputFiles.size() > 1)
                    System.out.println(fileName);
                for (String line : newFilesTexts.get(fileName)) {
                    System.out.println(line);
                }
            }
        }
    }
}