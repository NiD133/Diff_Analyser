package org.apache.commons.lang3.concurrent;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link MultiBackgroundInitializer}.
 */
public class MultiBackgroundInitializerTest {

    /**
     * Tests that calling close() on an initializer that has not been started
     * does not change its "started" state.
     */
    @Test
    public void isStartedShouldReturnFalseWhenCloseIsCalledBeforeStart() {
        // Arrange: Create a new, unstarted MultiBackgroundInitializer.
        final MultiBackgroundInitializer initializer = new MultiBackgroundInitializer();

        // Act: Call the close() method. Since the initializer hasn't started and has
        // no child initializers, this should effectively be a no-op.
        initializer.close();

        // Assert: Verify that the initializer is still not considered "started".
        // The isStarted() flag should only become true after the start() method is invoked.
        assertFalse("The initializer should not be in a started state if close() is called before start()",
                initializer.isStarted());
    }
}