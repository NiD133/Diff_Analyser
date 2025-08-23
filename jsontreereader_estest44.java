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

public class JsonTreeReader_ESTestTest44 extends JsonTreeReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test043() throws Throwable {
        JsonNull jsonNull0 = JsonNull.INSTANCE;
        JsonTreeReader jsonTreeReader0 = new JsonTreeReader(jsonNull0);
        JsonToken jsonToken0 = jsonTreeReader0.peek();
        assertEquals(JsonToken.NULL, jsonToken0);
    }
}
