package com.itextpdf.text.pdf.parser;

import org.junit.Test;

/**
 * Unit tests for the {@link MultiFilteredRenderListener} class.
 */
public class MultiFilteredRenderListenerTest {

    /**
     * Verifies that calling beginTextBlock() on a listener with no attached delegates
     * executes without throwing an exception. This confirms the method handles the
     * "no-op" (no operation) case gracefully.
     */
    @Test
    public void beginTextBlock_withNoDelegates_shouldNotThrowException() {
        // Arrange: Create a new MultiFilteredRenderListener with no delegates.
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();

        // Act: Call the method under test.
        listener.beginTextBlock();

        // Assert: The test passes if no exception was thrown during the 'Act' phase.
        // This is an implicit assertion for this type of test.
    }
}