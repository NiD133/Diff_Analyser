package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Test class for {@link JsonArray}.
 * Renamed from JsonArrayTestTest11 for clarity.
 */
public class JsonArrayTest {

    @Test
    public void add_withDoubleAndNull_buildsCorrectArray() {
        // Arrange
        JsonArray actualArray = new JsonArray();

        // Act
        actualArray.add(1.0);
        actualArray.add(2.13232);
        actualArray.add(0.121);
        // According to JsonArray's contract, null values are converted to JsonNull
        actualArray.add((Double) null);
        actualArray.add(-0.00234);
        actualArray.add((Double) null);

        // Assert
        JsonArray expectedArray = new JsonArray();
        expectedArray.add(new JsonPrimitive(1.0));
        expectedArray.add(new JsonPrimitive(2.13232));
        expectedArray.add(new JsonPrimitive(0.121));
        expectedArray.add(JsonNull.INSTANCE);
        expectedArray.add(new JsonPrimitive(-0.00234));
        expectedArray.add(JsonNull.INSTANCE);

        assertThat(actualArray).isEqualTo(expectedArray);
    }
}