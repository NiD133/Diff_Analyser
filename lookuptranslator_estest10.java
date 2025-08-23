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

public class LookupTranslator_ESTestTest10 extends LookupTranslator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        CharSequence[] charSequenceArray0 = new CharSequence[3];
        char[] charArray0 = new char[2];
        charArray0[1] = '1';
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        charSequenceArray0[0] = (CharSequence) charBuffer0;
        charSequenceArray0[1] = (CharSequence) "FFFFF15A";
        CharBuffer charBuffer1 = CharBuffer.wrap(charSequenceArray0[0]);
        CharBuffer charBuffer2 = CharBuffer.wrap(charSequenceArray0[1]);
        CharSequence[][] charSequenceArray1 = new CharSequence[4][7];
        charSequenceArray1[0] = charSequenceArray0;
        charSequenceArray1[1] = charSequenceArray0;
        charSequenceArray1[2] = charSequenceArray0;
        charBuffer1.get();
        CharSequence[] charSequenceArray2 = new CharSequence[10];
        charSequenceArray2[0] = (CharSequence) charBuffer1;
        charSequenceArray2[1] = (CharSequence) "FFFFF15A";
        charSequenceArray1[3] = charSequenceArray2;
        LookupTranslator lookupTranslator0 = new LookupTranslator(charSequenceArray1);
        String string0 = lookupTranslator0.translate((CharSequence) charBuffer2);
        assertEquals("FFFFFFFFFF15A5A", string0);
    }
}
