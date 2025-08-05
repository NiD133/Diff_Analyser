package org.apache.ibatis.logging;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.logging.commons.JakartaCommonsLoggingImpl;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class LogFactory_ESTest extends LogFactory_ESTest_scaffolding {

    // Tests for getLog() method ==============================================
    
    @Test(timeout = 4000)
    public void getLog_WithValidClassName_ReturnsLogger() {
        Log log = LogFactory.getLog("org.apache.ibatis.logging.LogFactory");
        assertNotNull("Logger should be created for valid class name", log);
    }

    @Test(timeout = 4000)
    public void getLog_WithValidClass_ReturnsLogger() {
        Log log = LogFactory.getLog(Object.class);
        assertNotNull("Logger should be created for valid Class object", log);
    }

    @Test(timeout = 4000)
    public void getLog_WithNullClass_ThrowsNullPointerException() {
        try {
            LogFactory.getLog((Class<?>) null);
            fail("Expected NullPointerException when class is null");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void getLog_WithNullString_ThrowsRuntimeException() {
        try {
            LogFactory.getLog((String) null);
            fail("Expected RuntimeException when logger name is null");
        } catch (RuntimeException e) {
            assertTrue("Exception should mention logger creation failure",
                    e.getMessage().contains("Error creating logger for logger null"));
        }
    }

    // Tests for custom logging implementations ===============================
    
    @Test(timeout = 4000)
    public void useCustomLogging_WithInvalidImplementation_ThrowsRuntimeException() {
        try {
            LogFactory.useCustomLogging(JakartaCommonsLoggingImpl.class);
            fail("Expected RuntimeException for unsupported custom logger");
        } catch (RuntimeException e) {
            assertTrue("Exception should mention logging implementation error",
                    e.getMessage().contains("Error setting Log implementation"));
        }
    }

    // Tests for standard logging implementations =============================
    
    @Test(timeout = 4000)
    public void useSlf4jLogging_ConfiguresWithoutError() {
        LogFactory.useSlf4jLogging();
    }

    @Test(timeout = 4000)
    public void useJdkLogging_ConfiguresWithoutError() {
        LogFactory.useJdkLogging();
    }

    @Test(timeout = 4000)
    public void useStdOutLogging_ConfiguresWithoutError() {
        LogFactory.useStdOutLogging();
    }

    @Test(timeout = 4000)
    public void useNoLogging_ConfiguresWithoutError() {
        LogFactory.useNoLogging();
    }

    // Tests for failing logging implementations ==============================
    
    @Test(timeout = 4000)
    public void useLog4J2Logging_WhenUnavailable_ThrowsRuntimeException() {
        try {
            LogFactory.useLog4J2Logging();
            fail("Expected RuntimeException when Log4J2 is unavailable");
        } catch (RuntimeException e) {
            assertTrue("Exception should mention implementation error",
                    e.getMessage().contains("Error setting Log implementation"));
        }
    }

    @Test(timeout = 4000)
    public void useCommonsLogging_WhenUnavailable_ThrowsRuntimeException() {
        try {
            LogFactory.useCommonsLogging();
            fail("Expected RuntimeException when Commons Logging is unavailable");
        } catch (RuntimeException e) {
            assertTrue("Exception should mention implementation error",
                    e.getMessage().contains("Error setting Log implementation"));
        }
    }

    @Test(timeout = 4000)
    public void useLog4JLogging_WhenUnavailable_ThrowsRuntimeException() {
        try {
            LogFactory.useLog4JLogging();
            fail("Expected RuntimeException when Log4J is unavailable");
        } catch (RuntimeException e) {
            assertTrue("Exception should contain missing class information",
                    e.getMessage().contains("org/apache/log4j/Priority"));
        }
    }
}