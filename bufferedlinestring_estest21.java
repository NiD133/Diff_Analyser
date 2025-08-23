package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;

/**
 * This test class contains tests for the BufferedLineString class.
 * This particular test case focuses on the behavior of the getBuffered method.
 */
public class BufferedLineString_ESTestTest21 extends BufferedLineString_ESTest_scaffolding {

    /**
     * Verifies that the getBuffered method throws a NullPointerException
     * when it is called with a null SpatialContext.
     */
    @Test(expected = NullPointerException.class)
    public void getBufferedShouldThrowNullPointerExceptionWhenContextIsNull() {
        // Arrange: Create a valid, albeit empty, BufferedLineString.
        // The state of the line string itself is not the focus of this test.
        SpatialContext geoContext = SpatialContext.GEO;
        BufferedLineString emptyLineString = new BufferedLineString(Collections.<Point>emptyList(), 1.0, geoContext);

        // Act: Call the method under test with a null context.
        // The buffer distance (e.g., 5.0) is arbitrary as the call should fail before it's used.
        emptyLineString.getBuffered(5.0, null);

        // Assert: The test succeeds if a NullPointerException is thrown,
        // as specified by the `expected` attribute in the @Test annotation.
    }
}