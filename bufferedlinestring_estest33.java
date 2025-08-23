package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link BufferedLineString} class.
 * This specific test file focuses on the equals() method behavior.
 */
// Note: The class name and inheritance are preserved from the original EvoSuite-generated code.
public class BufferedLineString_ESTestTest33 extends BufferedLineString_ESTest_scaffolding {

    /**
     * Verifies that the equals() method returns false when a BufferedLineString
     * is compared to a null object, which is the expected contract for any equals() method.
     */
    @Test(timeout = 4000)
    public void equals_whenComparedWithNull_shouldReturnFalse() {
        // ARRANGE
        // A negative buffer is used to match the original test's input.
        // The source code's Javadoc suggests buf >= 0, but the implementation allows negative values.
        final double buffer = -1.2265789709679984;
        final SpatialContext spatialContext = SpatialContext.GEO;
        final List<Point> emptyPoints = Collections.emptyList();

        // Create an empty BufferedLineString.
        BufferedLineString lineString = new BufferedLineString(emptyPoints, buffer, spatialContext);

        // ACT
        // Perform the comparison against null.
        boolean isEqual = lineString.equals(null);

        // ASSERT
        // An object instance should never be equal to null.
        assertFalse("equals(null) must always return false.", isEqual);

        // Also, verify the object was constructed with the correct state.
        assertEquals("The buffer value should be correctly stored.", buffer, lineString.getBuf(), 0.0);
    }
}