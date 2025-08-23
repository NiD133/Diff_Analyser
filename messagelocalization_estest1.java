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

public class MessageLocalization_ESTestTest1 extends MessageLocalization_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        // Undeclared exception!
        try {
            MessageLocalization.setMessages((Reader) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.io.Reader", e);
        }
    }
}
