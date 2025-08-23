package com.itextpdf.text.error_messages;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MessageLocalization_ESTestTest6 extends MessageLocalization_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        boolean boolean0 = MessageLocalization.setLanguage("en", "No message found for #N.value.1.is.not.supported");
        assertTrue(boolean0);
        String string0 = MessageLocalization.getMessage("No message found for #N.value.1.is.not.supported", false);
        assertEquals("No message found for No message found for #N.value.1.is.not.supported", string0);
        assertNotNull(string0);
    }
}
