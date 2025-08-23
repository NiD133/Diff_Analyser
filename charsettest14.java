package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests the serialization and deserialization of {@link CharSet} instances.
 */
class CharSetSerializationTest extends AbstractLangTest {

    @DisplayName("A CharSet instance should be equal to its clone after serialization and deserialization.")
    @ParameterizedTest(name = "Test with definition: \"{0}\"")
    @ValueSource(strings = {
        "a",        // Case 1: A single character
        "a-e",      // Case 2: A simple character range
        "be-f^a-z"  // Case 3: A complex definition with mixed rules
    })
    void clonedCharSetShouldBeEqualToOriginal(final String setDefinition) {
        // Arrange: Create an original CharSet instance from a given definition.
        final CharSet originalSet = CharSet.getInstance(setDefinition);

        // Act: Clone the instance via serialization.
        final CharSet clonedSet = SerializationUtils.clone(originalSet);

        // Assert: The clone should be a different instance but logically equal.
        assertNotSame(originalSet, clonedSet, "The cloned object should be a new instance in memory.");
        assertEquals(originalSet, clonedSet, "The cloned object's state should be identical to the original.");
    }
}