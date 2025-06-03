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
    public void test10() throws Throwable {
        String string0 = MessageLocalization.getMessage("writelength.can.only.be.called.in.a.contructed.pdfstream.inputstream.pdfwriter");
        assertEquals("writeLength() can only be called in a contructed PdfStream(InputStream,PdfWriter).", string0);
    }
}
