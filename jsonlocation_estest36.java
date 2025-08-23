package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonLocation} class, focusing on its equality contract.
 */
public class JsonLocationTest {

    /**
     * Tests that two {@link JsonLocation} instances are considered equal if they are
     * created with the same source reference, offsets, and line/column numbers.
     * It also verifies the general contract for {@code equals()} by checking against
     * a non-equal object.
     */
    @Test
    public void shouldBeEqualWhenConstructedWithSameValues() {
        // Arrange
        // The original test used JsonLocation.NA as the source object, which is an interesting edge case.
        final Object sourceReference = JsonLocation.NA;
        final long byteOffset = 500L;
        final long charOffset = 500L;
        final int lineNumber = 500;
        final int columnNumber = 500;

        // Create two distinct but identical JsonLocation instances.
        // Note: This constructor is deprecated in favor of one using ContentReference.
        JsonLocation locationA = new JsonLocation(sourceReference, byteOffset, charOffset, lineNumber, columnNumber);
        JsonLocation locationB = new JsonLocation(sourceReference, byteOffset, charOffset, lineNumber, columnNumber);

        // Act & Assert

        // 1. Verify that the two identical instances are equal.
        assertEquals("Instances with identical properties should be equal.", locationA, locationB);

        // 2. Verify the hashCode contract: equal objects must have equal hash codes.
        assertEquals("Hash codes of equal instances should be identical.", locationA.hashCode(), locationB.hashCode());

        // 3. Verify that the instance is not equal to a different object (JsonLocation.NA).
        assertNotEquals("Instance should not be equal to JsonLocation.NA.", locationA, JsonLocation.NA);

        // 4. Sanity-check the properties of the created location object to ensure the constructor worked.
        assertEquals("Line number should match constructor argument.", lineNumber, locationA.getLineNr());
        assertEquals("Column number should match constructor argument.", columnNumber, locationA.getColumnNr());
        assertEquals("Character offset should match constructor argument.", charOffset, locationA.getCharOffset());
        assertEquals("Byte offset should match constructor argument.", byteOffset, locationA.getByteOffset());
    }
}