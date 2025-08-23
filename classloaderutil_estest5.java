package org.apache.commons.jxpath.util;

import org.junit.Test;

/**
 * Contains tests for the {@link ClassLoaderUtil} class.
 */
public class ClassLoaderUtilTest {

    /**
     * Verifies that attempting to load a class with a malformed array name
     * (e.g., one starting with a digit) correctly throws a ClassNotFoundException.
     *
     * @throws ClassNotFoundException expected exception for this test case
     */
    @Test(expected = ClassNotFoundException.class)
    public void getClassWithMalformedArrayNameThrowsClassNotFoundException() throws ClassNotFoundException {
        // A valid Java class name cannot begin with a digit.
        // This test ensures that such malformed names are properly handled.
        String malformedArrayClassName = "9[]";
        boolean initialize = false;

        // This call is expected to fail with a ClassNotFoundException.
        ClassLoaderUtil.getClass(malformedArrayClassName, initialize);
    }
}