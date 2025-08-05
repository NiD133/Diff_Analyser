/*
 * Copyright (c) 1998-2022 iText Group NV
 *
 * This test class is for the iText (R) project.
 * It is designed to verify the functionality of the MultiFilteredRenderListener class.
 */
package com.itextpdf.text.pdf.parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link MultiFilteredRenderListener}.
 *
 * These tests verify the core behaviors of the listener, including:
 * 1. Correct delegation of events to attached listeners.
 * 2. Proper application of render filters to control delegation.
 * 3. Graceful handling of edge cases, such as having no listeners or invalid arguments.
 */
@RunWith(MockitoJUnitRunner.class)
public class MultiFilteredRenderListenerTest {

    // The class under test
    private MultiFilteredRenderListener multiListener;

    // Mocks for dependencies
    @Mock
    private RenderListener delegateListener1;
    @Mock
    private RenderListener delegateListener2;

    @Mock
    private RenderFilter passingFilter;
    @Mock
    private RenderFilter failingFilter;

    @Mock
    private TextRenderInfo textRenderInfo;
    @Mock
    private ImageRenderInfo imageRenderInfo;

    @Before
    public void setUp() {
        // Initialize a new instance for each test to ensure isolation
        multiListener = new MultiFilteredRenderListener();

        // Configure default behavior for mock filters
        when(passingFilter.allowText(any(TextRenderInfo.class))).thenReturn(true);
        when(passingFilter.allowImage(any(ImageRenderInfo.class))).thenReturn(true);

        when(failingFilter.allowText(any(TextRenderInfo.class))).thenReturn(false);
        when(failingFilter.allowImage(any(ImageRenderInfo.class))).thenReturn(false);
    }

    // --- Tests for behavior with no listeners attached ---

    @Test
    public void eventMethods_whenNoListenersAttached_doNotThrowException() {
        // Act & Assert: Calling event methods on an empty listener should be a no-op
        // and not throw any exceptions.
        multiListener.beginTextBlock();
        multiListener.renderText(textRenderInfo);
        multiListener.renderImage(imageRenderInfo);
        multiListener.endTextBlock();
    }

    // --- Tests for delegation of non-filtered events ---

    @Test
    public void beginTextBlock_withAttachedListeners_delegatesToAll() {
        // Arrange
        multiListener.attachRenderListener(delegateListener1);
        multiListener.attachRenderListener(delegateListener2);

        // Act
        multiListener.beginTextBlock();

        // Assert
        verify(delegateListener1).beginTextBlock();
        verify(delegateListener2).beginTextBlock();
    }

    @Test
    public void endTextBlock_withAttachedListeners_delegatesToAll() {
        // Arrange
        multiListener.attachRenderListener(delegateListener1);
        multiListener.attachRenderListener(delegateListener2);

        // Act
        multiListener.endTextBlock();

        // Assert
        verify(delegateListener1).endTextBlock();
        verify(delegateListener2).endTextBlock();
    }

    // --- Tests for filtered text rendering ---

    @Test
    public void renderText_whenAllFiltersPass_delegatesCall() {
        // Arrange
        multiListener.attachRenderListener(delegateListener1, passingFilter, passingFilter);

        // Act
        multiListener.renderText(textRenderInfo);

        // Assert
        verify(delegateListener1).renderText(textRenderInfo);
    }

    @Test
    public void renderText_whenOneFilterFails_doesNotDelegateCall() {
        // Arrange
        multiListener.attachRenderListener(delegateListener1, passingFilter, failingFilter);

        // Act
        multiListener.renderText(textRenderInfo);

        // Assert
        verify(delegateListener1, never()).renderText(textRenderInfo);
    }

    @Test
    public void renderText_withNoFilters_delegatesCall() {
        // Arrange
        multiListener.attachRenderListener(delegateListener1); // No filters means it should always pass

        // Act
        multiListener.renderText(textRenderInfo);

        // Assert
        verify(delegateListener1).renderText(textRenderInfo);
    }

    // --- Tests for filtered image rendering ---

    @Test
    public void renderImage_whenAllFiltersPass_delegatesCall() {
        // Arrange
        multiListener.attachRenderListener(delegateListener1, passingFilter, passingFilter);

        // Act
        multiListener.renderImage(imageRenderInfo);

        // Assert
        verify(delegateListener1).renderImage(imageRenderInfo);
    }

    @Test
    public void renderImage_whenOneFilterFails_doesNotDelegateCall() {
        // Arrange
        multiListener.attachRenderListener(delegateListener1, passingFilter, failingFilter);

        // Act
        multiListener.renderImage(imageRenderInfo);

        // Assert
        verify(delegateListener1, never()).renderImage(imageRenderInfo);
    }

    // --- Tests for exceptional cases and invalid arguments ---

    @Test(expected = NullPointerException.class)
    public void renderText_whenFilterIsNull_throwsNullPointerException() {
        // Arrange
        multiListener.attachRenderListener(delegateListener1, passingFilter, null);

        // Act: This should throw an NPE when it tries to call allowText() on the null filter.
        multiListener.renderText(textRenderInfo);
    }

    @Test(expected = NullPointerException.class)
    public void renderImage_whenFilterIsNull_throwsNullPointerException() {
        // Arrange
        multiListener.attachRenderListener(delegateListener1, null);

        // Act: This should throw an NPE when it tries to call allowImage() on the null filter.
        multiListener.renderImage(imageRenderInfo);
    }

    @Test(expected = NullPointerException.class)
    public void beginTextBlock_whenListenerIsNull_throwsNullPointerException() {
        // Arrange
        multiListener.attachRenderListener(null);

        // Act: This should throw an NPE when it tries to delegate the call to a null listener.
        multiListener.beginTextBlock();
    }
}