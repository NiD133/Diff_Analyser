package com.itextpdf.text.pdf.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Vector class.
 * This test suite verifies the correctness of vector operations.
 * Author: Kevin Day
 */
public class VectorTest {

    // Setup method executed before each test
    @Before
    public void setUp() throws Exception {
        // Initialize resources if needed
    }

    // Teardown method executed after each test
    @After
    public void tearDown() throws Exception {
        // Clean up resources if needed
    }

    /**
     * Test the cross product of a vector with a matrix.
     * This test checks if the cross product operation is correctly implemented.
     */
    @Test
    public void testCrossProductWithMatrix() {
        // Arrange: Initialize a vector and a matrix
        Vector vector = new Vector(2, 3, 4);
        Matrix matrix = new Matrix(5, 6, 7, 8, 9, 10);
        
        // Expected result after the cross product operation
        Vector expectedVector = new Vector(67, 76, 4);
        
        // Act: Perform the cross product operation
        Vector resultVector = vector.cross(matrix);
        
        // Assert: Verify the result is as expected
        assertEquals("The cross product of the vector and matrix is incorrect.", expectedVector, resultVector);
    }
}