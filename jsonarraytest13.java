package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest13 {

    @Test
    public void testCharPrimitiveAddition() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('a');
        jsonArray.add('e');
        jsonArray.add('i');
        jsonArray.add((char) 111);
        jsonArray.add((Character) null);
        jsonArray.add('u');
        jsonArray.add("and sometimes Y");
        assertThat(jsonArray.toString()).isEqualTo("[\"a\",\"e\",\"i\",\"o\",null,\"u\",\"and sometimes Y\"]");
    }
}
