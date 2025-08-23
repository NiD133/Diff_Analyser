package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Contains tests for the {@link JsonArray#hashCode()} method.
 */
public class JsonArrayHashCodeTest {

    @Test
    public void hashCode_forEmptyArray_isConsistent() {
        // Arrange
        JsonArray emptyArray = new JsonArray();

        // Act
        int hashCode = emptyArray.hashCode();

        // Assert
        // The hashCode for an empty JsonArray should be 1,
        // which is consistent with the contract of java.util.List.hashCode().
        assertEquals(1, hashCode);
    }

    @Test
    public void hashCode_forEqualArrays_isSame() {
        // Arrange: Create two separate but identical JsonArray instances.
        JsonArray array1 = new JsonArray();
        array1.add("hello");
        array1.add(123);
        array1.add(true);

        JsonArray array2 = new JsonArray();
        array2.add("hello");
        array2.add(123);
        array2.add(true);

        // Assert
        // Per the Object.hashCode() contract, if two objects are equal,
        // their hash codes must also be equal.
        assertEquals(array1, array2); // First, confirm they are equal.
        assertEquals(array1.hashCode(), array2.hashCode());
    }

    @Test
    public void hashCode_forDifferentArrays_isDifferent() {
        // Arrange
        JsonArray array1 = new JsonArray();
        array1.add("a");

        JsonArray array2 = new JsonArray();
        array2.add("b");

        // Assert
        // While not strictly required by the hashCode contract, two unequal objects
        // should ideally produce different hash codes.
        assertNotEquals(array1, array2);
        assertNotEquals(array1.hashCode(), array2.hashCode());
    }
}