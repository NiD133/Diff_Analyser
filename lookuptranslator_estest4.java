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

public class LookupTranslator_ESTestTest4 extends LookupTranslator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        CharSequence[][] charSequenceArray0 = new CharSequence[1][7];
        CharSequence[] charSequenceArray1 = new CharSequence[3];
        char[] charArray0 = new char[2];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        charSequenceArray1[0] = (CharSequence) charBuffer0;
        charSequenceArray1[1] = (CharSequence) "FFFFF15A";
        charSequenceArray1[2] = (CharSequence) charBuffer0;
        charSequenceArray0[0] = charSequenceArray1;
        LookupTranslator lookupTranslator0 = new LookupTranslator(charSequenceArray0);
        StringWriter stringWriter0 = new StringWriter(1);
        int int0 = lookupTranslator0.translate(charSequenceArray1[2], 0, (Writer) stringWriter0);
        assertEquals("FFFFF15A", stringWriter0.toString());
        assertEquals(2, int0);
    }
}
