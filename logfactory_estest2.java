package org.apache.ibatis.logging;

import org.junit.Test;

/**
 * Test suite for the {@link LogFactory} class.
 * This class verifies the correct behavior of the factory, especially in edge cases.
 */
public class LogFactoryTest {

    /**
     * Verifies that calling getLog(Class) with a null argument
     * correctly throws a NullPointerException.
     *
     * A logger instance is intrinsically tied to a class, so a null class
     * is an invalid input that should be rejected.
     */
    @Test(expected = NullPointerException.class)
    public void getLogWithNullClassShouldThrowNullPointerException() {
        LogFactory.getLog((Class<?>) null);
    }
}