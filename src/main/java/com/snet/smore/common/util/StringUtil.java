package com.snet.smore.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil {

    public static String dbStr(int num) {
        return dbStr(num + "");
    }

    public static String dbStr(String str) {
        if (str == null) {
            return "null";
        } else {
            return "'" + str + "'";
        }
    }

    public static boolean isNull(String str) {
        return str == null;
    }

    public static boolean isNotNull(String str) {
        return str != null;
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !"".equals(str);
    }

    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    public static boolean isNotBlank(String str) {
        return str != null && !"".equals(str.trim());
    }

    public static String toUnderscoreCase(String str) {
        StringBuilder underscoreSb = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            String currStr = String.valueOf(str.charAt(i));

            if (i == 0) {
                underscoreSb.append(currStr.toUpperCase());
                continue;
            }

            if (currStr.toUpperCase().equals(currStr)) {
                try {
                    Integer.parseInt(currStr);
                } catch (Exception ex) {
                    underscoreSb.append("_");
                }

                underscoreSb.append(currStr.toUpperCase());

            } else {
                underscoreSb.append(currStr.toUpperCase());
            }
        }

        return underscoreSb.toString();
    }

    public static String toCamelCase(String str) {
        StringBuilder sb = new StringBuilder();

        String[] splits = str.toLowerCase().split("_");

        int i = 0;
        for (String e : splits) {
            if (i++ == 0)
                sb.append(e);
            else
                sb.append(e.substring(0, 1).toUpperCase()).append(e.substring(1));
        }

        return sb.toString();
    }

}