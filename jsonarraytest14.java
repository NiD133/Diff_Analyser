package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest14 {

    @Test
    public void testAdd_withMixedTypes_createsExpectedArray() {
        // Arrange: Create an expected JsonArray to compare against.
        // This clearly documents the expected state of the array after all additions.
        JsonArray expectedArray = new JsonArray();
        expectedArray.add(new JsonPrimitive('a'));
        expectedArray.add(new JsonPrimitive("apple"));
        expectedArray.add(new JsonPrimitive(12121));
        expectedArray.add(new JsonPrimitive('o'));
        expectedArray.add(JsonNull.INSTANCE); // null Boolean becomes JsonNull
        expectedArray.add(JsonNull.INSTANCE); // null Character becomes JsonNull
        expectedArray.add(new JsonPrimitive(12.232));
        expectedArray.add(new JsonPrimitive(BigInteger.valueOf(2323)));

        // Act: Add various data types to the JsonArray.
        JsonArray actualArray = new JsonArray();
        actualArray.add('a');
        actualArray.add("apple");
        actualArray.add(12121);
        actualArray.add('o'); // Use 'o' directly instead of the obscure (char) 111
        actualArray.add((Boolean) null);
        actualArray.add((Character) null);
        actualArray.add(12.232);
        actualArray.add(BigInteger.valueOf(2323));

        // Assert: Verify that the actual array matches the expected one.
        // This is more robust than comparing string representations.
        assertThat(actualArray).isEqualTo(expectedArray);
    }
}