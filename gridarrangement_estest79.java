package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit tests for the {@link GridArrangement} class, focusing on object equality.
 */
public class GridArrangementEqualityTest {

    /**
     * Verifies that two GridArrangement instances created with the same number of rows
     * and columns are considered equal in value.
     */
    @Test
    public void equals_shouldReturnTrue_whenObjectsHaveSameDimensions() {
        // Arrange: Create two separate GridArrangement objects with identical dimensions.
        GridArrangement arrangement1 = new GridArrangement(0, 0);
        GridArrangement arrangement2 = new GridArrangement(0, 0);

        // Assert: The objects should be equal in value but must be different instances.
        assertNotSame("The two instances should be different objects in memory.", arrangement1, arrangement2);
        assertEquals("Instances with the same dimensions should be considered equal.", arrangement1, arrangement2);
        
        // As per the equals() contract, equal objects must also have equal hash codes.
        assertEquals("Equal objects must have the same hash code.", arrangement1.hashCode(), arrangement2.hashCode());
    }
}