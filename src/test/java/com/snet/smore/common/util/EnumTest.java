package com.snet.smore.common.util;

import com.snet.smore.common.constant.FileStatusPrefix;
import org.junit.Test;

public class EnumTest {
    @Test
    public void test() {
        FileStatusPrefix[] values = FileStatusPrefix.values();
        for (FileStatusPrefix e : values) {
            System.out.println(e.getPrefix());
        }
    }
}
