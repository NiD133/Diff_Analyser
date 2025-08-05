package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A clear and maintainable suite of unit tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    @Test
    public void constructor_withNegativeCapacity_shouldThrowIllegalArgumentException() {
        try {
            // Act
            new JsonArray(-1);
            fail("Expected IllegalArgumentException for negative capacity");
        } catch (IllegalArgumentException expected) {
            // Assert: Exception was thrown as expected
        }
    }

    @Test
    public void add_shouldIncreaseSizeAndStoreElements() {
        // Arrange
        JsonArray array = new JsonArray();
        assertTrue("New array should be empty", array.isEmpty());
        assertEquals("New array should have size 0", 0, array.size());

        // Act & Assert for first element
        array.add(new JsonPrimitive("a"));
        assertFalse("Array should not be empty after add", array.isEmpty());
        assertEquals("Array size should be 1", 1, array.size());
        assertEquals(new JsonPrimitive("a"), array.get(0));

        // Act & Assert for second element
        array.add(new JsonPrimitive(123));
        assertEquals("Array size should be 2", 2, array.size());
        assertEquals(new JsonPrimitive(123), array.get(1));
    }

    @Test
    public void addAll_shouldAddAllElementsFromAnotherArray() {
        // Arrange
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive(1));
        JsonArray other = new JsonArray();
        other.add(new JsonPrimitive(2));

        // Act
        array.addAll(other);

        // Assert
        assertEquals(2, array.size());
        assertEquals(new JsonPrimitive(1), array.get(0));
        assertEquals(new JsonPrimitive(2), array.get(1));
    }

    @Test
    public void addAll_withNull_shouldThrowNullPointerException() {
        // Arrange
        JsonArray array = new JsonArray();

        try {
            // Act
            array.addAll(null);
            fail("Expected NullPointerException when adding a null array");
        } catch (NullPointerException expected) {
            // Assert: Exception was thrown as expected
        }
    }

    @Test
    public void add_withPrimitiveWrappers_shouldStoreAsJsonPrimitives() {
        // Arrange
        JsonArray array = new JsonArray();

        // Act
        array.add(true);
        array.add('c');
        array.add(123);
        array.add("hello");
        array.add((String) null); // Should be converted to JsonNull

        // Assert
        assertEquals(new JsonPrimitive(true), array.get(0));
        assertEquals(new JsonPrimitive('c'), array.get(1));
        assertEquals(new JsonPrimitive(123), array.get(2));
        assertEquals(new JsonPrimitive("hello"), array.get(3));
        assertEquals(JsonNull.INSTANCE, array.get(4));
    }

    @Test
    public void set_withValidIndex_shouldReplaceElementAndReturnOld() {
        // Arrange
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive(1));
        array.add(new JsonPrimitive(2));

        // Act
        JsonElement oldElement = array.set(1, new JsonPrimitive(3));

        // Assert
        assertEquals(new JsonPrimitive(2), oldElement);
        assertEquals(new JsonPrimitive(3), array.get(1));
        assertEquals(2, array.size());
    }

    @Test
    public void set_withInvalidIndex_shouldThrowIndexOutOfBoundsException() {
        // Arrange
        JsonArray array = new JsonArray();
        try {
            // Act
            array.set(0, new JsonPrimitive(1));
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Assert: Exception was thrown as expected
        }
    }

    @Test
    public void removeByIndex_withValidIndex_shouldRemoveAndReturnElement() {
        // Arrange
        JsonArray array = new JsonArray();
        JsonPrimitive elementA = new JsonPrimitive("a");
        JsonPrimitive elementB = new JsonPrimitive("b");
        array.add(elementA);
        array.add(elementB);

        // Act
        JsonElement removedElement = array.remove(0);

        // Assert
        assertEquals(elementA, removedElement);
        assertEquals(1, array.size());
        assertEquals(elementB, array.get(0));
    }

    @Test
    public void removeByIndex_withInvalidIndex_shouldThrowIndexOutOfBoundsException() {
        // Arrange
        JsonArray array = new JsonArray();
        try {
            // Act
            array.remove(0);
            fail("Expected IndexOutOfBoundsException for empty array");
        } catch (IndexOutOfBoundsException expected) {
            // Assert: Exception was thrown as expected
        }
    }

    @Test
    public void removeByElement_shouldModifyArrayAndReturnStatus() {
        // Arrange
        JsonArray array = new JsonArray();
        JsonPrimitive elementA = new JsonPrimitive("a");
        JsonPrimitive elementB = new JsonPrimitive("b");
        array.add(elementA);
        array.add(elementB);

        // Act & Assert for existing element
        assertTrue("remove should return true for an existing element", array.remove(elementA));
        assertEquals(1, array.size());
        assertFalse("Array should no longer contain the removed element", array.contains(elementA));

        // Act & Assert for non-existing element
        assertFalse("remove should return false for a non-existing element", array.remove(new JsonPrimitive("c")));
        assertEquals(1, array.size());
    }

    @Test
    public void contains_shouldReturnTrueForExistingElement() {
        // Arrange
        JsonArray array = new JsonArray();
        JsonPrimitive element = new JsonPrimitive("a");
        array.add(element);

        // Assert
        assertTrue("contains should return true for the exact element instance", array.contains(element));
        assertTrue("contains should return true for an equal element", array.contains(new JsonPrimitive("a")));
        assertFalse("contains should return false for a non-existing element", array.contains(new JsonPrimitive("b")));
    }

    // --- 'getAs...' Method Tests ---

    @Test
    public void getAsType_onSingleElementArray_shouldReturnConvertedValue() {
        // Arrange
        JsonArray numberArray = new JsonArray();
        numberArray.add(new JsonPrimitive(42));
        JsonArray stringArray = new JsonArray();
        stringArray.add(new JsonPrimitive("hello"));
        JsonArray boolArray = new JsonArray();
        boolArray.add(new JsonPrimitive(true));

        // Act & Assert
        assertEquals(new BigDecimal("42"), numberArray.getAsNumber());
        assertEquals(42.0, numberArray.getAsDouble(), 0.0);
        assertEquals(42.0f, numberArray.getAsFloat(), 0.0f);
        assertEquals(42L, numberArray.getAsLong());
        assertEquals(42, numberArray.getAsInt());
        assertEquals((short) 42, numberArray.getAsShort());
        assertEquals((byte) 42, numberArray.getAsByte());
        assertEquals(new BigInteger("42"), numberArray.getAsBigInteger());
        assertEquals(new BigDecimal("42"), numberArray.getAsBigDecimal());
        assertEquals('4', numberArray.getAsCharacter());

        assertEquals("hello", stringArray.getAsString());
        assertEquals('h', stringArray.getAsCharacter());

        assertTrue(boolArray.getAsBoolean());
    }

    @Test
    public void getAsType_onEmptyArray_shouldThrowIllegalStateException() {
        // Arrange
        JsonArray array = new JsonArray();
        try {
            // Act
            array.getAsString();
            fail("Expected IllegalStateException when calling getAs... on an empty array");
        } catch (IllegalStateException e) {
            // Assert
            assertEquals("Array must have size 1, but has size 0", e.getMessage());
        }
    }

    @Test
    public void getAsType_onMultiElementArray_shouldThrowIllegalStateException() {
        // Arrange
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive(1));
        array.add(new JsonPrimitive(2));
        try {
            // Act
            array.getAsInt();
            fail("Expected IllegalStateException when calling getAs... on a multi-element array");
        } catch (IllegalStateException e) {
            // Assert
            assertEquals("Array must have size 1, but has size 2", e.getMessage());
        }
    }

    @Test
    public void getAsNumericType_onNonNumericString_shouldThrowNumberFormatException() {
        // Arrange
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive("not a number"));
        try {
            // Act
            array.getAsInt();
            fail("Expected NumberFormatException for non-numeric content");
        } catch (NumberFormatException expected) {
            // Assert: Exception was thrown as expected
        }
    }

    @Test
    public void getAsType_onNullElement_shouldThrowUnsupportedOperationException() {
        // Arrange
        JsonArray array = new JsonArray();
        array.add((String) null); // Adds a JsonNull
        try {
            // Act
            array.getAsString();
            fail("Expected UnsupportedOperationException for a JsonNull element");
        } catch (UnsupportedOperationException e) {
            // Assert
            assertEquals("JsonNull", e.getMessage());
        }
    }

    // --- Object Method Tests ---

    @Test
    public void equals_shouldAdhereToContract() {
        // Arrange
        JsonArray a1 = new JsonArray(); // []
        JsonArray a2 = new JsonArray(); // []
        JsonArray a3 = new JsonArray(); // ["a"]
        a3.add(new JsonPrimitive("a"));
        JsonArray a4 = new JsonArray(); // ["a"]
        a4.add(new JsonPrimitive("a"));

        // Assert
        assertTrue("An array must be equal to itself", a1.equals(a1));
        assertTrue("Two empty arrays must be equal", a1.equals(a2));
        assertTrue("Two arrays with the same elements in the same order must be equal", a3.equals(a4));

        assertFalse("Arrays with different content must not be equal", a1.equals(a3));
        assertFalse("An array must not be equal to null", a1.equals(null));
        assertFalse("An array must not be equal to an object of a different type", a1.equals("[]"));
    }

    @Test
    public void hashCode_shouldBeConsistentWithEquals() {
        // Arrange
        JsonArray a1 = new JsonArray();
        JsonArray a2 = new JsonArray();
        JsonArray a3 = new JsonArray();
        a3.add(new JsonPrimitive("a"));

        // Assert
        assertEquals("Hashcode for two empty arrays must be the same", a1.hashCode(), a2.hashCode());
        assertNotEquals("Hashcode for arrays with different content should be different", a1.hashCode(), a3.hashCode());
    }

    @Test
    public void deepCopy_shouldCreateEqualButNotSameInstance() {
        // Arrange
        JsonArray original = new JsonArray();
        original.add(new JsonPrimitive("a"));
        JsonArray innerArray = new JsonArray();
        innerArray.add(new JsonPrimitive(1));
        original.add(innerArray);

        // Act
        JsonArray copy = original.deepCopy();

        // Assert
        assertNotSame("Deep copy should not be the same instance as the original", original, copy);
        assertEquals("Deep copy should be equal to the original", original, copy);

        // Verify it's a true deep copy by checking nested elements
        JsonArray copiedInnerArray = (JsonArray) copy.get(1);
        assertNotSame("Nested array in copy should not be the same instance", innerArray, copiedInnerArray);
        assertEquals("Nested array in copy should be equal to the original's", innerArray, copiedInnerArray);

        // Modifying the copy's inner array should not affect the original
        copiedInnerArray.add(new JsonPrimitive(2));
        assertEquals("Original inner array should be unchanged", 1, innerArray.size());
        assertEquals("Copied inner array should be modified", 2, copiedInnerArray.size());
    }

    // --- View Method Tests ---

    @Test
    public void iterator_shouldIterateOverElementsInOrder() {
        // Arrange
        JsonArray array = new JsonArray();
        JsonPrimitive p1 = new JsonPrimitive(1);
        JsonPrimitive p2 = new JsonPrimitive(2);
        array.add(p1);
        array.add(p2);

        // Act
        Iterator<JsonElement> iterator = array.iterator();

        // Assert
        assertTrue(iterator.hasNext());
        assertEquals(p1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(p2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void asList_shouldReturnLiveViewOfElements() {
        // Arrange
        JsonArray array = new JsonArray();
        JsonPrimitive p1 = new JsonPrimitive(1);
        JsonPrimitive p2 = new JsonPrimitive(2);
        array.add(p1);
        array.add(p2);

        // Act
        List<JsonElement> list = array.asList();

        // Assert initial state
        assertEquals(Arrays.asList(p1, p2), list);
        assertEquals(2, list.size());

        // Assert that the list is a live view (modifying list affects array)
        list.remove(0);
        assertEquals("Removing from list should affect array size", 1, array.size());
        assertEquals("Array should reflect element removal from list", p2, array.get(0));

        // Assert that the list is a live view (modifying array affects list)
        array.add(p1);
        assertEquals("Adding to array should affect list size", 2, list.size());
        assertEquals("List should reflect element addition to array", p1, list.get(1));
    }
}