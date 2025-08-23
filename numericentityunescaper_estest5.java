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

public class NumericEntityUnescaper_ESTestTest5 extends NumericEntityUnescaper_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        NumericEntityUnescaper.OPTION[] numericEntityUnescaper_OPTIONArray0 = new NumericEntityUnescaper.OPTION[0];
        NumericEntityUnescaper numericEntityUnescaper0 = new NumericEntityUnescaper(numericEntityUnescaper_OPTIONArray0);
        char[] charArray0 = new char[7];
        charArray0[3] = '&';
        charArray0[4] = '#';
        charArray0[5] = '3';
        charArray0[6] = ';';
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        StringWriter stringWriter0 = new StringWriter();
        int int0 = numericEntityUnescaper0.translate((CharSequence) charBuffer0, 3, (Writer) stringWriter0);
        assertEquals("\u0003", stringWriter0.toString());
        assertEquals(4, int0);
    }
}
