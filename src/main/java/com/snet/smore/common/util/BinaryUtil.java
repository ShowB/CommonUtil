package com.snet.smore.common.util;

public class BinaryUtil {
    /**
     * 2진수값을 10진수로 변경
     *
     * @param bytes bytes
     * @param row row
     * @param col col
     * @return int
     */
    public static int oneBinaryToDecimal(byte[] bytes, int row, int col) {
        String binary = "";
        binary = byteArrayToBin(bytes[row], col) + binary;

        return Integer.parseInt(binary, 2); // 10진수값 변경
    }

    /**
     * 특정영역의 2진수값을 10진수로 변경
     *
     * @param bytes bytes
     * @param row row
     * @param startCol startCol
     * @param endCol endCol
     * @return int
     */
    public static int binaryToDecimal(byte[] bytes, int row, int startCol, int endCol) {
        StringBuilder binary = new StringBuilder();
        for (int i = startCol; i <= endCol; i++) {
            binary.insert(0, byteArrayToBin(bytes[row], i));
        }
        return Integer.parseInt(binary.toString(), 2); // 10진수값 변경
    }

    /**
     * 멀티Row의 2진수값을 10진수로 변경
     *
     * @param bytes bytes
     * @param startRow startRow
     * @param endRow endRow
     * @return int
     */
    public static int multiByteToDecimal(byte[] bytes, int startRow, int endRow) {
        StringBuilder binary = new StringBuilder();
        for (int i = startRow; i <= endRow; i++) {
            for (int j = 0; j <= 7; j++) {
                binary.insert(0, byteArrayToBin(bytes[i], j));
            }
        }
//        int decimal = Integer.parseInt(binary, 2); // 10진수값 변경
        // COMUtil.debug("multiByteToDecimal binary ::::::::::" + binary);
        // COMUtil.debug("multiByteToDecimal decimal ::::::::::" + decimal);
        return Integer.parseInt(binary.toString(), 2); // 10진수값 변경
    }

    /**
     * 멀티Row의 2진수값을 10진수로 변경
     *
     * @param bytes bytes
     * @param startRow startRow
     * @param endRow endRow
     * @return Long
     */
    public static long multiByteToDecimalLong(byte[] bytes, int startRow, int endRow) {
        StringBuilder binary = new StringBuilder();
        for (int i = startRow; i <= endRow; i++) {
            for (int j = 0; j <= 7; j++) {
                binary.insert(0, byteArrayToBin(bytes[i], j));
            }
        }
//        Long decimal = Long.parseLong(binary, 2); // 10진수값 변경

        /*
         * int decimal = 0; for (int i = 0; i < binary.length(); i++){ decimal
         * += Math.pow(2, ((binary.length() - 1) - i)) *
         * Integer.parseInt(Character.toString(binary.charAt(i))); }
         */
        // COMUtil.debug("multiByteToDecimal decimal ::::::::::" + decimal);
        return Long.parseLong(binary.toString(), 2); // 10진수값 변경
    }

    /**
     * 2바이트 Binary의 2의 보수 역산(음수 처리)
     *
     * @param bytes bytes
     * @param startRow
     * @return
     */
    public static long revert2Complement(byte[] bytes, int startRow, int endRow) {
        long result;
        StringBuilder binary = new StringBuilder();

        for (int i = startRow; i <= endRow; i++) {
            for (int j = 0; j <= 7; j++) {
                binary.insert(0, byteArrayToBin(bytes[i], j));
            }
        }

        if (binary.toString().startsWith("1")) {
            // 2의 보수란...
            // 1의 보수 값에 1을 더한 값이므로
            // 역산을 취하려면 2의 보수 값에서 1을 뺀 뒤, 다시 1의 보수를 취하여 10진수로 변환한다.

            // 1. 10진수로 변환해서 1을 뺀다.
            long val = Long.parseLong(binary.toString(), 2);
            val--;

            // 2. 다시 16bit 2진수로 변환
            int base = 1;
            int diff = endRow - startRow;

            for (int i = 0; i <= diff; i++) {
                base = base * 16 * 16;  // row(byte) 갯수에 맞춰서 bit 자리 생성해주기
            }

            String byteArray = Long.toBinaryString(base | val).substring(1);

            StringBuilder cmplBinary = new StringBuilder(byteArray);

            // 3. 변환한 2진수의 값에서 1의 보수를 취한다.
            for (int i = 0; i < byteArray.length(); i++) {
                if (cmplBinary.substring(i, i + 1).equals("1"))
                    cmplBinary.replace(i, i + 1, "0");
                else
                    cmplBinary.replace(i, i + 1, "1");
            }

            // 4. 10진수로 변환
            result = Long.parseLong(cmplBinary.toString(), 2);

            // 5. 최상위 bit가 1일 경우에만 이 조건문을 타므로, 여기 들어오는 값들은 모두 음수 처리를 해줘야 함.
            result = result * -1;
        } else {
            // 최상위 bit가 1이 아니면, 그냥 10진수로 변환하여 return
            result = Long.parseLong(binary.toString(), 2);
        }

        return result;
    }

