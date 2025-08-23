package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest16 {

    @Test
    public void testNullJsonElementAddition() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((JsonElement) null);
        assertThat(jsonArray.get(0)).isEqualTo(JsonNull.INSTANCE);
    }
}
