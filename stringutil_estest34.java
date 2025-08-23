package org.jsoup.internal;

import org.junit.Test;

/**
 * Tests for {@link StringUtil}.
 */
public class StringUtilTest {

    /**
     * Verifies that inSorted throws a NullPointerException when the input array is null,
     * which is the expected behavior from the underlying Arrays.binarySearch.
     */
    @Test(expected = NullPointerException.class)
    public void inSortedWithNullArrayThrowsNullPointerException() {
        // The first argument's value is irrelevant; the test focuses on the null array.
        StringUtil.inSorted("any-string", (String[]) null);
    }
}