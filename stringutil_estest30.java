package org.jsoup.internal;

import org.junit.Test;

import java.util.Iterator;

/**
 * Test suite for the {@link StringUtil#join(Iterator, String)} method.
 */
public class StringUtilJoinIteratorTest {

    /**
     * Verifies that calling StringUtil.join() with a null iterator
     * throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void joinWithNullIteratorShouldThrowNullPointerException() {
        // The method under test should perform a null check on the iterator argument
        // and throw a NullPointerException if it is null.
        StringUtil.join((Iterator<?>) null, ", ");
    }
}