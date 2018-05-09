package tail;

import java.util.*;

public class TailEdit {

    private List<String> newText = new ArrayList<>();
    private Map<String, List<String>> filesTexts;
    private Map<String, List<String>> newFilesTexts = new HashMap<>();
    private CommandLineArgument arguments;

    TailEdit(CommandLineArgument args, Map<String, List<String>> filesTexts) {
        this.filesTexts = filesTexts;
        this.arguments = args;
    }

    public Map<String, List<String>> getNewFilesTexts() {
        return newFilesTexts;
    }

    public void doTailEdit() {

        //Действия для кол-ва строк

        if (arguments.numStr > 0) {
            for (String fileName : arguments.inputFiles) {
                Collections.reverse(filesTexts.get(fileName));
                if (filesTexts.get(fileName).size() > arguments.numStr) {
                    newText = filesTexts.get(fileName).subList(0, arguments.numStr);
                } else {
                    newText = filesTexts.get(fileName);
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

        //Действие когда ничего не подходит

        if (arguments.numChar == 0 && arguments.numStr == 0) {
            for (String fileName : arguments.inputFiles) {
                Collections.reverse(filesTexts.get(fileName));
                if (filesTexts.get(fileName).size() > 10) {
                    newText = filesTexts.get(fileName).subList(0, 10);
                } else {
                    newText = filesTexts.get(fileName);
                }
                Collections.reverse(newText);
                newFilesTexts.put(fileName, new ArrayList<>(newText));
                newText.clear();
            }
        }
    }
}
