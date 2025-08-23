package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest12 {

    @Test
    public void testBooleanPrimitiveAddition() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(true);
        jsonArray.add(true);
        jsonArray.add(false);
        jsonArray.add(false);
        jsonArray.add((Boolean) null);
        jsonArray.add(true);
        assertThat(jsonArray.toString()).isEqualTo("[true,true,false,false,null,true]");
    }
}
