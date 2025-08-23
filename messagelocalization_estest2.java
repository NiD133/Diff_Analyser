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

public class MessageLocalization_ESTestTest2 extends MessageLocalization_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        StringReader stringReader0 = new StringReader("rL:MuU>kC(vmJ+!e!(>");
        stringReader0.close();
        try {
            MessageLocalization.setMessages(stringReader0);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Stream closed
            //
            verifyException("java.io.StringReader", e);
        }
    }
}
