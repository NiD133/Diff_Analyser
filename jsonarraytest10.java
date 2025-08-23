package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest10 {

    @Test
    public void testIntegerPrimitiveAddition() {
        JsonArray jsonArray = new JsonArray();
        int x = 1;
        jsonArray.add(x);
        x = 2;
        jsonArray.add(x);
        x = -3;
        jsonArray.add(x);
        jsonArray.add((Integer) null);
        x = 4;
        jsonArray.add(x);
        x = 0;
        jsonArray.add(x);
        assertThat(jsonArray.toString()).isEqualTo("[1,2,-3,null,4,0]");
    }
}
