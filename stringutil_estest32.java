package org.jsoup.internal;

import org.junit.Test;
import java.util.Collection;

/**
 * Test suite focusing on exception handling for the {@link StringUtil#join(Collection, String)} method.
 */
public class StringUtilTest {

    /**
     * Verifies that calling {@code StringUtil.join()} with a null collection
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void joinWithNullCollectionThrowsNullPointerException() {
        // The method under test should reject a null collection argument.
        // The @Test(expected) annotation asserts that the expected exception is thrown.
        StringUtil.join((Collection<?>) null, ", ");
    }
}