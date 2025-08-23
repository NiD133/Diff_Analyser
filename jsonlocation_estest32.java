package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Verifies that appendOffsetDescription() correctly formats the output
     * when the column number is negative, which signifies an unknown column.
     * In this case, it should display "UNKNOWN" for the column value.
     */
    @Test
    public void appendOffsetDescription_withNegativeColumn_shouldRenderColumnAsUnknown() {
        // Arrange
        final int lineNumber = 500;
        final int unknownColumn = -1291; // A negative value indicates an unknown column.
        final long charOffset = -3036L; // This value is not used by appendOffsetDescription.
        final String expectedDescription = "line: 500, column: UNKNOWN";

        // The source reference is required but its content is not relevant for this test.
        ContentReference contentReference = ContentReference.rawReference(false, null);
        JsonLocation location = new JsonLocation(contentReference, charOffset, lineNumber, unknownColumn);

        StringBuilder descriptionBuilder = new StringBuilder();

        // Act
        location.appendOffsetDescription(descriptionBuilder);

        // Assert
        assertEquals("The generated description should indicate an unknown column.",
                expectedDescription, descriptionBuilder.toString());

        // This constructor overload sets the byte offset to -1, which we can also verify.
        assertEquals("Byte offset should be -1 as per the constructor used.",
                -1L, location.getByteOffset());
    }
}