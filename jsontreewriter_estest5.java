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

public class JsonTreeWriter_ESTestTest5 extends JsonTreeWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        JsonTreeWriter jsonTreeWriter0 = new JsonTreeWriter();
        Strictness strictness0 = Strictness.LENIENT;
        jsonTreeWriter0.setStrictness(strictness0);
        JsonWriter jsonWriter0 = jsonTreeWriter0.value("Kv)DQr");
        assertFalse(jsonWriter0.isHtmlSafe());
    }
}
