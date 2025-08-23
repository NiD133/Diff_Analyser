package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link MultiFilteredRenderListener}.
 */
public class MultiFilteredRenderListenerTest {

    /**
     * Verifies that calling renderImage with a null ImageRenderInfo object
     * throws a NullPointerException.
     *
     * A delegate listener must be attached for the method's logic to be executed.
     */
    @Test(expected = NullPointerException.class)
    public void renderImage_withNullImageRenderInfo_throwsNullPointerException() {
        // Arrange: Create a MultiFilteredRenderListener and attach a mock delegate listener.
        // This setup is necessary to ensure the internal loop in renderImage is entered.
        MultiFilteredRenderListener multiListener = new MultiFilteredRenderListener();
        RenderListener mockDelegateListener = mock(RenderListener.class);
        RenderFilter[] anyFilters = new RenderFilter[1]; // The content of the filters is irrelevant for this test.
        multiListener.attachRenderListener(mockDelegateListener, anyFilters);

        // Act: Call renderImage with a null argument.
        multiListener.renderImage(null);

        // Assert: A NullPointerException is expected, as declared in the @Test annotation.
    }
}