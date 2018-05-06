package Tail;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommandLineArgument {

    @Option(name = "-c", usage = "Num of returning symbols", forbids = {"-n"})
    public int numChar;

    @Option(name = "-n", usage = "Num of returning strings", forbids = {"-c"})
    public int numStr;

    @Option(name = "-o", usage = "Name of output file")
    public File oFile;

    @Argument
    public List<String> inputFiles = new ArrayList<>();

    CommandLineArgument(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (Exception e) {
            System.out.println("Ошибка! Неверный ввод аргументов.");
            System.exit(1);
        }
    }
}
