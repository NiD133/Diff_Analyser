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

public class JsonTreeWriter_ESTestTest40 extends JsonTreeWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        JsonTreeWriter jsonTreeWriter0 = new JsonTreeWriter();
        JsonWriter jsonWriter0 = jsonTreeWriter0.beginObject();
        // Undeclared exception!
        try {
            jsonWriter0.value((-1.0F));
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.gson.internal.bind.JsonTreeWriter", e);
        }
    }
}
