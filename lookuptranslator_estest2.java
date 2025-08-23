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

public class LookupTranslator_ESTestTest2 extends LookupTranslator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
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
        LookupTranslator lookupTranslator0 = null;
        try {
            lookupTranslator0 = new LookupTranslator(charSequenceArray1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.lang3.text.translate.LookupTranslator", e);
        }
    }
}
