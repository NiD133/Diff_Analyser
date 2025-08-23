package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest3 {

    @Test
    public void testRemove() {
        JsonArray array = new JsonArray();
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(0));
        JsonPrimitive a = new JsonPrimitive("a");
        array.add(a);
        assertThat(array.remove(a)).isTrue();
        assertThat(array).doesNotContain(a);
        array.add(a);
        array.add(new JsonPrimitive("b"));
        assertThat(array.remove(1).getAsString()).isEqualTo("b");
        assertThat(array).hasSize(1);
        assertThat(array).contains(a);
    }
}
