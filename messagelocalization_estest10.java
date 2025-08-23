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

public class MessageLocalization_ESTestTest10 extends MessageLocalization_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        // Undeclared exception!
        try {
            MessageLocalization.setLanguage((String) null, "#wrong.number.of.columns");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // The language cannot be null.
            //
            verifyException("com.itextpdf.text.error_messages.MessageLocalization", e);
        }
    }
}
