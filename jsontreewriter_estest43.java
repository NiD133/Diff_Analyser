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

public class JsonTreeWriter_ESTestTest43 extends JsonTreeWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        JsonTreeWriter jsonTreeWriter0 = new JsonTreeWriter();
        // Undeclared exception!
        try {
            jsonTreeWriter0.name((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // name == null
            //
            verifyException("java.util.Objects", e);
        }
    }
}
