package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest4 {

    @Test
    public void testSet() {
        JsonArray array = new JsonArray();
        assertThrows(IndexOutOfBoundsException.class, () -> array.set(0, new JsonPrimitive(1)));
        JsonPrimitive a = new JsonPrimitive("a");
        array.add(a);
        JsonPrimitive b = new JsonPrimitive("b");
        JsonElement oldValue = array.set(0, b);
        assertThat(oldValue).isEqualTo(a);
        assertThat(array.get(0).getAsString()).isEqualTo("b");
        oldValue = array.set(0, null);
        assertThat(oldValue).isEqualTo(b);
        assertThat(array.get(0)).isEqualTo(JsonNull.INSTANCE);
        oldValue = array.set(0, new JsonPrimitive("c"));
        assertThat(oldValue).isEqualTo(JsonNull.INSTANCE);
        assertThat(array.get(0).getAsString()).isEqualTo("c");
        assertThat(array).hasSize(1);
    }
}
