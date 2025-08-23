package org.apache.commons.io.file.attribute;

import java.math.BigDecimal;
import org.junit.Test;

/**
 * Tests for the {@link FileTimes} class, focusing on its handling of null arguments.
 */
public class FileTimesTest {

    /**
     * Tests that ntfsTimeToDate(BigDecimal) throws a NullPointerException
     * when the input BigDecimal is null.
     */
    @Test(expected = NullPointerException.class)
    public void testNtfsTimeToDateWithNullBigDecimalThrowsNullPointerException() {
        // This test calls the package-private version of ntfsTimeToDate.
        FileTimes.ntfsTimeToDate((BigDecimal) null);
    }
}