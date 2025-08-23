package org.jsoup.internal;

import org.junit.Test;

/**
 * Tests for the {@link StringUtil#releaseBuilderVoid(StringBuilder)} method.
 */
public class StringUtil_ESTestTest24 extends StringUtil_ESTest_scaffolding {

    /**
     * Verifies that passing a null StringBuilder to releaseBuilderVoid
     * results in a NullPointerException, as expected.
     */
    @Test(expected = NullPointerException.class)
    public void releaseBuilderVoidShouldThrowNullPointerExceptionWhenBuilderIsNull() {
        StringUtil.releaseBuilderVoid(null);
    }
}