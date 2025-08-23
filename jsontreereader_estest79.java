package com.google.gson.internal.bind;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonTreeReader_ESTestTest79 extends JsonTreeReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test078() throws Throwable {
        JsonPrimitive jsonPrimitive0 = new JsonPrimitive("-Infinity");
        JsonTreeReader jsonTreeReader0 = new JsonTreeReader(jsonPrimitive0);
        try {
            jsonTreeReader0.nextDouble();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // JSON forbids NaN and infinities: -Infinity
            //
            verifyException("com.google.gson.internal.bind.JsonTreeReader", e);
        }
    }
}
