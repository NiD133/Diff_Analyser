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
    public void test12() throws Throwable {
        boolean boolean0 = MessageLocalization.setLanguage("en", "");
        assertTrue(boolean0);
        String string0 = MessageLocalization.getComposedMessage("", (-31));
        assertEquals("No message found for ", string0);
    }
}
