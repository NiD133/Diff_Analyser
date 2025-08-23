package com.itextpdf.text.pdf.parser;

import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

/**
 * Tests for MultiFilteredRenderListener focusing on:
 * - No-op behavior when no delegates are attached
 * - Exception behavior when a null delegate is attached
 * - Propagation of calls to a real delegate
 * - Behavior when the filters array contains null entries
 */
public class MultiFilteredRenderListenerTest {

    // No delegates attached: all callbacks should be safe no-ops

    @Test
    public void beginTextBlock_withoutDelegates_doesNotFail() {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        listener.beginTextBlock();
    }

    @Test
    public void endTextBlock_withoutDelegates_doesNotFail() {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        listener.endTextBlock();
    }

    @Test
    public void renderText_withoutDelegates_acceptsNull() {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        listener.renderText(null);
    }

    // Attaching a null delegate: invoking callbacks should throw NPE from the listener

    @Test
    public void beginTextBlock_withNullDelegate_throwsNPE() {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        listener.attachRenderListener(null, new RenderFilter[0]);

        assertThrows(NullPointerException.class, listener::beginTextBlock);
    }

    @Test
    public void endTextBlock_withNullDelegate_throwsNPE() {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        listener.attachRenderListener(null, (RenderFilter[]) null);

        assertThrows(NullPointerException.class, listener::endTextBlock);
    }

    @Test
    public void renderText_withNullDelegate_throwsNPE() {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        listener.attachRenderListener(null, new RenderFilter[1]);

        assertThrows(NullPointerException.class, () -> listener.renderText(null));
    }

    // Attaching a valid delegate: calls should be forwarded; null renderInfo should NPE from the delegate

    @Test
    public void beginTextBlock_withValidDelegate_doesNotFail() {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy delegate = new LocationTextExtractionStrategy();

        // Filters array contents don't matter for begin/end callbacks
        listener.attachRenderListener(delegate, new RenderFilter[3]);
        listener.beginTextBlock();
    }

    @Test
    public void endTextBlock_withValidDelegate_doesNotFail() {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy delegate = new LocationTextExtractionStrategy();

        listener.attachRenderListener(delegate);
        listener.endTextBlock();
    }

    @Test
    public void renderText_null_propagatesToDelegate() {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy delegate = new LocationTextExtractionStrategy();

        listener.attachRenderListener(delegate);
        // Expect the NPE to originate from the delegate when given a null TextRenderInfo
        assertThrows(NullPointerException.class, () -> listener.renderText(null));
    }

    // Filters array handling: null entries in the filters array should lead to NPE when evaluating

    @Test
    public void renderImage_withNullFiltersArrayEntries_throwsNPE() {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy delegate = new LocationTextExtractionStrategy();

        // Provide an array of null filters; evaluating them should trigger an NPE inside the listener
        listener.attachRenderListener(delegate, new RenderFilter[3]);

        assertThrows(NullPointerException.class, () -> listener.renderImage(null));
    }

    // API sanity: attachRenderListener returns the same delegate instance

    @Test
    public void attachRenderListener_returnsSameDelegateInstance() {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy delegate = new LocationTextExtractionStrategy();

        LocationTextExtractionStrategy returned = listener.attachRenderListener(delegate);
        assertSame(delegate, returned);
    }
}