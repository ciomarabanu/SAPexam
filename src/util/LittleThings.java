package util;

import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

public class LittleThings {
    public static void main(String[] args) throws IOException {

        // boolean -> 1 bit
        // byte -> 1 Byte ( from -128 to 127)
        // char -> 2 Bytes (in C/C++ char -> 1Byte)
        // int -> 4 Bytes
        // float -> 4 Bytes (for fractional nums)
        // long -> 8 BYTES
        // double -> 8 BYTES (for fractional nums)

        //SHA 1 size - 20 bytes
        //SHA 256 size - 32 bytes
        //SHA 512 size - 64
        //MD 5 size - 16 bytes

        var newLine = System.getProperty("line.separator").getBytes();

        byte[] iv = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

        // String to byte[]:
        String a = "some bytes";
        byte[] b = a.getBytes();

        // byte[] to string:
        String c = new String(b); //b is a byte[]

        //BYTE and HEXA format:
        byte num = 0b0000_0010;
        num = 0x02; //
        num = 0xa;

        //BIT MASK
        int val = 0b0000_0000_0000_0010;
        val = 0xaa;

        byte[] binaryKey = {(byte)0b1000_0001, 0b0000_0010, 0b0000_0100, 0b0000_1000 };
        byte[] anotherBynaryKey = {(byte)0x81, 0x02, 0x04, 0x08}; //the 2 keys are equivalent

        //encode byte[] to BASE64
        String b64key = Base64.getEncoder().encodeToString(binaryKey);

        //BITWISE OP
        //0000 1011
//    ->  1234 5678 ->   numerotarea bitilor

        int x = 1;
        //bitmask 4th bit of an int:
        var y = x << 28;
        System.out.println(Integer.toBinaryString(y));
        //bitmask 4th bit of long:
        System.out.println(Long.toBinaryString(y));

        // cand faci cast din byte in int preserva bit sign,
        // cand faci invers, din int in byte pierzi bit sign pt ca truncheaza


    }

}
