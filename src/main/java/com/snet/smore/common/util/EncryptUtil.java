package com.snet.smore.common.util;

import com.snet.smore.common.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class EncryptUtil {

    /**
     * SUN 의 BASE64Encode 를 이용한 encode.
     *
     * @param strEncode Encoding 대상
     * @return String Encoding 결과값
     * @throws Exception
     */
    public static String getEncrypt(String strEncode) {

        String result = null;

        if (strEncode == null || strEncode.trim().length() == 0)
            return null;

        try {

            BASE64Encoder b64e = new BASE64Encoder();

            ByteArrayInputStream bais = new
                    ByteArrayInputStream(strEncode.getBytes());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = (byte[]) null;

            b64e.encodeBuffer(bais, baos);
            bytes = baos.toByteArray();
            result = (new String(bytes)).trim();

            //result = new String(org.apache.commons.codec.binary.Base64.encodeBase64(strEncode.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * SUN 의 BASE64Decoder 를 이용한 decode.
     *
     * @param strDecode Decoding 대상
     * @return String Decoding 결과값
     * @throws Exception
     */
    public static String getDecrypt(String strDecode) {
        String result = null;

        if (strDecode == null || strDecode.trim().length() == 0)
            return null;

        try {

            BASE64Decoder b64d = new BASE64Decoder();
            ByteArrayInputStream bais = new ByteArrayInputStream(strDecode
                    .getBytes());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes;

            b64d.decodeBuffer(bais, baos);
            bytes = baos.toByteArray();
            result = new String(bytes);

            //result = new String(org.apache.commons.codec.binary.Base64.decodeBase64(strDecode));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
