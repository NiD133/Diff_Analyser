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

public class JsonParser_ESTestTest10 extends JsonParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        JsonParser jsonParser0 = new JsonParser();
        StringReader stringReader0 = new StringReader("|h|[8&ZGQ1Fbg]Xp");
        JsonReader jsonReader0 = new JsonReader(stringReader0);
        jsonParser0.parse(jsonReader0);
        JsonElement jsonElement0 = jsonParser0.parse(jsonReader0);
        assertFalse(jsonElement0.isJsonObject());
    }
}
