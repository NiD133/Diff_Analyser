package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest15 {

    @Test
    public void testNullPrimitiveAddition() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Character) null);
        jsonArray.add((Boolean) null);
        jsonArray.add((Integer) null);
        jsonArray.add((Double) null);
        jsonArray.add((Float) null);
        jsonArray.add((BigInteger) null);
        jsonArray.add((String) null);
        jsonArray.add((Boolean) null);
        jsonArray.add((Number) null);
        assertThat(jsonArray.toString()).isEqualTo("[null,null,null,null,null,null,null,null,null]");
        for (int i = 0; i < jsonArray.size(); i++) {
            // Verify that they are actually a JsonNull and not a Java null
            assertThat(jsonArray.get(i)).isEqualTo(JsonNull.INSTANCE);
        }
    }
}
