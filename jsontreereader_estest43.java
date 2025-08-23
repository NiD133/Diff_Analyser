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

public class JsonTreeReader_ESTestTest43 extends JsonTreeReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test042() throws Throwable {
        JsonTreeReader jsonTreeReader0 = new JsonTreeReader((JsonElement) null);
        jsonTreeReader0.close();
        // Undeclared exception!
        try {
            jsonTreeReader0.peek();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // JsonReader is closed
            //
            verifyException("com.google.gson.internal.bind.JsonTreeReader", e);
        }
    }
}
