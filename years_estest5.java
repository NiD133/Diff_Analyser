package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void plus_givenNullYears_shouldReturnSameInstance() {
        // Arrange
        Years threeYears = Years.THREE;

        // Act
        // According to the Javadoc, adding a null period is a no-op.
        Years result = threeYears.plus((Years) null);

        // Assert
        // The method should return the exact same instance, not just an equal one.
        assertSame("Adding null should return the same instance", threeYears, result);
    }
}