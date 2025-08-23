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

public class NumericEntityUnescaper_ESTestTest9 extends NumericEntityUnescaper_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        NumericEntityUnescaper.OPTION[] numericEntityUnescaper_OPTIONArray0 = new NumericEntityUnescaper.OPTION[0];
        NumericEntityUnescaper numericEntityUnescaper0 = new NumericEntityUnescaper(numericEntityUnescaper_OPTIONArray0);
        char[] charArray0 = new char[6];
        charArray0[2] = '&';
        charArray0[3] = '#';
        charArray0[4] = 'x';
        charArray0[5] = ';';
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        String string0 = numericEntityUnescaper0.translate((CharSequence) charBuffer0);
        assertEquals("\u0000\u0000&#x;", string0);
    }
}
