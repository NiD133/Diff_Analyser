package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * Unit tests for the {@link MultiFilteredRenderListener} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MultiFilteredRenderListenerTest {

    // Using @Mock to create a mock implementation of the RenderListener interface.
    // This mock will act as the delegate listener attached to our main listener.
    @Mock
    private RenderListener mockDelegateListener;

    /**
     * Verifies that a call to endTextBlock() on the MultiFilteredRenderListener
     * is correctly forwarded to an attached delegate listener.
     */
    @Test
    public void endTextBlock_forwardsCallToAttachedDelegateListener() {
        // Arrange: Set up the test objects and preconditions.
        // 1. Create an instance of the class under test.
        MultiFilteredRenderListener multiListener = new MultiFilteredRenderListener();

        // 2. Attach the mock delegate listener. For this test, the filters are not
        // relevant to the endTextBlock() method, so we can pass an empty array.
        multiListener.attachRenderListener(mockDelegateListener, new RenderFilter[0]);

        // Act: Execute the method being tested.
        multiListener.endTextBlock();

        // Assert: Verify that the expected interaction occurred.
        // We expect the endTextBlock() method to have been called exactly once
        // on our mock delegate listener.
        verify(mockDelegateListener, times(1)).endTextBlock();
    }
}