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

public class JsonParser_ESTestTest9 extends JsonParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        JsonParser jsonParser0 = new JsonParser();
        StringReader stringReader0 = new StringReader("{}/GH6t)T:8sOB3 #");
        JsonReader jsonReader0 = new JsonReader(stringReader0);
        JsonElement jsonElement0 = jsonParser0.parse(jsonReader0);
        assertTrue(jsonElement0.isJsonObject());
    }
}
