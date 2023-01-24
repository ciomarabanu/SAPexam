package lfsr;

import java.util.ArrayList;
import java.util.List;

public class LFSR32bit {
    //32 bit lfsr with 1 register
    //tap sequence from right to left: x^31 + x^11 + x^5 + x^2 (it will only process those bits)
    //it needs an initial key seed of 32 bits
    byte[] register;

    public LFSR32bit(byte[] seed) {
        if (seed.length != 4) {
            throw new UnsupportedOperationException("Seed size not ok");
        }
//        this.register = seed; don't do shallow copy
        this.register = seed.clone();

        //daca faci de mana:
//        for (int i = 0; i < register.length; i++) {
//            this.register[i] = seed[i];
//        }
    }

    public byte get31thBit() {
        byte maskMostSignificantBit = (byte) 0b1000_0000;
        byte result = (byte) (register[0] & maskMostSignificantBit);
        return (byte) (result == 0 ? 0 : 1);
    }

    public byte get11thBit() {
        byte mask11thBit = (byte) 0b0000_1000;
        byte result = (byte) (register[2] & mask11thBit);
        return (byte) (result == 0 ? 0 : 1);
    }

    public byte get5thBit() {
        byte mask5thBit = (byte) 0b0010_0000;
        byte result = (byte) (register[3] & mask5thBit);
        return (byte) (result == 0 ? 0 : 1);
    }

    public byte get2ndBit() {
        byte mask2ndBit = (byte) 0b0000_0100;
        byte result = (byte) (register[3] & mask2ndBit);
        return (byte) (result == 0 ? 0 : 1);
    }

    public byte getLastBit(byte value) {
        return (byte) (value & 1);
    }

    public byte shiftAndAddMostSignificantBit(Byte value, Byte bit) {
        value = (byte) ((value & 0xFF) >>> 1); //& 0xff keeps the value at byte level
        return (byte) (value | (bit << 7));
    }

    public byte shiftRegisterAndAddBit(byte bit) {
        byte firstByteLastBit = getLastBit(register[0]);
        register[0] = shiftAndAddMostSignificantBit(register[0], bit);

        byte secondBytelastBit = getLastBit(register[1]);
        register[1] = shiftAndAddMostSignificantBit(register[1], firstByteLastBit);

        byte thirdBiteLastBit = getLastBit(register[2]);
        register[2] = shiftAndAddMostSignificantBit(register[2], secondBytelastBit);

        byte pseudoRandomBit = getLastBit(register[3]);
        register[3] = shiftAndAddMostSignificantBit(register[3], thirdBiteLastBit);

        return pseudoRandomBit;
    }


    public byte procesTapSequence() {
        return (byte) (get31thBit() ^ get11thBit() ^ get5thBit() ^ get2ndBit());
    }

    public List<Byte> getRandomBits(int noBits) {
        //we need to run 32 iterations to remove the initial seed

        for (int i = 0; i < 32; i++) {
            byte nextBit = procesTapSequence();
            shiftRegisterAndAddBit(nextBit);
        }

        List<Byte> randomBits = new ArrayList<>();
        for (int i = 0; i < noBits; i++) {
            byte nextBit = procesTapSequence();
            byte randomBit = shiftRegisterAndAddBit(nextBit);
            randomBits.add(randomBit);
        }
        return randomBits;
    }
}

