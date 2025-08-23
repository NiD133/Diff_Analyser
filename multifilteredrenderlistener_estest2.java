package com.itextpdf.text.pdf.parser;

import org.junit.Test;

/**
 * Tests for {@link MultiFilteredRenderListener}.
 */
public class MultiFilteredRenderListener_ESTestTest2 extends MultiFilteredRenderListener_ESTest_scaffolding {

    /**
     * Verifies that calling beginTextBlock() throws a NullPointerException
     * if a null RenderListener was previously attached. The MultiFilteredRenderListener
     * should not permit null delegates, as it attempts to forward calls to them.
     */
    @Test(expected = NullPointerException.class)
    public void beginTextBlock_throwsNullPointerException_whenAttachedListenerIsNull() {
        // Arrange: Create a listener and attach a null delegate to it.
        MultiFilteredRenderListener multiListener = new MultiFilteredRenderListener();
        RenderListener nullListener = null;
        RenderFilter[] noFilters = new RenderFilter[0];

        multiListener.attachRenderListener(nullListener, noFilters);

        // Act: Call the method under test. This is expected to throw a NullPointerException
        // because it will try to call beginTextBlock() on the null delegate.
        multiListener.beginTextBlock();

        // Assert: The exception is verified by the @Test(expected) annotation.
    }
}