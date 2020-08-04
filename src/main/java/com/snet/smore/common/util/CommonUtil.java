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
public class CommonUtil {

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

    /**
     * yyyy-MM-dd HH:mm:ss 포멧의 날짜를 yyyyMMddHHmmss포멧으로 변경
     * @param sDate 날짜
     * @return String
     */
    public static String getStringFormatString(String sDate) {
        if (sDate == null || sDate.trim().length() == 0) return "";

        String srcFormat = "yyyyMMddHHmmss";
        String taregetFormat = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat sdf = new SimpleDateFormat(srcFormat);
        ParsePosition pos = new ParsePosition(0);
        Date date = sdf.parse(sDate, pos);
        return new SimpleDateFormat(taregetFormat).format(date);

    }

    public static List<File> getWholeFiles(String source) {
        return getHierarchyFiles(source, new ArrayList<File>());
    }

    private static List<File> getHierarchyFiles(String source, List<File> files) {
        try {
            File root = new File(source);

            if (!root.exists())
                return files;

            File[] currentFiles = root.listFiles();

            if (currentFiles == null)
                return files;

            for (File e : currentFiles) {
                if (e.isFile()) {
                    files.add(e);
                } else {
                    // Directory일 경우, 자기 자신 함수 재귀 호출
                    getHierarchyFiles(e.getCanonicalPath(), files);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return files;
    }


    public static String getFileExt(File file) {
        if (file == null)
            return "";

        if (!file.exists())
            return "";

        if (file.isDirectory())
            return "";

        int index = file.getName().lastIndexOf(".") + 1;

        if (index == 0)
            return "";

        return file.getName().substring(index);
    }

    public static void deleteSubPath(File root) {
        if (root == null)
            return;

        if (!root.exists())
            return;

        File[] files = root.listFiles();

        if (files == null)
            return;

        for (File e : files) {
            if (e.isFile()) e.delete();
            else deleteSubPath(new File(e.getPath()));

            e.delete();
        }
    }

    public static String getFileNameOnly(File file) {
        if (file == null)
            return "";

        if (!file.exists())
            return "";

        if (file.isDirectory())
            return "";

        int index = file.getName().lastIndexOf(".");

        if (index == -1)
            return file.getName();

        return file.getName().substring(0, index);
    }

    public static void extractZip(File zipFile) {
        if (!"zip".equals(CommonUtil.getFileExt(zipFile)))
            return;

        if (zipFile.getName().startsWith("cmpl_"))
            return;

        if (zipFile.getName().startsWith("err_"))
            return;

        System.out.println(zipFile.getPath() + " ---> 압축 풀기 시작 !");

        Environment env = EnvManager.getEnvironment();
        String dest = env.getProperty("TEMP_FOLDER");

        if (!(dest.charAt(dest.length() - 1) == '/'))
            dest += "/";

        String zipRoot = dest + CommonUtil.getFileNameOnly(zipFile) + "/";

        byte[] data = new byte[1024];
        ZipEntry entry;
        ZipInputStream zipstream = null;
        FileOutputStream out = null;

        boolean isSuccess;

        try {
            zipstream = new ZipInputStream(new FileInputStream(zipFile));

            while ((entry = zipstream.getNextEntry()) != null) {

                int read;
                File entryFile;

                int lastIndex = entry.getName().lastIndexOf("/");

                if (lastIndex != -1) {
                    String dir = entry.getName().substring(0, lastIndex);
                    File parent = new File(zipRoot + dir);
                    if (!parent.exists())
                        parent.mkdirs();
                }

                if (entry.isDirectory()) {
                    File folder = new File(zipRoot + entry.getName());
                    if (!folder.exists())
                        folder.mkdirs();

                    continue;
                } else {
                    entryFile = new File(zipRoot + entry.getName());
                }

                if (!entryFile.exists())
                    entryFile.createNewFile();

                out = new FileOutputStream(entryFile);
                while ((read = zipstream.read(data, 0, 1024)) != -1)
                    out.write(data, 0, read);

                zipstream.closeEntry();

            }

            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
            log.error(zipFile.getPath() + " --> 압축 해제 실패!");
        } finally {
            try {
                if (out != null) out.close();
                if (zipstream != null) zipstream.close();
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }

        if (isSuccess)
            zipFile.renameTo(new File(zipFile.getParent() + Constant.FILE_SEPARATOR + "cmpl_" + zipFile.getName()));
        else
            zipFile.renameTo(new File(zipFile.getParent() + Constant.FILE_SEPARATOR + "err_" + zipFile.getName()));
    }

    public static void removeFilePrefix(List<File> files, String... prefix) {
        for (File e : files) {
            for (String e2 : prefix)
                if (e.getName().startsWith(e2)) {
                    String tobeFilePath = e.getParent() + Constant.FILE_SEPARATOR + e.getName().replace(e2, "");
                    if (e.renameTo(new File(tobeFilePath)))
                        log.info(e.getPath() + " ---> " + tobeFilePath + " ::: 변경 완료!");
                }
        }
    }

    public static boolean isDateFormat(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        try {
            Date date = sdf.parse(dateStr);
            if (sdf.format(date).equals(dateStr)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
