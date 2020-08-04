package com.snet.smore.common.constant;

public enum FileStatusPrefix {
    COMPLETE("cmpl_"),
    ERROR("err_"),
    TEMP("tmp_");

    private String prefix;

    FileStatusPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
