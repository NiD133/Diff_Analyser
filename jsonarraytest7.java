package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

public class JsonArrayTestTest7 {

    @Test
    public void testFailedGetArrayValues() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(JsonParser.parseString("{" + "\"key1\":\"value1\"," + "\"key2\":\"value2\"," + "\"key3\":\"value3\"," + "\"key4\":\"value4\"" + "}"));
        Exception e = assertThrows(UnsupportedOperationException.class, () -> jsonArray.getAsBoolean());
        assertThat(e).hasMessageThat().isEqualTo("JsonObject");
        e = assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.get(-1));
        assertThat(e).hasMessageThat().isEqualTo("Index -1 out of bounds for length 1");
        e = assertThrows(UnsupportedOperationException.class, () -> jsonArray.getAsString());
        assertThat(e).hasMessageThat().isEqualTo("JsonObject");
        jsonArray.remove(0);
        jsonArray.add("hello");
        e = assertThrows(NumberFormatException.class, () -> jsonArray.getAsDouble());
        assertThat(e).hasMessageThat().isEqualTo("For input string: \"hello\"");
        e = assertThrows(NumberFormatException.class, () -> jsonArray.getAsInt());
        assertThat(e).hasMessageThat().isEqualTo("For input string: \"hello\"");
        e = assertThrows(IllegalStateException.class, () -> jsonArray.get(0).getAsJsonArray());
        assertThat(e).hasMessageThat().isEqualTo("Not a JSON Array: \"hello\"");
        e = assertThrows(IllegalStateException.class, () -> jsonArray.getAsJsonObject());
        assertThat(e).hasMessageThat().isEqualTo("Not a JSON Object: [\"hello\"]");
        e = assertThrows(NumberFormatException.class, () -> jsonArray.getAsLong());
        assertThat(e).hasMessageThat().isEqualTo("For input string: \"hello\"");
    }
}
