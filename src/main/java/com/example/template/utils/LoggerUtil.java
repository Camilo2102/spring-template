package com.example.template.utils;

import com.example.template.constants.LogConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void petitionLog(String... args) {
        String log = StringUtil.getFromTemplate(LogConstants.LOG_TEMPLATE_PETITION, args);
        logger.info(log);
    }
}
