package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link DefaultIndenter} class, focusing on its default state.
 */
public class DefaultIndenterTest {

    /**
     * Verifies that the default constructor of {@link DefaultIndenter} creates an instance
     * configured for block-style (non-inline) indentation and uses the system's
     * default line separator.
     */
    @Test
    public void defaultConstructor_shouldCreateNonInlineIndenter_withSystemLineFeed() {
        // Arrange: No special arrangement is needed as we are testing the default constructor.

        // Act
        DefaultIndenter indenter = new DefaultIndenter();

        // Assert
        // A default indenter is used for pretty-printing, which is not an inline format.
        assertFalse("Default indenter should not be considered inline", indenter.isInline());

        // The default end-of-line character should match the system's line separator constant.
        assertEquals("Default end-of-line should be the system line feed",
                DefaultIndenter.SYS_LF, indenter.getEol());
    }
}