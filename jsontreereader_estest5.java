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

public class JsonTreeReader_ESTestTest5 extends JsonTreeReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test004() throws Throwable {
        Long long0 = new Long((-1977L));
        JsonPrimitive jsonPrimitive0 = new JsonPrimitive(long0);
        JsonTreeReader jsonTreeReader0 = new JsonTreeReader(jsonPrimitive0);
        int int0 = jsonTreeReader0.nextInt();
        assertEquals((-1977), int0);
    }
}
