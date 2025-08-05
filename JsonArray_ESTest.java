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
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class JsonArray_ESTest extends JsonArray_ESTest_scaffolding {

    private static final float FLOAT_ZERO = 0.0f;
    private static final int INITIAL_CAPACITY = 846;
    private static final char CHAR_SIX = '6';

    @Test(timeout = 4000)
    public void testAddFloatIncreasesSize() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(FLOAT_ZERO);
        assertEquals(1, jsonArray.size());
    }

    @Test(timeout = 4000)
    public void testSetNullJsonElement() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(FLOAT_ZERO);
        JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonArray.set(0, null);
        assertFalse(jsonPrimitive.isBoolean());
    }

    @Test(timeout = 4000)
    public void testRemoveSelfReference() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonArray);
        boolean removed = jsonArray.remove(jsonArray);
        assertEquals(0, jsonArray.size());
        assertTrue(removed);
    }

    @Test(timeout = 4000)
    public void testRemoveCharacter() throws Throwable {
        JsonArray jsonArray = new JsonArray(INITIAL_CAPACITY);
        jsonArray.add(Character.valueOf('\\'));
        JsonElement removedElement = jsonArray.remove(0);
        assertFalse(removedElement.isJsonNull());
    }

    @Test(timeout = 4000)
    public void testIteratorNotNull() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        Iterator<JsonElement> iterator = jsonArray.iterator();
        assertNotNull(iterator);
    }

    @Test(timeout = 4000)
    public void testGetAsStringNotNull() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(-3);
        String stringValue = jsonArray.getAsString();
        assertNotNull(stringValue);
    }

    @Test(timeout = 4000)
    public void testGetAsStringEmptyString() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("");
        String stringValue = jsonArray.getAsString();
        assertEquals("", stringValue);
    }

    @Test(timeout = 4000)
    public void testGetAsShort() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(CHAR_SIX);
        short shortValue = jsonArray.getAsShort();
        assertEquals((short) 6, shortValue);
    }

    @Test(timeout = 4000)
    public void testGetAsNumber() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(CHAR_SIX);
        Number numberValue = jsonArray.getAsNumber();
        assertEquals((byte) 6, numberValue.byteValue());
    }

    @Test(timeout = 4000)
    public void testGetAsLongZero() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(FLOAT_ZERO);
        long longValue = jsonArray.getAsLong();
        assertEquals(0L, longValue);
    }

    @Test(timeout = 4000)
    public void testGetAsLongCharacter() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(CHAR_SIX);
        long longValue = jsonArray.getAsLong();
        assertEquals(6L, longValue);
    }

    @Test(timeout = 4000)
    public void testGetAsInt() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(CHAR_SIX);
        int intValue = jsonArray.getAsInt();
        assertEquals(6, intValue);
    }

    @Test(timeout = 4000)
    public void testGetAsFloatZero() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(FLOAT_ZERO);
        float floatValue = jsonArray.getAsFloat();
        assertEquals(0.0F, floatValue, 0.01F);
    }

    @Test(timeout = 4000)
    public void testGetAsFloatCharacter() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(CHAR_SIX);
        float floatValue = jsonArray.getAsFloat();
        assertEquals(6.0F, floatValue, 0.01F);
    }

    @Test(timeout = 4000)
    public void testGetAsFloatNegative() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(-19.963131388716253f);
        float floatValue = jsonArray.getAsFloat();
        assertEquals(-19.96313F, floatValue, 0.01F);
    }

    @Test(timeout = 4000)
    public void testGetAsDouble() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(CHAR_SIX);
        double doubleValue = jsonArray.getAsDouble();
        assertEquals(6.0, doubleValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testGetAsCharacter() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('~');
        char charValue = jsonArray.getAsCharacter();
        assertEquals('~', charValue);
    }

    @Test(timeout = 4000)
    public void testGetAsCharacterSix() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(CHAR_SIX);
        char charValue = jsonArray.getAsCharacter();
        assertEquals(CHAR_SIX, charValue);
    }

    @Test(timeout = 4000)
    public void testGetAsCharacterFromFloat() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(FLOAT_ZERO);
        char charValue = jsonArray.getAsCharacter();
        assertEquals('j', charValue);
    }

    @Test(timeout = 4000)
    public void testGetAsByteZero() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(FLOAT_ZERO);
        byte byteValue = jsonArray.getAsByte();
        assertEquals((byte) 0, byteValue);
    }

    @Test(timeout = 4000)
    public void testGetAsByteNegative() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(-3);
        byte byteValue = jsonArray.getAsByte();
        assertEquals((byte) -3, byteValue);
    }

    @Test(timeout = 4000)
    public void testGetAsBooleanFromString() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("+");
        boolean booleanValue = jsonArray.getAsBoolean();
        assertFalse(booleanValue);
    }

    @Test(timeout = 4000)
    public void testGetAsBigInteger() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(1);
        BigInteger bigIntegerValue = jsonArray.getAsBigInteger();
        assertEquals((short) 1, bigIntegerValue.shortValue());
    }

    @Test(timeout = 4000)
    public void testGetAsBigDecimal() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(CHAR_SIX);
        BigDecimal bigDecimalValue = jsonArray.getAsBigDecimal();
        assertEquals((short) 6, bigDecimalValue.shortValue());
    }

    @Test(timeout = 4000)
    public void testGetJsonElement() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        JsonPrimitive jsonPrimitive = new JsonPrimitive("");
        jsonArray.add(jsonPrimitive);
        JsonElement jsonElement = jsonArray.get(0);
        assertFalse(jsonElement.isJsonObject());
    }

    @Test(timeout = 4000)
    public void testAsListSize() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(FLOAT_ZERO);
        List<JsonElement> list = jsonArray.asList();
        assertEquals(1, list.size());
    }

    @Test(timeout = 4000)
    public void testSetOutOfBounds() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.set(1, null);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testRemoveNegativeIndex() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.remove(-1);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsShortFromNull() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Number) null);
        try {
            jsonArray.getAsShort();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsShortFromInvalidChar() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('@');
        try {
            jsonArray.getAsShort();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsLongFromNull() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Number) null);
        try {
            jsonArray.getAsLong();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsLongFromInvalidChar() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('~');
        try {
            jsonArray.getAsLong();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsIntFromNull() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Number) null);
        try {
            jsonArray.getAsInt();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsIntFromBoolean() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(Boolean.FALSE);
        try {
            jsonArray.getAsInt();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsFloatFromNull() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Number) null);
        try {
            jsonArray.getAsFloat();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsFloatFromInvalidChar() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('|');
        try {
            jsonArray.getAsFloat();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsDoubleFromNull() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Number) null);
        try {
            jsonArray.getAsDouble();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsDoubleFromInvalidString() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("=^K4eh7 E_%>\"E3QB7U");
        try {
            jsonArray.getAsDouble();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsCharacterFromNull() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Boolean) null);
        try {
            jsonArray.getAsCharacter();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsByteFromNull() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Number) null);
        try {
            jsonArray.getAsByte();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsByteFromInvalidString() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("rs68C\"p!#b))3'caR4");
        try {
            jsonArray.getAsByte();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsBooleanFromNull() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Number) null);
        try {
            jsonArray.getAsBoolean();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsBigIntegerFromNull() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Number) null);
        try {
            jsonArray.getAsBigInteger();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsBigIntegerFromBoolean() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(Boolean.TRUE);
        try {
            jsonArray.getAsBigInteger();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsBigDecimalFromNull() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Number) null);
        try {
            jsonArray.getAsBigDecimal();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsBigDecimalFromEmptyString() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("");
        try {
            jsonArray.getAsBigDecimal();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetOutOfBounds() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.get(-469);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testAddAllNull() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.addAll(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testIllegalCapacity() throws Throwable {
        try {
            new JsonArray(-1905);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEqualsNull() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        assertFalse(jsonArray.equals(null));
    }

    @Test(timeout = 4000)
    public void testDeepCopyNotContains() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        JsonArray jsonArray1 = new JsonArray();
        jsonArray1.add(jsonArray);
        JsonArray jsonArray2 = jsonArray1.deepCopy();
        assertFalse(jsonArray1.contains(jsonArray2));
        assertFalse(jsonArray1.equals(jsonArray));
    }

    @Test(timeout = 4000)
    public void testEqualsSelf() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        assertTrue(jsonArray.equals(jsonArray));
    }

    @Test(timeout = 4000)
    public void testGetAsByteFromCharacter() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(CHAR_SIX);
        byte byteValue = jsonArray.getAsByte();
        assertEquals((byte) 6, byteValue);
    }

    @Test(timeout = 4000)
    public void testIsEmpty() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        assertTrue(jsonArray.isEmpty());
    }

    @Test(timeout = 4000)
    public void testIsEmptyAfterAdd() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        assertTrue(jsonArray.isEmpty());
        jsonArray.add(CHAR_SIX);
        assertFalse(jsonArray.isEmpty());
    }

    @Test(timeout = 4000)
    public void testContainsSelf() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonArray);
        assertTrue(jsonArray.contains(jsonArray));
    }

    @Test(timeout = 4000)
    public void testSetNegativeIndex() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.set(-1411, null);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testSetNegativeIndexSelf() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.set(-1, jsonArray);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsNumberFromNull() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((JsonElement) null);
        try {
            jsonArray.getAsNumber();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testRemoveNullElement() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((String) null);
        JsonElement removedElement = jsonArray.remove(0);
        assertFalse(removedElement.isJsonArray());
    }

    @Test(timeout = 4000)
    public void testRemoveSelfReferenceFails() throws Throwable {
        JsonArray jsonArray = new JsonArray(INITIAL_CAPACITY);
        jsonArray.add((Number) (float) INITIAL_CAPACITY);
        boolean removed = jsonArray.remove(jsonArray);
        assertFalse(jsonArray.isEmpty());
        assertFalse(removed);
    }

    @Test(timeout = 4000)
    public void testGetAsStringFromNullCharacter() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Character) null);
        try {
            jsonArray.getAsString();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEqualsDifferentArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Boolean) null);
        JsonArray jsonArray1 = new JsonArray(1);
        assertFalse(jsonArray.equals(jsonArray1));
    }

    @Test(timeout = 4000)
    public void testGetAsBooleanTrue() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(Boolean.TRUE);
        assertTrue(jsonArray.getAsBoolean());
    }

    @Test(timeout = 4000)
    public void testDeepCopyEquals() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        JsonArray jsonArray1 = jsonArray.deepCopy();
        assertTrue(jsonArray1.equals(jsonArray));
    }

    @Test(timeout = 4000)
    public void testGetAsBigDecimalEmptyArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.getAsBigDecimal();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testRemoveFromEmptyArray() throws Throwable {
        JsonArray jsonArray = new JsonArray(INITIAL_CAPACITY);
        try {
            jsonArray.remove(0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testHashCode() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.hashCode();
    }

    @Test(timeout = 4000)
    public void testGetFromEmptyArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.get(0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testAsListEmpty() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        List<JsonElement> list = jsonArray.asList();
        assertEquals(0, list.size());
    }

    @Test(timeout = 4000)
    public void testGetAsNumberEmptyArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.getAsNumber();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsByteEmptyArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.getAsByte();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testAddAllSelf() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        jsonArray.addAll(jsonArray);
        assertFalse(jsonArray.isJsonPrimitive());
    }

    @Test(timeout = 4000)
    public void testGetAsBooleanEmptyArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.getAsBoolean();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsBigIntegerEmptyArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.getAsBigInteger();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsStringEmptyArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.getAsString();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsLongEmptyArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.getAsLong();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsFloatEmptyArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.getAsFloat();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsDoubleEmptyArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.getAsDouble();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testSizeEmptyArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        assertEquals(0, jsonArray.size());
    }

    @Test(timeout = 4000)
    public void testGetAsIntEmptyArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.getAsInt();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsShortEmptyArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.getAsShort();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetAsCharacterEmptyArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray.getAsCharacter();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
}