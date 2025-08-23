package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Verifies that adding a null Minutes object is a no-op and returns the original instance.
     * <p>
     * The Javadoc for {@link Minutes#plus(Minutes)} specifies that a null input is treated as zero.
     * This test confirms that for a zero-value addition, the implementation returns the same
     * instance, leveraging the class's immutability.
     */
    @Test
    public void plus_givenNullArgument_shouldReturnSameInstance() {
        // Arrange
        final Minutes zeroMinutes = Minutes.ZERO;

        // Act
        // The cast to (Minutes) is necessary to resolve the overloaded plus() method.
        final Minutes result = zeroMinutes.plus((Minutes) null);

        // Assert
        assertSame("Adding null should not create a new object but return the original instance.", zeroMinutes, result);
    }
}