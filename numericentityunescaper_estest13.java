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

public class NumericEntityUnescaper_ESTestTest13 extends NumericEntityUnescaper_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        NumericEntityUnescaper.OPTION[] numericEntityUnescaper_OPTIONArray0 = new NumericEntityUnescaper.OPTION[0];
        StringWriter stringWriter0 = new StringWriter();
        NumericEntityUnescaper numericEntityUnescaper0 = new NumericEntityUnescaper(numericEntityUnescaper_OPTIONArray0);
        char[] charArray0 = new char[6];
        charArray0[0] = '&';
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        numericEntityUnescaper0.translate((CharSequence) charBuffer0, (Writer) stringWriter0);
        assertTrue(charBuffer0.hasArray());
    }
}
