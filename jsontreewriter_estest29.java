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

public class JsonTreeWriter_ESTestTest29 extends JsonTreeWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        JsonTreeWriter jsonTreeWriter0 = new JsonTreeWriter();
        jsonTreeWriter0.beginArray();
        jsonTreeWriter0.setHtmlSafe(true);
        JsonWriter jsonWriter0 = jsonTreeWriter0.endArray();
        assertSame(jsonTreeWriter0, jsonWriter0);
    }
}
