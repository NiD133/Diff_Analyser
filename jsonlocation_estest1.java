package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link JsonLocation} class, focusing on its equality logic.
 */
public class JsonLocationTest {

    /**
     * Verifies that the equals() method returns false when two JsonLocation objects
     * have the same coordinates but different source references. The equals() contract
     * requires checking all significant fields, including the source.
     */
    @Test
    public void equals_shouldReturnFalse_whenSourceReferencesAreDifferent() {
        // Arrange
        final long charOffset = 500L;
        final long byteOffset = 500L;
        final int line = 500;
        final int column = 500;

        // Create a location with a null source, which the constructor treats as "unknown".
        // Note: This uses a deprecated constructor, but its behavior is still well-defined.
        JsonLocation locationWithUnknownSource = new JsonLocation(null, charOffset, line, column);

        // Create a second location with the same coordinates but a different, explicit source.
        ContentReference redactedSource = ContentReference.redacted();
        JsonLocation locationWithRedactedSource = new JsonLocation(redactedSource, byteOffset, charOffset, line, column);

        // Sanity check to ensure the source references are indeed different.
        assertNotEquals(locationWithUnknownSource.contentReference(), locationWithRedactedSource.contentReference());

        // Act
        boolean areEqual = locationWithRedactedSource.equals(locationWithUnknownSource);

        // Assert
        assertFalse("Locations with different source references should not be equal", areEqual);
    }

    /**
     * Verifies that the equals() method is symmetric, meaning a.equals(b) is the same as b.equals(a).
     */
    @Test
    public void equals_shouldBeSymmetric() {
        // Arrange
        JsonLocation location1 = new JsonLocation(ContentReference.unknown(), 100L, 1, 10);
        JsonLocation location2 = new JsonLocation(ContentReference.redacted(), 100L, 1, 10);

        // Act & Assert
        assertFalse("a.equals(b) should be false", location1.equals(location2));
        assertFalse("b.equals(a) should also be false", location2.equals(location1));
    }
}