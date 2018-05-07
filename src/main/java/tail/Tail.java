package tail;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

public class Tail {


    public static void main(String[] args){

        try {

            //Парсинг

            Scanner scan = new Scanner(System.in);
            String consoleRead = scan.nextLine();
            CommandLineArgument arguments = new CommandLineArgument(consoleRead.split(" "));

            //Считывание

            List<String> text = new ArrayList<>();
            Map<String, List<String>> filesTexts = new HashMap<>();
            Map<String, List<String>> newFilesTexts;

            if (arguments.numStr < 0 && arguments.numChar < 0) {
                System.out.println("Аргумент меньше или равен 0");
                System.exit(1);
            }

            if (arguments.oFile != null && !new File("./src/test/resources/" + arguments.oFile + ".txt").exists()) {
                File outFile = new File("./src/test/resources/" + arguments.oFile + ".txt");
                if (outFile.createNewFile()) {
                    System.out.println("Файл создан: " + outFile.getAbsolutePath());
                } else {
                    System.out.println("Не удалось создать файл.");
                }
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

            TailEdit editedText = new TailEdit(arguments, filesTexts);
            editedText.doTailEdit();
            newFilesTexts = editedText.getNewFilesTexts();

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
                } catch (Exception e) {
                    System.out.println("Ошибка при записи в файл. Вам нужна помочь программиста.");
                    System.exit(-1);
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
        }catch (Exception e){
            System.out.println("В ходе выполнения программы произошёл неизвестный косяк");
            System.exit(-1);
        }
    }
}