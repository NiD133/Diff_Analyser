package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest11 {

    @Test
    public void testDoublePrimitiveAddition() {
        JsonArray jsonArray = new JsonArray();
        double x = 1.0;
        jsonArray.add(x);
        x = 2.13232;
        jsonArray.add(x);
        x = 0.121;
        jsonArray.add(x);
        jsonArray.add((Double) null);
        x = -0.00234;
        jsonArray.add(x);
        jsonArray.add((Double) null);
        assertThat(jsonArray.toString()).isEqualTo("[1.0,2.13232,0.121,null,-0.00234,null]");
    }
}
