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

public class MessageLocalization_ESTestTest11 extends MessageLocalization_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        boolean boolean0 = MessageLocalization.setLanguage("#N.value.1.is.not.supported", "Z+[q![/n\".7YLF#");
        assertFalse(boolean0);
    }
}
