package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileManip {
    public static void main(String[] args) throws IOException {
//=====================================================
        //FILES
//=====================================================

        //files are processed as streams
        //binary files are processed differently (at byte level) from text files (at char level, 2bytes)

        //write into the TEXT FILE
        File message = new File("message.txt");
        if (!message.exists()){
            message.createNewFile();
        }
        FileWriter fw = new FileWriter(message);
        PrintWriter pw = new PrintWriter(fw);
        pw.println("This is a secret message");
        pw.println("Don't tell anyone");

        //ALWAYS CLOSE THE FILE!!!
        pw.close(); //SAU fw.close();
    }
}
