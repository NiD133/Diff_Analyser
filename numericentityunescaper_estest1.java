package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumericEntityUnescaper_ESTestTest1 extends NumericEntityUnescaper_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        NumericEntityUnescaper.OPTION[] numericEntityUnescaper_OPTIONArray0 = new NumericEntityUnescaper.OPTION[0];
        StringWriter stringWriter0 = new StringWriter();
        NumericEntityUnescaper numericEntityUnescaper0 = new NumericEntityUnescaper(numericEntityUnescaper_OPTIONArray0);
        char[] charArray0 = new char[8];
        charArray0[1] = '&';
        charArray0[2] = '#';
        charArray0[3] = 'y';
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        numericEntityUnescaper0.translate((CharSequence) charBuffer0, (Writer) stringWriter0);
        assertEquals("\u0000&#y\u0000\u0000\u0000\u0000", stringWriter0.toString());
    }
}
