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

public class LookupTranslator_ESTestTest8 extends LookupTranslator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        CharSequence[][] charSequenceArray0 = new CharSequence[1][2];
        CharSequence[] charSequenceArray1 = new CharSequence[2];
        char[] charArray0 = new char[0];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        charSequenceArray1[0] = (CharSequence) charBuffer0;
        charSequenceArray1[1] = (CharSequence) charBuffer0;
        charSequenceArray0[0] = charSequenceArray1;
        LookupTranslator lookupTranslator0 = null;
        try {
            lookupTranslator0 = new LookupTranslator(charSequenceArray0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.nio.Buffer", e);
        }
    }
}
