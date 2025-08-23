package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * Verifies that the get() method correctly retrieves the X-component of the vector
     * when accessed with the index constant I1.
     */
    @Test
    public void get_withIndexI1_returnsXComponent() {
        // Arrange: Create a vector with distinct, non-zero coordinates to ensure
        // the test doesn't pass by coincidence (e.g., if all components were 0).
        final float xComponent = 10.5f;
        final float yComponent = -20.0f;
        final float zComponent = 30.2f;
        Vector vector = new Vector(xComponent, yComponent, zComponent);

        // Act: Retrieve the component at the index for the X-coordinate.
        float retrievedComponent = vector.get(Vector.I1);

        // Assert: The retrieved value must match the original X-component.
        // A delta of 0.0f is used because we expect an exact value from a simple getter.
        assertEquals(xComponent, retrievedComponent, 0.0f);
    }
}