package com.wangcong.warehousefirewarning.utils;

public class CRCValidate {

    private static final String TAG = "CRCValidate";

    /**
     * /计算CRC�?
     * 第一个参数：要计算的buff
     * 第一个参数：起始位置
     * 第三个参数：计算长度
     */
    public static final int[] calculateCRC(byte[] data, int offset, int len) {

        int[] crc = {0xFF, 0xFF};

        int nextByte = 0;

        int uIndex; /* will index into CRC lookup *//* table */

		/* pass through message buffer */

        for (int i = offset; i < len && i < data.length; i++) {

            nextByte = 0xFF & ((int) data[i]);

            uIndex = crc[0] ^ nextByte; // *puchMsg++; /* calculate the CRC */

            crc[0] = crc[1] ^ auchCRCHi[uIndex];

            crc[1] = auchCRCLo[uIndex];

        }

        return crc;

    }// calculateCRC

    /**
     * /计算一位CRC�?，按位异或
     * 第一个参数：要计算的buff
     * 第一个参数：起始位置
     * 第三个参数：计算长度
     */
    public static final int calculateSingleCRC(byte[] data, int offset, int len) {

        int crc = data[offset];

		/* pass through message buffer */

        for (int i = offset + 1; i < (offset + len) && i < data.length; i++) {

            crc = crc ^ ((int) data[i]); // *puchMsg++; /* calculate the CRC */

        }

        return crc;

    }

    /**
     * /计算CRC�? 第一个参数：要计算的calcStr
     * // "01 10 00 5e 00 02 04 11 12 13 14"得到crc="df 19"
     */
    public static final String calculateStringCRC(String calcStr) {

        String crcStr = null;
        if (HexStrConvertUtil.isHex(calcStr)) {
            byte[] data = HexStrConvertUtil.hexStringToByte(calcStr);
            int[] crcIntArr = CRCValidate.calculateCRC(data, 0, data.length);
            byte[] crcByteArr1 = {0, (byte) crcIntArr[0]};
            byte[] crcByteArr2 = {0, (byte) crcIntArr[1]};
            String crcStr1 = HexStrConvertUtil.bytesToHexString(crcByteArr1).substring(2);
            String crcStr2 = HexStrConvertUtil.bytesToHexString(crcByteArr2).substring(2);
            crcStr = crcStr1 + " " + crcStr2;
        }

        return crcStr.toLowerCase();

    }

	/* Table of CRC values for high-order byte */

    private final static short[] auchCRCHi = {

            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,

            0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,

            0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,

            0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,

            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,

            0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,

            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,

            0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,

            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,

            0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,

            0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,

            0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,

            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,

            0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,

            0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,

            0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,

            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,

            0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,

            0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,

            0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,

            0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,

            0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,

            0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,

            0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,

            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,

            0x80, 0x41, 0x00, 0xC1, 0x81, 0x40

    };

	/* Table of CRC values for low-order byte */

