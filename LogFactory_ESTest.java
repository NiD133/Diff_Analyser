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

    @Test(timeout = 4000)
    public void testGetLogWithValidClassName() throws Throwable {
        // Test that a valid class name returns a non-null Log instance
        Log log = LogFactory.getLog("org.apache.ibatis.logging.LogFactory");
        assertNotNull("Log should not be null for a valid class name", log);
    }

    @Test(timeout = 4000)
    public void testGetLogWithNullClass() throws Throwable {
        // Test that passing null as a class throws a NullPointerException
        try {
            LogFactory.getLog((Class<?>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.logging.LogFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetLogWithValidClass() throws Throwable {
        // Test that a valid class returns a non-null Log instance
        Log log = LogFactory.getLog(Object.class);
        assertNotNull("Log should not be null for a valid class", log);
    }

    @Test(timeout = 4000)
    public void testUseCustomLoggingWithInvalidClass() throws Throwable {
        // Test that using an invalid custom logging class throws a RuntimeException
        try {
            LogFactory.useCustomLogging(JakartaCommonsLoggingImpl.class);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.logging.LogFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testUseLog4J2LoggingThrowsException() throws Throwable {
        // Test that using Log4J2 logging throws a RuntimeException
        try {
            LogFactory.useLog4J2Logging();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.logging.LogFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testUseSlf4jLogging() throws Throwable {
        // Test that using SLF4J logging does not throw an exception
        LogFactory.useSlf4jLogging();
    }

    @Test(timeout = 4000)
    public void testGetLogWithNullString() throws Throwable {
        // Test that passing null as a string throws a RuntimeException
        try {
            LogFactory.getLog((String) null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.logging.LogFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testUseCommonsLoggingThrowsException() throws Throwable {
        // Test that using Commons Logging throws a RuntimeException
        try {
            LogFactory.useCommonsLogging();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.logging.LogFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testUseJdkLogging() throws Throwable {
        // Test that using JDK logging does not throw an exception
        LogFactory.useJdkLogging();
    }

    @Test(timeout = 4000)
    public void testUseLog4JLoggingThrowsException() throws Throwable {
        // Test that using Log4J logging throws a RuntimeException
        try {
            LogFactory.useLog4JLogging();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.logging.LogFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testUseStdOutLogging() throws Throwable {
        // Test that using standard output logging does not throw an exception
        LogFactory.useStdOutLogging();
    }

    @Test(timeout = 4000)
    public void testUseNoLogging() throws Throwable {
        // Test that using no logging does not throw an exception
        LogFactory.useNoLogging();
    }
}