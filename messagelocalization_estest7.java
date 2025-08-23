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

public class MessageLocalization_ESTestTest7 extends MessageLocalization_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        String string0 = MessageLocalization.getMessage("#you.can.t.add.a.1.to.a.section");
        assertNotNull(string0);
        assertEquals("No message found for #you.can.t.add.a.1.to.a.section", string0);
    }
}
