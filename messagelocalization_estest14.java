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

public class MessageLocalization_ESTestTest14 extends MessageLocalization_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        StringReader stringReader0 = new StringReader("W=EJ%T_\"");
        stringReader0.read();
        MessageLocalization.setMessages(stringReader0);
        String string0 = MessageLocalization.getMessage("", false);
        assertEquals("EJ%T_\"", string0);
    }
}
