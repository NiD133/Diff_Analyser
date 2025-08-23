package org.apache.commons.jxpath;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BasicNodeSet}.
 */
public class BasicNodeSetTest {

    /**
     * Tests that the toString() method on a newly created, empty BasicNodeSet
     * returns an empty list representation "[]".
     */
    @Test
    public void toString_onEmptySet_returnsEmptyBrackets() {
        // Arrange: Create an empty node set
        BasicNodeSet emptyNodeSet = new BasicNodeSet();
        String expectedRepresentation = "[]";

        // Act: Get the string representation
        String actualRepresentation = emptyNodeSet.toString();

        // Assert: Verify the representation is correct
        assertEquals(expectedRepresentation, actualRepresentation);
    }
}