package antivirus;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

public class Antivirus {

    private static final String DELIMITER = " > ";

    public static void scan(String rootPath, String key, String outputFilename) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        File hmacOutputFile = new File(outputFilename);

        hmacOutputFile.createNewFile();

        FileOutputStream fos = new FileOutputStream(hmacOutputFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos); //keep the same BOS in which to write recursively
        //don't open the file everytime, you'll just overwrite it

        File rootFolder = new File(rootPath);

        recCheckFileAndStoreHMAC(key, bos, rootFolder);
        bos.close();
    }

    private static void recCheckFileAndStoreHMAC(String key, BufferedOutputStream bos, File rootFolder)
        throws NoSuchAlgorithmException, InvalidKeyException, IOException {

        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            System.out.println("root folder not found");
            return;
        }

        File[] files = rootFolder.listFiles();


        for (File file : files) {

            //compute Hmac
            if (file.isFile()) {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);

                //create Mac obj
                SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "SHA1"); //create key from string
                Mac mac = Mac.getInstance("HmacSHA1");
                mac.init(secretKeySpec);

                byte[] buffer = new byte[Math.max(50, files.length)]; //random no for buffer length
                int noBytes = 0;
                byte[] hmac;

                while (noBytes != -1) {
                    noBytes = bis.read(buffer); //returns -1 if no bytes are read from file

                    if (noBytes != -1) {
                        mac.update(buffer, 0, noBytes);
                    }
                }
                hmac = mac.doFinal();

                //write hmac in output file
                bos.write(file.getName().getBytes());
                bos.write(DELIMITER.getBytes());
                bos.write(Base64.getEncoder().encode(hmac));
                bos.write("\n".getBytes());

                bis.close();
            } else if (file.isDirectory()) {
                recCheckFileAndStoreHMAC(key, bos, file);
            }
        }
    }

    public static void check(String rootPath, String key, String initialHMACsFilename)
        throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        File initialHMACsFile = new File(initialHMACsFilename);

        if (!initialHMACsFile.exists()) {
            throw new FileNotFoundException();
        }

        FileReader fr = new FileReader(initialHMACsFile);
        BufferedReader br = new BufferedReader(fr);

        HashMap initialHMACs = new HashMap<String, String>();
        var entries =  br.lines()
            .map(e -> e.split(DELIMITER))
            .peek(e -> initialHMACs.put(e[0], e[1]))
            .count();
        br.close();

        File rootFolder = new File(rootPath);

        //initialise report file
        File report = new File("Status report.txt");
        report.createNewFile();
        FileOutputStream fos =  new FileOutputStream(report);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        recFileCompareAndReport(key, initialHMACs, bos, rootFolder);

        bos.close();

    }

    private static void recFileCompareAndReport(String key,
                                                HashMap<String, String> initialHMACs,
                                                BufferedOutputStream bos,
                                                File rootFolder)
        throws NoSuchAlgorithmException, InvalidKeyException, IOException {

        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            System.out.println("root folder not found");
            return;
        }

        File[] files = rootFolder.listFiles();


        for (File file : files) {

            //compute Hmac
            if (file.isFile()) {

                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);

                //create Mac obj
                SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "SHA1");
                Mac mac = Mac.getInstance("HmacSHA1");
                mac.init(secretKeySpec);

                byte[] buffer = new byte[Math.max(50, files.length)]; //random no
                int noBytes = 0;
                byte[] hmac;

                while (noBytes != -1) {
                    noBytes = bis.read(buffer);

                    if (noBytes != -1) {
                        mac.update(buffer, 0, noBytes);
                    }
                }
                hmac = mac.doFinal();

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();

                //crosscheck hmac with value from hashmap & write result in report file
                if (!initialHMACs.containsKey(file.getName())) {
                    bos.write("STATUS: New " .getBytes());

                } else
                    if (Arrays.equals(Base64.getDecoder().decode(initialHMACs.get(file.getName())), hmac)) {
                        bos.write("STATUS: OK ".getBytes());
                    } else {
                        bos.write("STATUS: Corrupted ".getBytes());
                    }
                bos.write("File Name: ".getBytes());
                bos.write( file.getName().getBytes());
                bos.write("Check time: ".getBytes()) ;
                bos.write(formatter.format(date).getBytes()) ;
                bos.write("\n".getBytes()) ;


                bis.close();
            } else if (file.isDirectory()) {
                recFileCompareAndReport(key, initialHMACs, bos, file);
            }
        }
    }
}
