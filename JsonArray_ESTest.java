package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

/**
 * Test suite for JsonArray functionality.
 * Tests cover basic operations, type conversions, edge cases, and error conditions.
 */
public class JsonArrayTest {

    // ========== Basic Array Operations ==========
    
    @Test
    public void shouldReturnCorrectSizeAfterAddingElement() {
        JsonArray array = new JsonArray();
        array.add(0.0f);
        
        assertEquals(1, array.size());
    }
    
    @Test
    public void shouldReplaceElementAtSpecifiedIndex() {
        JsonArray array = new JsonArray();
        array.add(0.0f);
        
        JsonElement replacedElement = array.set(0, null);
        
        assertNotNull(replacedElement);
        assertTrue(replacedElement.isJsonPrimitive());
    }
    
    @Test
    public void shouldRemoveElementSuccessfully() {
        JsonArray array = new JsonArray();
        array.add(array); // Add self-reference
        
        boolean wasRemoved = array.remove(array);
        
        assertTrue(wasRemoved);
        assertEquals(0, array.size());
    }
    
    @Test
    public void shouldRemoveElementByIndex() {
        JsonArray array = new JsonArray(10);
        array.add('\\');
        
        JsonElement removedElement = array.remove(0);
        
        assertNotNull(removedElement);
        assertFalse(removedElement.isJsonNull());
    }
    
    @Test
    public void shouldProvideIteratorForElements() {
        JsonArray array = new JsonArray();
        
        Iterator<JsonElement> iterator = array.iterator();
        
        assertNotNull(iterator);
    }

    // ========== Type Conversion Tests ==========
    
    @Test
    public void shouldConvertSingleStringElementToString() {
        JsonArray array = new JsonArray();
        array.add("");
        
        String result = array.getAsString();
        
        assertEquals("", result);
    }
    
    @Test
    public void shouldConvertSingleIntegerToString() {
        JsonArray array = new JsonArray();
        array.add(-3);
        
        String result = array.getAsString();
        
        assertNotNull(result);
    }
    
    @Test
    public void shouldConvertCharacterToNumericTypes() {
        JsonArray array = new JsonArray();
        array.add('6');
        
        assertEquals((short) 6, array.getAsShort());
        assertEquals(6L, array.getAsLong());
        assertEquals(6, array.getAsInt());
        assertEquals(6.0f, array.getAsFloat(), 0.01f);
        assertEquals(6.0, array.getAsDouble(), 0.01);
        assertEquals((byte) 6, array.getAsByte());
    }
    
    @Test
    public void shouldConvertFloatToNumericTypes() {
        JsonArray array = new JsonArray();
        array.add(0.0f);
        
        assertEquals(0L, array.getAsLong());
        assertEquals(0.0f, array.getAsFloat(), 0.01f);
        assertEquals((byte) 0, array.getAsByte());
    }
    
    @Test
    public void shouldConvertNegativeFloatCorrectly() {
        JsonArray array = new JsonArray();
        array.add(-19.963131388716253f);
        
        assertEquals(-19.96313f, array.getAsFloat(), 0.01f);
    }
    
    @Test
    public void shouldConvertCharacterToCharacter() {
        JsonArray array = new JsonArray();
        array.add('~');
        
        assertEquals('~', array.getAsCharacter());
    }
    
    @Test
    public void shouldConvertNumberToCharacterRepresentation() {
        JsonArray array = new JsonArray();
        array.add(0.0f);
        
        // Numbers get converted to string first, then first character is taken
        char result = array.getAsCharacter();
        assertEquals('j', result); // First character of JSON representation
    }
    
    @Test
    public void shouldConvertStringToBoolean() {
        JsonArray array = new JsonArray();
        array.add("+");
        
        assertFalse(array.getAsBoolean());
    }
    
    @Test
    public void shouldConvertBooleanToBoolean() {
        JsonArray array = new JsonArray();
        array.add(true);
        
        assertTrue(array.getAsBoolean());
    }
    
    @Test
    public void shouldConvertToBigInteger() {
        JsonArray array = new JsonArray();
        array.add(1);
        
        BigInteger result = array.getAsBigInteger();
        assertEquals(1, result.shortValue());
    }
    
    @Test
    public void shouldConvertToBigDecimal() {
        JsonArray array = new JsonArray();
        array.add('6');
        
        BigDecimal result = array.getAsBigDecimal();
        assertEquals(6, result.shortValue());
    }

    // ========== Collection Operations ==========
    
    @Test
    public void shouldGetElementByIndex() {
        JsonArray array = new JsonArray();
        array.add("");
        
        JsonElement element = array.get(0);
        
        assertNotNull(element);
        assertFalse(element.isJsonObject());
    }
    
    @Test
    public void shouldConvertToList() {
        JsonArray array = new JsonArray();
        array.add(0.0f);
        
        List<JsonElement> list = array.asList();
        
        assertEquals(1, list.size());
    }
    
    @Test
    public void shouldCheckIfEmpty() {
        JsonArray emptyArray = new JsonArray();
        assertTrue(emptyArray.isEmpty());
        
        emptyArray.add('6');
        assertFalse(emptyArray.isEmpty());
    }
    
