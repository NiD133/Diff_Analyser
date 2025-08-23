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

public class JsonTreeReader_ESTestTest23 extends JsonTreeReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test022() throws Throwable {
        JsonArray jsonArray0 = new JsonArray();
        Boolean boolean0 = Boolean.FALSE;
        jsonArray0.add(boolean0);
        JsonTreeReader jsonTreeReader0 = new JsonTreeReader(jsonArray0);
        jsonTreeReader0.beginArray();
        jsonArray0.addAll(jsonArray0);
        // Undeclared exception!
        try {
            jsonTreeReader0.nextJsonElement();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.ArrayList$Itr", e);
        }
    }
}