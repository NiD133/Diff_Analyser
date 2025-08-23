package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonParser_ESTestTest29 extends JsonParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        StringReader stringReader0 = new StringReader("");
        JsonParser jsonParser0 = new JsonParser();
        JsonReader jsonReader0 = new JsonReader(stringReader0);
        JsonElement jsonElement0 = jsonParser0.parse(jsonReader0);
        assertFalse(jsonElement0.isJsonArray());
    }
}
