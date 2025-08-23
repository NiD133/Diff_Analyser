package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the StringUtil utility class.
 */
public class StringUtilTest {

    /**
     * Verifies that the StringUtil class can be instantiated.
     * <p>
     * StringUtil is a utility class containing only static methods and is not intended
     * for instantiation. However, it has a default public constructor. This test calls
     * that constructor to ensure it does not throw an exception and to achieve full
     * code coverage for the class.
     */
    @Test
    public void canBeInstantiatedForCoverage() {
        // WHEN a new instance of StringUtil is created
        StringUtil stringUtil = new StringUtil();

        // THEN the instance should not be null
        assertNotNull("The StringUtil instance should be successfully created.", stringUtil);
    }
}