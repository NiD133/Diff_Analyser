package com.google.gson;

import com.google.common.testing.EqualsTester;
import org.junit.Test;

/**
 * Test class for {@link JsonArray}'s equals() and hashCode() methods.
 */
// The original test class name `JsonArrayTestTest1` was redundant.
// A more standard name is `JsonArrayTest`.
public class JsonArrayTest {

    @Test
    public void testEqualsAndHashCode() {
        // Guava's EqualsTester provides a robust way to verify the contracts of
        // equals() and hashCode(). It checks for reflexivity, symmetry,
        // transitivity, and consistency between equals() and hashCode().
        // This is more comprehensive and declarative than testing a single case.
        new EqualsTester()
            // Group 1: Two empty arrays should be equal.
            .addEqualityGroup(new JsonArray(), new JsonArray())

            // Group 2: Arrays with the same elements in the same order should be equal.
            .addEqualityGroup(
                createArray(new JsonPrimitive(1), new JsonPrimitive("a")),
                createArray(new JsonPrimitive(1), new JsonPrimitive("a")))

            // Group 3: An array with different elements should not be equal to the previous groups.
            .addEqualityGroup(createArray(new JsonPrimitive(1), new JsonPrimitive("b")))

            // Group 4: An array with the same elements but in a different order should not be equal.
            .addEqualityGroup(createArray(new JsonPrimitive("a"), new JsonPrimitive(1)))

            // Group 5: An array with a different number of elements should not be equal.
            .addEqualityGroup(createArray(new JsonPrimitive(1)))

            // Group 6: A JsonArray should not be equal to other object types, like JsonObject.
            .addEqualityGroup(new JsonObject())
            .testEquals();
    }

    /**
     * Helper method to create a JsonArray from a varargs of JsonElements.
     * This improves the readability of the test setup.
     */
    private static JsonArray createArray(JsonElement... elements) {
        JsonArray array = new JsonArray();
        for (JsonElement element : elements) {
            array.add(element);
        }
        return array;
    }
}