package com.snet.smore.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnvManager {

    private static EnvManager instance = null;

    private final Environment env;

    private EnvManager() {
        env = new Environment();
    }

    public static EnvManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    public static Environment getEnvironment() {
        return getInstance().env;
    }

    public static String getProperty(String propertyName) {
        return getEnvironment().getProperty(propertyName);
    }

    public static int getProperty(String propertyName, int defaultValue) {
        try {
            return Integer.parseInt(EnvManager.getProperty(propertyName));
        } catch (Exception e) {
            log.info("Cannot convert value [{}}]. System will be set default value: {}", propertyName, defaultValue);
            return defaultValue;
        }
    }

    public static String getProperty(String propertyName, String defaultValue) {
        String value = EnvManager.getProperty(propertyName);

        if (StringUtil.isNull(value)) {
            log.info("Cannot convert value [{}}]. System will be set default value: {}", propertyName, defaultValue);
            return defaultValue;
        } else {
            return value;
        }
    }

    public static void reload() {
        LazyHolder.reload();
    }

    private static class LazyHolder {
        private static EnvManager INSTANCE = new EnvManager();

        private static void reload() {
            LazyHolder.INSTANCE = new EnvManager();
        }
    }
}