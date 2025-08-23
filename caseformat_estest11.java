package com.google.common.base;

import org.junit.Test;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    @Test(expected = NullPointerException.class)
    public void converterTo_shouldThrowNullPointerException_whenTargetFormatIsNull() {
        // The converterTo method relies on Preconditions.checkNotNull for its target format.
        // Therefore, passing null should result in a NullPointerException.
        CaseFormat.LOWER_CAMEL.converterTo(null);
    }
}