package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the {@link MultiFilteredRenderListener} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MultiFilteredRenderListenerTest {

    /**
     * Verifies that when beginTextBlock() is called, it delegates the call
     * to all attached render listeners.
     */
    @Test
    public void beginTextBlock_delegatesCallToAttachedListener() {
        // Arrange
        // 1. Create a mock RenderListener to act as a delegate.
        RenderListener delegateListener = mock(RenderListener.class);

        // 2. Create the MultiFilteredRenderListener instance to be tested.
        MultiFilteredRenderListener multiListener = new MultiFilteredRenderListener();
        
        // 3. Attach the delegate listener. The filters are not used by the beginTextBlock()
        //    method, so an empty array is sufficient for this test.
        multiListener.attachRenderListener(delegateListener, new RenderFilter[0]);

        // Act
        // Call the method under test.
        multiListener.beginTextBlock();

        // Assert
        // Verify that the beginTextBlock() method was called on the delegate listener exactly once.
        verify(delegateListener, times(1)).beginTextBlock();
    }
}