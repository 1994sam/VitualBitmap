package com.project4.virtualbitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static java.lang.Math.log;

/**
 * Implementation of the Virtual Bitmap.
 *
 * @param <T> type of element to insert.
 */
public class VirtualBitMap<T> {
    final int bitmapSize;
    final int virtualBitmapSize;
    final boolean[] physicalBitmap;

    private final long[] hashes;

    public VirtualBitMap(int bitmapSize, int virtualBitmapSize) {
        this.bitmapSize = bitmapSize;
        this.virtualBitmapSize = virtualBitmapSize;
        physicalBitmap = new boolean[bitmapSize];
        hashes = new long[virtualBitmapSize];
        initRandomArray();
    }

    public void addFlow(String flowID, List<T> elements) throws IOException {
        for (T element : elements) {
            addFlow(flowID, element);
        }
    }

    public void addFlow(String flowID, T element) throws IOException {
        int index = masterHashSetGen(flowID, (String) element);
        physicalBitmap[index] = true;
    }

    public int getNumberOfZeroesInPhysicalBitmap() {
        int count = 0;
        for (boolean value : physicalBitmap) {
            if (!value) {
                count++;
            }
        }
        return count;
    }

    public int getNumberOfZeroesInVirtualBitmap(String flowID, List<String> destinations) throws IOException {
        int count = 0;
        for (int i = 0; i < virtualBitmapSize; i++) {
            long ipAddressToLong = convertIPAddressToLong(flowID);
            long temp = ipAddressToLong ^ hashes[i];
            int hashedFlowID = customHash(String.valueOf(temp));
            int index = hashedFlowID % bitmapSize;
            if (!physicalBitmap[index]) {
                count++;
            }
        }
        return count;
    }

    public int getEstimate(String flowID, List<String> destinations) throws IOException {
        int numberOfZeroesInPhysicalBitmap = getNumberOfZeroesInPhysicalBitmap();
        int numberOfZeroesInVirtualBitmap = getNumberOfZeroesInVirtualBitmap(flowID, destinations);
        double Vb = numberOfZeroesInPhysicalBitmap / (double) bitmapSize;
        double Vf = numberOfZeroesInVirtualBitmap / (double) virtualBitmapSize;
        return (int) (virtualBitmapSize * log(Vb) + (-1 * virtualBitmapSize * log(Vf)));
    }

    private int customHash(Object name) throws IOException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512/256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.reset();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(name);
        md.update(out.toByteArray());
        byte[] hash = md.digest();
        int result = getInt(hash);
        return result & 0x7fffffff;

    }

    private static int getInt(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    public int masterHashSetGen(String src, String dst) throws IOException {
        long ipAddressToLong = convertIPAddressToLong(src);
        int generatedIndex = genIndex(virtualBitmapSize, dst);
        Long tempValue = ipAddressToLong ^ hashes[generatedIndex];
        int hashedSrc = customHash(String.valueOf(tempValue));
        return hashedSrc % bitmapSize;
    }

    public static long convertIPAddressToLong(String ipAddress) {
        String[] ipAddressInArray = ipAddress.split("\\.");
        long result = 0;
        for (int i = 0; i < ipAddressInArray.length; i++) {
            int power = 3 - i;
            int ip = Integer.parseInt(ipAddressInArray[i]);
            result += ip * Math.pow(256, power);
        }
        return result;
    }

    public static int genIndex(int size, String dst) {
        long ipDecimalDst = convertIPAddressToLong(dst);
        long hashedDecimal = hashLong(ipDecimalDst);
        return (int) Math.abs((hashedDecimal % size));
    }

    private void initRandomArray() {
        for (int i = 0; i < virtualBitmapSize; i++) {
            int randNumber = hashLong(i);
            hashes[i] = randNumber;
        }
    }

    private static int hashLong(long data) {
        int m = 0x5bd1e995;
        int r = 24;
        int h = 0;
        int k = (int) data * m;
        k ^= k >>> r;
        h ^= k * m;
        k = (int) (data >> 32) * m;
        k ^= k >>> r;
        h *= m;
        h ^= k * m;
        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;
        return h;
    }
}
