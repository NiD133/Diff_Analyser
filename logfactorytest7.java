package org.apache.ibatis.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.commons.JakartaCommonsLoggingImpl;
import org.apache.ibatis.logging.jdk14.Jdk14LoggingImpl;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class LogFactoryTestTest7 {

    @AfterAll
    static void restore() {
        LogFactory.useSlf4jLogging();
    }

    private void logSomething(Log log) {
        log.warn("Warning message.");
        log.debug("Debug message.");
        log.error("Error message.");
        log.error("Error with Exception.", new Exception("Test exception."));
    }

    @Test
    void shouldUseNoLogging() {
        LogFactory.useNoLogging();
        Log log = LogFactory.getLog(Object.class);
        logSomething(log);
        assertEquals(log.getClass().getName(), NoLoggingImpl.class.getName());
    }
}
