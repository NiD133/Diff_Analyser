package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest17 {

    @Test
    public void testSameAddition() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('a');
        jsonArray.add('a');
        jsonArray.add(true);
        jsonArray.add(true);
        jsonArray.add(1212);
        jsonArray.add(1212);
        jsonArray.add(34.34);
        jsonArray.add(34.34);
        jsonArray.add((Boolean) null);
        jsonArray.add((Boolean) null);
        assertThat(jsonArray.toString()).isEqualTo("[\"a\",\"a\",true,true,1212,1212,34.34,34.34,null,null]");
    }
}
