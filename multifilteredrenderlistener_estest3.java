package com.itextpdf.text.pdf.parser;

import com.itextpdf.awt.geom.Rectangle2D;
import org.junit.Test;

/**
 * This test suite focuses on the MultiFilteredRenderListener class.
 */
// The original test class name was unclear. Renaming it makes its purpose obvious.
public class MultiFilteredRenderListenerTest extends MultiFilteredRenderListener_ESTest_scaffolding {

    /**
     * Verifies that the renderImage method is robust and does not throw an exception
     * when called with a null ImageRenderInfo object. This is a common defensive check.
     */
    @Test(timeout = 4000)
    public void renderImage_withNullImageRenderInfo_shouldNotThrowException() {
        // Arrange: Set up the MultiFilteredRenderListener with a delegate listener and a filter.
        // The specific types of listener and filter are not important for this test,
        // as we are only checking for null-safety.
        MultiFilteredRenderListener multiListener = new MultiFilteredRenderListener();
        RenderListener delegateListener = new LocationTextExtractionStrategy();
        RenderFilter regionFilter = new RegionTextRenderFilter(new Rectangle2D.Double(0, 0, 100, 100));

        multiListener.attachRenderListener(delegateListener, regionFilter);

        // Act: Call the method under test with a null argument.
        multiListener.renderImage(null);

        // Assert: No explicit assertions are needed. The test succeeds if the "Act" phase
        // completes without throwing a NullPointerException or any other exception.
    }
}