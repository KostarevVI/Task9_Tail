package tail;

import org.junit.Test;

import java.io.*;

import static com.google.common.io.Files.equal;
import static org.junit.Assert.assertTrue;

public class TailTest {
    @Test
    public void main() throws IOException{

        //-с из файла в файл

        Tail.main(new String[]{"-c", "10", "-o", "src/test/resources/output1", "src/test/resources/file1"});
        File output1 = new File("src/test/resources/output1.txt");
        assertTrue(equal(output1, new File("src/test/resources/result1.txt")));
        output1.delete();

        //-n из файла в файл

        Tail.main(new String[]{"-n", "6", "-o", "src/test/resources/output2", "src/test/resources/file2"});
        File output2 = new File("src/test/resources/output2.txt");
        assertTrue(equal(output2, new File("src/test/resources/result2.txt")));
        output2.delete();

        //10 последних строк из файла в файл

        Tail.main(new String[]{"-o", "src/test/resources/output3", "src/test/resources/file3"});
        File output3 = new File("src/test/resources/output3.txt");
        assertTrue(equal(output3, new File("src/test/resources/result3.txt")));
        output3.delete();

        //-с ввод из консоли, вывод в файл

        System.setIn(new ByteArrayInputStream("Привет, меня зовут Влад\nend".getBytes()));
        Tail.main(new String[]{"-c", "10", "-o", "src/test/resources/output4"});
        File output4 = new File("src/test/resources/output4.txt");
        assertTrue(equal(output4, new File("src/test/resources/result4.txt")));
        output4.delete();

        //-n из файла в консоль
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream old = System.out;
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        Tail.main(new String[]{"-n", "3", "src/test/resources/file5"});
        System.out.flush();
        System.setOut(old);
        ps.close();
        assertTrue(baos.toString().equals("что такое аддон\r\nзолотой вавилон\r\nу меня появился другой\r\n"));

        //-c из концоли в консоль

        System.setIn(new ByteArrayInputStream("1234567890\nend".getBytes()));
        baos = new ByteArrayOutputStream();
        old = System.out;
        ps = new PrintStream(baos);
        System.setOut(ps);
        Tail.main(new String[]{"-c","5"});
        System.out.flush();
        System.setOut(old);
        ps.close();
        String result = baos.toString().substring(baos.toString().length()-7,baos.toString().length()-2);
        assertTrue(result.equals("67890"));
    }
}
