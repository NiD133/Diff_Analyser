package org.apache.commons.io.file.attribute;

import org.junit.Test;

import java.util.Date;

/**
 * Tests for the {@link FileTimes} utility class, focusing on exception handling.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#toNtfsTime(Date)} throws a NullPointerException
     * when given a null input.
     */
    @Test(expected = NullPointerException.class)
    public void toNtfsTimeWithNullDateShouldThrowNullPointerException() {
        FileTimes.toNtfsTime((Date) null);
    }
}