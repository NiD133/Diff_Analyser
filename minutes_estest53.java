package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that adding a null Minutes object is treated as adding zero,
     * which should return the original, unchanged instance.
     */
    @Test
    public void plus_withNullArgument_returnsSameInstance() {
        // Arrange
        final Minutes oneMinute = Minutes.ONE;

        // Act
        // The plus() method's contract states that a null argument is treated as zero.
        final Minutes result = oneMinute.plus((Minutes) null);

        // Assert
        // For an immutable object, adding zero should return the same instance, not just an equal one.
        assertSame("Adding null should not create a new object, but return the same instance", oneMinute, result);
    }
}