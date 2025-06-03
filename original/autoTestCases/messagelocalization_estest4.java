package org.example;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        Object[] objectArray0 = new Object[2];
        objectArray0[0] = (Object) "No message found for null";
        String string0 = MessageLocalization.getComposedMessage("No message found for null", objectArray0);
        assertEquals("No message found for No message found for null", string0);
    }
}
