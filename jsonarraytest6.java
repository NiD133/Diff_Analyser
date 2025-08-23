package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest6 {

    @Test
    public void testIsEmpty() {
        JsonArray array = new JsonArray();
        assertThat(array).isEmpty();
        JsonPrimitive a = new JsonPrimitive("a");
        array.add(a);
        assertThat(array).isNotEmpty();
        array.remove(0);
        assertThat(array).isEmpty();
    }
}
