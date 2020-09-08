package com.snet.smore.common.util;

import org.junit.Ignore;
import org.junit.Test;

public class SnycTest {
    Integer a;
    @Test
    @Ignore
    public void test() {
        synchronized (a) {
            a = 3;
            System.out.println(a);
        }

    }
}
