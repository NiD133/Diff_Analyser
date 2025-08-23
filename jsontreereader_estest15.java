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

public class JsonTreeReader_ESTestTest15 extends JsonTreeReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test014() throws Throwable {
        JsonArray jsonArray0 = new JsonArray();
        JsonTreeReader jsonTreeReader0 = new JsonTreeReader(jsonArray0);
        jsonTreeReader0.beginArray();
        Float float0 = new Float(1910.422785308);
        jsonArray0.add((Number) float0);
        // Undeclared exception!
        try {
            jsonTreeReader0.peek();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.ArrayList$Itr", e);
        }
    }
}
