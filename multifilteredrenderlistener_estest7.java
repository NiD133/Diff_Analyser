package com.itextpdf.text.pdf.parser;

import org.junit.Test;

/**
 * Test suite for the {@link MultiFilteredRenderListener} class, focusing on its exception-handling behavior.
 */
public class MultiFilteredRenderListenerTest extends MultiFilteredRenderListener_ESTest_scaffolding {

    /**
     * Verifies that {@link MultiFilteredRenderListener#renderText(TextRenderInfo)} throws a
     * {@link NullPointerException} if it was configured with a filter array containing a null element.
     *
     * The listener's internal logic is expected to iterate over the filters and invoke methods on them.
     * If a filter is null, this interaction should fail fast with an NPE.
     */
    @Test(expected = NullPointerException.class)
    public void renderText_whenAttachedFilterIsNull_throwsNullPointerException() {
        // Arrange: Create a listener and attach a delegate with a filter array
        // that contains a null element.
        MultiFilteredRenderListener multiListener = new MultiFilteredRenderListener();
        RenderFilter[] filtersWithNull = { null };
        multiListener.attachRenderListener(null, filtersWithNull);

        // Act & Assert: When renderText is called, it should attempt to process the
        // filters. Accessing the null filter will cause a NullPointerException.
        // The TextRenderInfo argument can be null as the code fails before it's used.
        multiListener.renderText(null);
    }
}