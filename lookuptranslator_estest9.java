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

public class LookupTranslator_ESTestTest9 extends LookupTranslator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        CharSequence[][] charSequenceArray0 = new CharSequence[1][0];
        LookupTranslator lookupTranslator0 = null;
        try {
            lookupTranslator0 = new LookupTranslator(charSequenceArray0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 0
            //
            verifyException("org.apache.commons.lang3.text.translate.LookupTranslator", e);
        }
    }
}
