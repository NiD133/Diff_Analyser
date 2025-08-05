package com.itextpdf.text.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for GetBufferedRandomAccessSource class.
 */
public class GetBufferedRandomAccessSourceTest {

    @Before
    public void setUp() {
        // Setup resources if needed before each test
    }

    @After
    public void tearDown() {
        // Clean up resources if needed after each test
    }

    @Test
    public void testSingleByteAccess() throws Exception {
        // Test case: Ensure GetBufferedRandomAccessSource can handle a single byte source without errors.
        
        // Arrange: Create a data source with a single byte.
        byte[] singleByteData = new byte[]{42};
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(singleByteData);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);

        // Act & Assert: Verify that the buffered source returns the correct byte value.
        assertEquals("The byte value at position 0 should be 42", 42, bufferedSource.get(0));
    }
}