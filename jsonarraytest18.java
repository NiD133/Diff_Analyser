package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest18 {

    @Test
    public void testToString() {
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
        assertThat(array.toString()).isEqualTo("[null,NaN,\"a\\u0000\",[\"\\\"\"],{\"n\\u0000\":1}]");
    }
}
