/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public final class JsonArrayTest {

    // Equality tests
    @Test
    public void testEquals_emptyArraysAreEqual() {
        MoreAsserts.assertEqualsAndHashCode(new JsonArray(), new JsonArray());
    }

    @Test
    public void testEquals_nonEmptyArraysWithSameElementsAreEqual() {
        JsonArray a = new JsonArray();
        a.add(new JsonObject());
        
        JsonArray b = new JsonArray();
        b.add(new JsonObject());
        
        MoreAsserts.assertEqualsAndHashCode(a, b);
    }

    @Test
    public void testEquals_arraysOfDifferentSizesAreNotEqual() {
        JsonArray a = new JsonArray();
        a.add(new JsonObject());
        a.add(new JsonObject());
        
        JsonArray b = new JsonArray();
        b.add(new JsonObject());
        
        assertThat(a.equals(b)).isFalse();
        assertThat(b.equals(a)).isFalse();
    }

    @Test
    public void testEquals_arraysWithDifferentElementsAreNotEqual() {
        JsonArray a = new JsonArray();
        a.add(new JsonObject());
        
        JsonArray b = new JsonArray();
        b.add(JsonNull.INSTANCE);
        
        assertThat(a.equals(b)).isFalse();
        assertThat(b.equals(a)).isFalse();
    }

    // Modification tests
    @Test
    public void testRemove_removesElementsByIndex() {
        JsonArray array = new JsonArray();
        JsonPrimitive a = new JsonPrimitive("a");
        JsonPrimitive b = new JsonPrimitive("b");
        
        array.add(a);
        array.add(b);
        
        JsonElement removed = array.remove(1);
        assertThat(removed).isEqualTo(b);
        assertThat(array).containsExactly(a);
    }

    @Test
    public void testRemove_removesElementsByValue() {
        JsonArray array = new JsonArray();
        JsonPrimitive a = new JsonPrimitive("a");
        
        array.add(a);
        boolean removed = array.remove(a);
        
        assertThat(removed).isTrue();
        assertThat(array).doesNotContain(a);
    }

    @Test
    public void testRemove_throwsForInvalidIndex() {
        JsonArray array = new JsonArray();
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(0));
    }

    @Test
    public void testSet_replacesElementAtIndex() {
        JsonArray array = new JsonArray();
        JsonPrimitive a = new JsonPrimitive("a");
        array.add(a);

        JsonPrimitive b = new JsonPrimitive("b");
        JsonElement oldValue = array.set(0, b);
        
        assertThat(oldValue).isEqualTo(a);
        assertThat(array.get(0)).isEqualTo(b);
    }

    @Test
    public void testSet_convertsNullToJsonNull() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive("a"));
        
        JsonElement oldValue = array.set(0, null);
        assertThat(array.get(0)).isEqualTo(JsonNull.INSTANCE);
    }

    @Test
    public void testSet_throwsForInvalidIndex() {
        JsonArray array = new JsonArray();
        assertThrows(IndexOutOfBoundsException.class, () -> array.set(0, new JsonPrimitive(1)));
    }

    @Test
    public void testDeepCopy_createsIsolatedCopy() {
        JsonArray original = new JsonArray();
        JsonArray firstEntry = new JsonArray();
        original.add(firstEntry);

        JsonArray copy = original.deepCopy();
        original.add(new JsonPrimitive("y"));
        firstEntry.add(new JsonPrimitive("z"));

        assertThat(copy).hasSize(1);
        assertThat(copy.get(0).getAsJsonArray()).isEmpty();
        assertThat(original.get(0).getAsJsonArray()).hasSize(1);
    }

    @Test
    public void testIsEmpty_returnsCorrectState() {
        JsonArray array = new JsonArray();
        assertThat(array).isEmpty();

        array.add(new JsonPrimitive("a"));
        assertThat(array).isNotEmpty();

        array.remove(0);
        assertThat(array).isEmpty();
    }

    // Primitive addition tests
    @Test
    public void testAdd_stringValues() {
        JsonArray array = new JsonArray();
        array.add("Hello");
        array.add("Goodbye");
        array.add((String) null);
        
        assertThat(array.toString()).isEqualTo("[\"Hello\",\"Goodbye\",null]");
    }

    @Test
    public void testAdd_integerValues() {
        JsonArray array = new JsonArray();
        array.add(1);
        array.add(-3);
        array.add((Integer) null);
        
        assertThat(array.toString()).isEqualTo("[1,-3,null]");
    }

    @Test
    public void testAdd_doubleValues() {
        JsonArray array = new JsonArray();
        array.add(1.0);
        array.add(-0.00234);
        array.add((Double) null);
        
        assertThat(array.toString()).isEqualTo("[1.0,-0.00234,null]");
    }

    @Test
    public void testAdd_booleanValues() {
        JsonArray array = new JsonArray();
        array.add(true);
        array.add(false);
        array.add((Boolean) null);
        
        assertThat(array.toString()).isEqualTo("[true,false,null]");
    }

    @Test
    public void testAdd_charValues() {
        JsonArray array = new JsonArray();
        array.add('a');
        array.add('o');
        array.add((Character) null);
        
        assertThat(array.toString()).isEqualTo("[\"a\",\"o\",null]");
    }

    @Test
    public void testAdd_mixedPrimitives() {
        JsonArray array = new JsonArray();
        array.add('a');
        array.add("apple");
        array.add(12121);
        array.add((Boolean) null);
        
        assertThat(array.toString()).isEqualTo("[\"a\",\"apple\",12121,null]");
    }

    @Test
    public void testAdd_nullValuesConvertToJsonNull() {
        JsonArray array = new JsonArray();
        array.add((Character) null);
        array.add((Boolean) null);
        array.add((JsonElement) null);
        
        assertThat(array.toString()).isEqualTo("[null,null,null]");
        for (JsonElement element : array) {
            assertThat(element).isEqualTo(JsonNull.INSTANCE);
        }
    }

    // Accessor exception tests
    @Test
    public void testGetAsXxx_throwsWhenArraySizeNotOne() {
        JsonArray array = new JsonArray();
        assertThrows(IllegalStateException.class, array::getAsByte);
        
        array.add(true);
        array.add(false);
        assertThrows(IllegalStateException.class, array::getAsByte);
    }

    @Test
    public void testGet_throwsForInvalidIndex() {
        JsonArray array = new JsonArray();
        array.add(new JsonObject());
        
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(1));
    }

    @Test
    public void testGetAsBoolean_throwsForNonBooleanElement() {
        JsonArray array = new JsonArray();
        array.add(new JsonObject());
        
        Exception e = assertThrows(UnsupportedOperationException.class, array::getAsBoolean);
        assertThat(e).hasMessageThat().isEqualTo("JsonObject");
    }

    @Test
    public void testGetAsString_throwsForNonStringElement() {
        JsonArray array = new JsonArray();
        array.add(new JsonObject());
        
        Exception e = assertThrows(UnsupportedOperationException.class, array::getAsString);
        assertThat(e).hasMessageThat().isEqualTo("JsonObject");
    }

    @Test
    public void testGetAsDouble_throwsForNonNumericElement() {
        JsonArray array = new JsonArray();
        array.add("hello");
        
        Exception e = assertThrows(NumberFormatException.class, array::getAsDouble);
        assertThat(e).hasMessageThat().isEqualTo("For input string: \"hello\"");
    }

    @Test
    public void testGetAsJsonObject_throwsWhenCalledOnArray() {
        JsonArray array = new JsonArray();
        array.add("hello");
        
        Exception e = assertThrows(IllegalStateException.class, array::getAsJsonObject);
        assertThat(e).hasMessageThat().isEqualTo("Not a JSON Object: [\"hello\"]");
    }

    @Test
    public void testGetAsJsonArray_throwsForNonArrayElement() {
        JsonArray array = new JsonArray();
        array.add("hello");
        
        Exception e = assertThrows(IllegalStateException.class, 
            () -> array.get(0).getAsJsonArray());
        assertThat(e).hasMessageThat().isEqualTo("Not a JSON Array: \"hello\"");
    }

    @Test
    public void testToString_formatsCorrectly() {
        JsonArray array = new JsonArray();
        assertThat(array.toString()).isEqualTo("[]");
        
        array.add(JsonNull.INSTANCE);
        array.add(Float.NaN);
        array.add("a\0");
        
        JsonArray nestedArray = new JsonArray();
        nestedArray.add('"');
        array.add(nestedArray);
        
        JsonObject nestedObject = new JsonObject();
        nestedObject.addProperty("n\0", 1);
        array.add(nestedObject);
        
        assertThat(array.toString())
            .isEqualTo("[null,NaN,\"a\\u0000\",[\"\\\"\"],{\"n\\u0000\":1}]");
    }
}