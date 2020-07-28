package com.snet.smore.common.util;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {

    @Test
    public void test() {
        String camelCase = StringUtil.toCamelCase("SCOTT_TIGER_AND_YOU_2_A");

        Assert.assertEquals(camelCase, "scottTigerAndYou2A");
    }
}