    /**
     * 특정영역의 2진수값을 Hexadecimal로 변경
     *
     * @param bytes bytes
     * @param row row
     * @param startCol startCol
     * @param endCol endCol
     * @return String
     */
    public static String binaryToHex(byte[] bytes, int row, int startCol, int endCol) {
        StringBuilder binary = new StringBuilder();
        for (int i = startCol; i <= endCol; i++) {
            binary.insert(0, byteArrayToBin(bytes[row], i));
        }
//        String hexa = Long.toHexString(Long.parseLong(binary, 2)); // HEX 변경
        // COMUtil.debug("binaryToDecimal binary :::::::: " + binary);
        // COMUtil.debug("binaryToDecimal hexa :::::::: " + hexa);
        return Long.toHexString(Long.parseLong(binary.toString(), 2)); // HEX 변경
    }

    /**
     * 멀티Row의 2진수값을 Hexadecimal로 변경
     *
     * @param bytes bytes
     * @param startRow startRow
     * @param endRow endRow
     * @return String
     */
    public static String multiByteToHex(byte[] bytes, int startRow, int endRow) {
        StringBuilder binary = new StringBuilder();
        for (int i = startRow; i <= endRow; i++) {
            for (int j = 0; j <= 7; j++) {
                binary.insert(0, byteArrayToBin(bytes[i], j));
            }
        }
//        String hexa = Long.toHexString(Long.parseLong(binary, 2)); // HEX 변경
        // COMUtil.debug("multiByteToDecimal binary ::::::::::" + binary);
        // COMUtil.debug("multiByteToDecimal hexa ::::::::::" + hexa);
        return Long.toHexString(Long.parseLong(binary.toString(), 2)); // HEX 변경
    }

    /**
     * 2진수값을 sign decimal로 변경
     *
     * @param binary binary
     * @return long
     */
    public static long signedBinaryToDecimal(String binary) {

        long[] obj = new long[64];// 8byte
        int objI = 0;
        for (int z = 63; z >= 0; z--) {
            obj[objI++] = (long) Math.pow(2, 63 - z);
        }
        long decimal = 0;
        int[] arryBinary = new int[binary.length()];
        for (int i = 0; i < binary.length(); i++) {
            arryBinary[i] = Integer.parseInt(binary.charAt(i) + "");
        }

        int k;

        for (int i = 1; i < binary.length(); i++) {
            k = (int) Math.pow(2, (binary.length() - 1) - i);

            decimal = decimal + (k * arryBinary[i]);
        }

        if (arryBinary[0] == 1) {
            decimal = obj[binary.length() - 1] - decimal;
            decimal = decimal * (-1);
        }

        return decimal;
    }

    /**
     *
     * @param data data
     * @param idx idx
     * @return int
     */
    public static int byteArrayToBin(byte data, int idx) {
        Integer[] obj = {1, 2, 4, 8, 16, 32, 64, 128};
        int result = obj[idx] & data;
        return result == 0 ? 0 : 1;
    }
}
