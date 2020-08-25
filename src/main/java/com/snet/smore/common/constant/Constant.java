package com.snet.smore.common.constant;

import java.text.SimpleDateFormat;

public class Constant {
    public final static String LOG_DIR = "/logs";
    public final static String COLUMN_SEPARATOR = "|";
    public final static String PROPERTY_SEPARATOR = ",";
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");

    public final static String WORK_DIR = System.getProperty("user.dir");
    public final static String FILE_SEPARATOR = System.getProperty("file.separator");

    public final static SimpleDateFormat sdf1 = new SimpleDateFormat("yyMMdd_HHmmss");

    public final static String CALLABLE_RESULT_SUCCESS = "success";
    public final static String CALLABLE_RESULT_FAIL = "fail";

    public final static String AGENT_TYPE_EXTRACTOR = "Extractor";
    public final static String AGENT_TYPE_TRANSFORMER = "Transformer";
    public final static String AGENT_TYPE_LOADER = "Loader";

    public final static String YN_Y = "Y";
    public final static String YN_N = "N";
}
