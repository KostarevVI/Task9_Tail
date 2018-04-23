package Tail;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommandLineArgument {

    @Option(name = "-c", usage = "Num of returning symbols")
    public int numChar;

    @Option(name = "-n", usage = "Num of returning strings")
    public int numStr;

    @Option(name = "-o", usage = "Name of output file")
    public File oFile;

    @Argument
    public List<String> inputFiles = new ArrayList<>();

    public CommandLineArgument(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (Exception e) {
            System.out.println("Ошибка парсинга: " + e.getMessage());
            System.exit(0);
        }
    }
}