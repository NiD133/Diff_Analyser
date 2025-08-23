package com.itextpdf.text.pdf.parser;

import org.junit.Test;

/**
 * Unit tests for the {@link MultiFilteredRenderListener} class.
 */
public class MultiFilteredRenderListenerTest {

    /**
     * Verifies that calling {@link MultiFilteredRenderListener#renderText(TextRenderInfo)}
     * with a null argument throws a {@link NullPointerException}.
     * <p>
     * This scenario tests that the call is correctly forwarded to a delegate listener,
     * which in turn is expected to throw the exception when given null input.
     */
    @Test(expected = NullPointerException.class)
    public void renderText_withNullRenderInfo_throwsNullPointerException() {
        // Arrange: Create a multi-listener and attach a delegate listener.
        // An empty filter array ensures the delegate is always invoked.
        MultiFilteredRenderListener multiListener = new MultiFilteredRenderListener();
        RenderListener delegateListener = new LocationTextExtractionStrategy();
        RenderFilter[] noFilters = new RenderFilter[0];
        multiListener.attachRenderListener(delegateListener, noFilters);

        // Act: Call the method under test with a null argument.
        // This is expected to cause the delegate to throw a NullPointerException.
        multiListener.renderText(null);

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}