    private final static short[] auchCRCLo = {

            0x00, 0xC0, 0xC1, 0x01, 0xC3, 0x03, 0x02, 0xC2, 0xC6, 0x06,

            0x07, 0xC7, 0x05, 0xC5, 0xC4, 0x04, 0xCC, 0x0C, 0x0D, 0xCD,

            0x0F, 0xCF, 0xCE, 0x0E, 0x0A, 0xCA, 0xCB, 0x0B, 0xC9, 0x09,

            0x08, 0xC8, 0xD8, 0x18, 0x19, 0xD9, 0x1B, 0xDB, 0xDA, 0x1A,

            0x1E, 0xDE, 0xDF, 0x1F, 0xDD, 0x1D, 0x1C, 0xDC, 0x14, 0xD4,

            0xD5, 0x15, 0xD7, 0x17, 0x16, 0xD6, 0xD2, 0x12, 0x13, 0xD3,

            0x11, 0xD1, 0xD0, 0x10, 0xF0, 0x30, 0x31, 0xF1, 0x33, 0xF3,

            0xF2, 0x32, 0x36, 0xF6, 0xF7, 0x37, 0xF5, 0x35, 0x34, 0xF4,

            0x3C, 0xFC, 0xFD, 0x3D, 0xFF, 0x3F, 0x3E, 0xFE, 0xFA, 0x3A,

            0x3B, 0xFB, 0x39, 0xF9, 0xF8, 0x38, 0x28, 0xE8, 0xE9, 0x29,

            0xEB, 0x2B, 0x2A, 0xEA, 0xEE, 0x2E, 0x2F, 0xEF, 0x2D, 0xED,

            0xEC, 0x2C, 0xE4, 0x24, 0x25, 0xE5, 0x27, 0xE7, 0xE6, 0x26,

            0x22, 0xE2, 0xE3, 0x23, 0xE1, 0x21, 0x20, 0xE0, 0xA0, 0x60,

            0x61, 0xA1, 0x63, 0xA3, 0xA2, 0x62, 0x66, 0xA6, 0xA7, 0x67,

            0xA5, 0x65, 0x64, 0xA4, 0x6C, 0xAC, 0xAD, 0x6D, 0xAF, 0x6F,

            0x6E, 0xAE, 0xAA, 0x6A, 0x6B, 0xAB, 0x69, 0xA9, 0xA8, 0x68,

            0x78, 0xB8, 0xB9, 0x79, 0xBB, 0x7B, 0x7A, 0xBA, 0xBE, 0x7E,

            0x7F, 0xBF, 0x7D, 0xBD, 0xBC, 0x7C, 0xB4, 0x74, 0x75, 0xB5,

            0x77, 0xB7, 0xB6, 0x76, 0x72, 0xB2, 0xB3, 0x73, 0xB1, 0x71,

            0x70, 0xB0, 0x50, 0x90, 0x91, 0x51, 0x93, 0x53, 0x52, 0x92,

            0x96, 0x56, 0x57, 0x97, 0x55, 0x95, 0x94, 0x54, 0x9C, 0x5C,

            0x5D, 0x9D, 0x5F, 0x9F, 0x9E, 0x5E, 0x5A, 0x9A, 0x9B, 0x5B,

            0x99, 0x59, 0x58, 0x98, 0x88, 0x48, 0x49, 0x89, 0x4B, 0x8B,

            0x8A, 0x4A, 0x4E, 0x8E, 0x8F, 0x4F, 0x8D, 0x4D, 0x4C, 0x8C,

            0x44, 0x84, 0x85, 0x45, 0x87, 0x47, 0x46, 0x86, 0x82, 0x42,

            0x43, 0x83, 0x41, 0x81, 0x80, 0x40

    };

    /*
     * 判断CRC值是否正确--CRC校验
     */
    public static Boolean isCRCConfig(byte[] buff) {
        int offset = 0;
        int len = buff.length - 2;
        // 根据前面的（地址、指令、数据，除了校验码）来
        // 计算CRC值,(第一个参数：要计算的buff 第一个参数：起始位 第三个参数：计算长度)
        int[] crc = CRCValidate.calculateCRC(buff, offset, len);
        // 比较buff的最后两位CRC码是否等于计算出来的CRC码
        if ((byte) crc[0] == buff[buff.length - 2] & (byte) crc[1] == buff[buff.length - 1]) {
            return true;
        } else {
            return false;
        }
    }

//	public static void main(String[] args) throws Exception {
//		String str = "0203002A0001";
//		byte[] tx_buff = Hex.decodeHex(str.toCharArray());
//		int[] result = CRCValidate.calculateCRC(tx_buff, 0, tx_buff.length);
//		System.out.println(result[0] + " " + result[1]);
//		byte[] b_result = new byte[2];
//		b_result[0] = (byte) result[0];
//		b_result[1] = (byte) result[1];
//		System.out.println(Hex.encodeHexString(b_result));
//	}
}
