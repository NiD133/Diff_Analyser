package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link DefaultIndenter} class.
 */
public class DefaultIndenterTest {

    /**
     * Verifies that calling {@link DefaultIndenter#withIndent(String)} with the
     * same indentation string returns the identical object instance.
     * This confirms an internal optimization to avoid creating unnecessary objects.
     */
    @Test
    public void withIndent_whenIndentIsUnchanged_returnsSameInstance() {
        // Arrange: Create an indenter with a specific indent string.
        DefaultIndenter originalIndenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE.withIndent("");

        // Act: Call withIndent() again with the exact same indent string.
        DefaultIndenter resultIndenter = originalIndenter.withIndent("");

        // Assert: The method should return the original instance, not a new one.
        assertSame("Expected the same instance when withIndent() is called with an unchanged value",
                originalIndenter, resultIndenter);
        
        // Also verify that the end-of-line character was preserved correctly.
        assertEquals("End-of-line character should be the system default",
                DefaultIndenter.SYS_LF, resultIndenter.getEol());
    }
}