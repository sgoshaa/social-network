package ru.skillbox.diplom.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.skillbox.diplom.service.enums.LoggerLevel;
import ru.skillbox.diplom.service.enums.LoggerValue;


public interface SocialNetworkService {

    default void log(Class<? extends SocialNetworkService> service,
                     LoggerLevel level,
                     String method,
                     LoggerValue value,
                     String description) {
        Logger logger = LogManager.getLogger(service);
        switch (level) {
            case INFO: {
                logger.info(method + value.description + description);
                break;
            }
            case WARN: {
                logger.warn(method + value.description + description);
                break;
            }
            case DEBUG: {
                logger.debug(method + value.description + description);
                break;
            }
            case TRACE: {
                logger.trace(method + value.description + description);
                break;
            }
            case ERROR: {
                logger.error(method + value.description + description);
            }
        }
    }
}
