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

public class JsonTreeReader_ESTestTest88 extends JsonTreeReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test087() throws Throwable {
        Boolean boolean0 = Boolean.FALSE;
        JsonPrimitive jsonPrimitive0 = new JsonPrimitive(boolean0);
        JsonTreeReader jsonTreeReader0 = new JsonTreeReader(jsonPrimitive0);
        boolean boolean1 = jsonTreeReader0.nextBoolean();
        assertFalse(boolean1);
    }
}
