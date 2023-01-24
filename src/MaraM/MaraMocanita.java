package MaraM;

import java.io.*;

public class MaraMocanita {
    public static void main(String[] args) {

    }

    public static void genericMethod(String inputFileName, String key, String outputFileName) throws IOException {
        File input = new File(inputFileName);
        if (!input.exists()) {
            throw new FileNotFoundException();
        }
        FileInputStream fis = new FileInputStream(input);
        BufferedInputStream bis = new BufferedInputStream(fis);

        File output = new File(outputFileName);
        if (!output.exists()){
            output.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(output);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        //add code here

        bis.close();
        bos.close();
    }

    public static String getHex(byte[] values) {
        StringBuilder sb = new StringBuilder();
        for (byte value : values) {
            sb.append(String.format("%02x", value));
        }

        return sb.toString();
    }
}
