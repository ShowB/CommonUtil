package com.snet.smore.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

@Slf4j
public class Environment {

    private Properties properties;

    Environment() {
        loadProperty();
    }

    /**
     * 환경정보 파일을 읽는다.
     */
    public void loadProperty() {
        if (properties != null) {
            return;
        }

        String uri = Constant.WORK_DIR + Constant.FILE_SEPARATOR + "config" + Constant.FILE_SEPARATOR + "environment.properties";
        properties = new Properties();

        try (FileChannel channel = FileChannel.open(Paths.get(uri), StandardOpenOption.READ);
             InputStream inputStream = Channels.newInputStream(channel)) {
            properties.load(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            loadProperty();
        }

        try (FileInputStream fis = new FileInputStream(uri)) {
            properties.load(fis);
        } catch (Exception e) {
            log.error(e.getMessage());
            loadProperty();
        }
    }

    /**
     * 환경정보 식별값에 해당하는 정보를 retrun한다.
     * @param key 요청 정보의 식별값
     * @return String 요청 정보
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public Properties getProperties() {
        return this.properties;
    }

}