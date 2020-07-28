package com.snet.smore.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class LogTest {

    @Test
    public void logTest() {

        log.info("This is just info !! ahahaha!!!");
        log.debug("aaa... this is debug T^T");
        log.error("this is error !!!!!!!!!!!!!!!!!!!");
    }
}
