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

public class MessageLocalization_ESTestTest4 extends MessageLocalization_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        String string0 = MessageLocalization.getMessage("you.can.only.add.cells.to.rows.no.objects.of.type.1", true);
        assertEquals("You can only add cells to rows, no objects of type {1}", string0);
    }
}
