package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest5 {

    @Test
    public void testDeepCopy() {
        JsonArray original = new JsonArray();
        JsonArray firstEntry = new JsonArray();
        original.add(firstEntry);
        JsonArray copy = original.deepCopy();
        original.add(new JsonPrimitive("y"));
        assertThat(copy).hasSize(1);
        firstEntry.add(new JsonPrimitive("z"));
        assertThat(original.get(0).getAsJsonArray()).hasSize(1);
        assertThat(copy.get(0).getAsJsonArray()).hasSize(0);
    }
}
