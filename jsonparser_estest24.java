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

public class JsonParser_ESTestTest24 extends JsonParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        StringReader stringReader0 = new StringReader("[GoyG.G]");
        JsonReader jsonReader0 = new JsonReader(stringReader0);
        JsonElement jsonElement0 = JsonParser.parseReader(jsonReader0);
        assertFalse(jsonElement0.isJsonPrimitive());
    }
}
