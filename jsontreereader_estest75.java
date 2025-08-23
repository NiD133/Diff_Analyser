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

public class JsonTreeReader_ESTestTest75 extends JsonTreeReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test074() throws Throwable {
        JsonArray jsonArray0 = new JsonArray();
        JsonTreeReader jsonTreeReader0 = new JsonTreeReader(jsonArray0);
        // Undeclared exception!
        try {
            jsonTreeReader0.nextLong();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Expected NUMBER but was BEGIN_ARRAY at path $
            //
            verifyException("com.google.gson.internal.bind.JsonTreeReader", e);
        }
    }
}