    @Test
    public void shouldCheckContainment() {
        JsonArray array = new JsonArray();
        array.add(array); // Self-reference
        
        assertTrue(array.contains(array));
    }
    
    @Test
    public void shouldAddAllElementsFromAnotherArray() {
        JsonArray array = new JsonArray();
        JsonArray emptyArray = new JsonArray();
        
        array.addAll(emptyArray);
        
        // Should complete without error
        assertFalse(array.isJsonPrimitive());
    }

    // ========== Equality and Hashing ==========
    
    @Test
    public void shouldNotEqualNull() {
        JsonArray array = new JsonArray();
        
        assertFalse(array.equals(null));
    }
    
    @Test
    public void shouldEqualItself() {
        JsonArray array = new JsonArray();
        
        assertTrue(array.equals(array));
    }
    
    @Test
    public void shouldEqualDeepCopy() {
        JsonArray array = new JsonArray();
        JsonArray copy = array.deepCopy();
        
        assertTrue(copy.equals(array));
    }
    
    @Test
    public void shouldNotEqualDifferentArray() {
        JsonArray array1 = new JsonArray();
        array1.add((Boolean) null);
        JsonArray array2 = new JsonArray(1);
        
        assertFalse(array1.equals(array2));
    }
    
    @Test
    public void shouldHandleDeepCopyWithNestedElements() {
        JsonArray parent = new JsonArray();
        JsonArray child = new JsonArray();
        parent.add(child);
        
        JsonArray parentCopy = parent.deepCopy();
        
        assertFalse(parent.contains(parentCopy));
    }
    
    @Test
    public void shouldCalculateHashCode() {
        JsonArray array = new JsonArray();
        
        // Should not throw exception
        array.hashCode();
    }

    // ========== Error Conditions ==========
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowExceptionWhenSettingInvalidIndex() {
        JsonArray array = new JsonArray();
        array.set(1, null);
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void shouldThrowExceptionWhenRemovingNegativeIndex() {
        JsonArray array = new JsonArray();
        array.remove(-1);
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenGettingStringFromEmptyArray() {
        JsonArray array = new JsonArray();
        array.getAsString();
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenGettingNumberFromEmptyArray() {
        JsonArray array = new JsonArray();
        array.getAsNumber();
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenGettingBooleanFromEmptyArray() {
        JsonArray array = new JsonArray();
        array.getAsBoolean();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNegativeCapacity() {
        new JsonArray(-1905);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowExceptionWhenGettingFromEmptyArray() {
        JsonArray array = new JsonArray();
        array.get(0);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowExceptionWhenRemovingFromEmptyArray() {
        JsonArray array = new JsonArray(846);
        array.remove(0);
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenAddingNullArray() {
        JsonArray array = new JsonArray();
        array.addAll(null);
    }

    // ========== Null Element Handling ==========
    
    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowExceptionWhenConvertingNullToShort() {
        JsonArray array = new JsonArray();
        array.add((Number) null);
        array.getAsShort();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowExceptionWhenConvertingNullToNumber() {
        JsonArray array = new JsonArray();
        array.add((JsonElement) null);
        array.getAsNumber();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowExceptionWhenConvertingNullToString() {
        JsonArray array = new JsonArray();
        array.add((Character) null);
        array.getAsString();
    }
    
    @Test
    public void shouldHandleNullElementRemoval() {
        JsonArray array = new JsonArray();
        array.add((String) null);
        
        JsonElement removed = array.remove(0);
        
        assertNotNull(removed);
        assertFalse(removed.isJsonArray());
    }

    // ========== Invalid Conversion Tests ==========
    
    @Test(expected = NumberFormatException.class)
    public void shouldThrowExceptionWhenConvertingInvalidStringToNumber() {
        JsonArray array = new JsonArray();
        array.add("invalid_number");
        array.getAsByte();
    }
    
    @Test(expected = NumberFormatException.class)
    public void shouldThrowExceptionWhenConvertingBooleanToInteger() {
        JsonArray array = new JsonArray();
        array.add(false);
        array.getAsInt();
    }
    
    @Test(expected = NumberFormatException.class)
    public void shouldThrowExceptionWhenConvertingInvalidCharacterToNumber() {
        JsonArray array = new JsonArray();
        array.add('@');
        array.getAsShort();
    }
    
    @Test(expected = NumberFormatException.class)
    public void shouldThrowExceptionWhenConvertingEmptyStringToBigDecimal() {
        JsonArray array = new JsonArray();
        array.add("");
        array.getAsBigDecimal();
    }

    // ========== Edge Cases ==========
    
    @Test
    public void shouldReturnZeroSizeForEmptyArray() {
        JsonArray array = new JsonArray();
        assertEquals(0, array.size());
    }
    
    @Test
    public void shouldReturnEmptyListForEmptyArray() {
        JsonArray array = new JsonArray();
        List<JsonElement> list = array.asList();
        assertEquals(0, list.size());
    }
    
    @Test
    public void shouldNotRemoveNonExistentElement() {
        JsonArray array = new JsonArray(846);
        array.add(846.0f);
        
        boolean wasRemoved = array.remove(array);
        
        assertFalse(wasRemoved);
        assertFalse(array.isEmpty());
    }
}