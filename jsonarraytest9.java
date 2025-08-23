package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest9 {

    @Test
    public void testStringPrimitiveAddition() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("Hello");
        jsonArray.add("Goodbye");
        jsonArray.add("Thank you");
        jsonArray.add((String) null);
        jsonArray.add("Yes");
        assertThat(jsonArray.toString()).isEqualTo("[\"Hello\",\"Goodbye\",\"Thank you\",null,\"Yes\"]");
    }
}
