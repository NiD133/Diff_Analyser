package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link WKTWriter} class.
 */
public class WKTWriterTest {

    /**
     * Tests that the getFormatName() method correctly returns "WKT".
     */
    @Test
    public void getFormatName_shouldReturnWKT() {
        // Arrange: Create an instance of the WKTWriter.
        WKTWriter wktWriter = new WKTWriter();
        String expectedFormatName = "WKT";

        // Act: Call the method under test.
        String actualFormatName = wktWriter.getFormatName();

        // Assert: Verify that the returned format name is correct.
        assertEquals(expectedFormatName, actualFormatName);
    }
}