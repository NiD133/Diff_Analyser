package com.itextpdf.text.pdf.parser;

import org.junit.Test;

/**
 * This test class is focused on the MultiFilteredRenderListener.
 * The original test was automatically generated and has been refactored for clarity.
 */
public class MultiFilteredRenderListener_ESTestTest5 extends MultiFilteredRenderListener_ESTest_scaffolding {

    /**
     * Verifies that endTextBlock() throws a NullPointerException if it attempts to
     * delegate the call to a listener that is null.
     */
    @Test(expected = NullPointerException.class)
    public void endTextBlock_whenAttachedListenerIsNull_throwsNullPointerException() {
        // Arrange: Create a listener and attach a null delegate.
        MultiFilteredRenderListener multiListener = new MultiFilteredRenderListener();
        RenderFilter[] noFilters = null; // Using a null filter set
        multiListener.attachRenderListener(null, noFilters);

        // Act: Call the method under test. This should trigger the exception
        // because it will try to call endTextBlock() on the null delegate.
        multiListener.endTextBlock();

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}