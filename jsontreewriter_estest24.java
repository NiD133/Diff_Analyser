package com.google.gson.internal.bind;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonTreeWriter_ESTestTest24 extends JsonTreeWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        JsonTreeWriter jsonTreeWriter0 = new JsonTreeWriter();
        JsonTreeWriter jsonTreeWriter1 = (JsonTreeWriter) jsonTreeWriter0.beginObject();
        jsonTreeWriter0.endObject();
        JsonObject jsonObject0 = (JsonObject) jsonTreeWriter1.get();
        assertFalse(jsonObject0.isJsonNull());
    }
}
