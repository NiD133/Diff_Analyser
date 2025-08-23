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

public class MessageLocalization_ESTestTest3 extends MessageLocalization_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Object[] objectArray0 = new Object[4];
        objectArray0[0] = (Object) "Xs";
        String string0 = MessageLocalization.getComposedMessage("X72rI", objectArray0);
        assertEquals("No message found for X72rI", string0);
    }
}
