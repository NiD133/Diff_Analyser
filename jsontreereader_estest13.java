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

public class JsonTreeReader_ESTestTest13 extends JsonTreeReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test012() throws Throwable {
        JsonObject jsonObject0 = new JsonObject();
        jsonObject0.addProperty("z", "z");
        JsonTreeReader jsonTreeReader0 = new JsonTreeReader(jsonObject0);
        jsonTreeReader0.beginObject();
        jsonObject0.add("", (JsonElement) null);
        // Undeclared exception!
        try {
            jsonTreeReader0.promoteNameToValue();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.gson.internal.LinkedTreeMap$LinkedTreeMapIterator", e);
        }
    }
}
