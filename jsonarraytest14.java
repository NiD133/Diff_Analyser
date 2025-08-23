package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest14 {

    @Test
    public void testMixedPrimitiveAddition() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('a');
        jsonArray.add("apple");
        jsonArray.add(12121);
        jsonArray.add((char) 111);
        jsonArray.add((Boolean) null);
        assertThat(jsonArray.get(jsonArray.size() - 1)).isEqualTo(JsonNull.INSTANCE);
        jsonArray.add((Character) null);
        assertThat(jsonArray.get(jsonArray.size() - 1)).isEqualTo(JsonNull.INSTANCE);
        jsonArray.add(12.232);
        jsonArray.add(BigInteger.valueOf(2323));
        assertThat(jsonArray.toString()).isEqualTo("[\"a\",\"apple\",12121,\"o\",null,null,12.232,2323]");
    }
}
