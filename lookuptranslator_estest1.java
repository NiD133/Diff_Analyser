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

public class LookupTranslator_ESTestTest1 extends LookupTranslator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        CharSequence[] charSequenceArray0 = new CharSequence[3];
        char[] charArray0 = new char[2];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        charSequenceArray0[0] = (CharSequence) charBuffer0;
        charSequenceArray0[1] = (CharSequence) "FFFFF15A";
        CharSequence[][] charSequenceArray1 = new CharSequence[8][3];
        charSequenceArray1[0] = charSequenceArray0;
        CharSequence[] charSequenceArray2 = new CharSequence[5];
        charSequenceArray2[0] = (CharSequence) "FFFFF15A";
        charSequenceArray2[1] = (CharSequence) "FFFFF15A";
        charSequenceArray1[1] = charSequenceArray2;
        charSequenceArray1[2] = charSequenceArray0;
        charSequenceArray1[3] = charSequenceArray0;
        charSequenceArray1[4] = charSequenceArray0;
        charSequenceArray1[5] = charSequenceArray0;
        charSequenceArray1[6] = charSequenceArray0;
        charSequenceArray1[7] = charSequenceArray0;
        LookupTranslator lookupTranslator0 = new LookupTranslator(charSequenceArray1);
        String string0 = lookupTranslator0.translate((CharSequence) "FFFFFFFF");
        assertEquals("FFFFFFFF", string0);
    }
}
