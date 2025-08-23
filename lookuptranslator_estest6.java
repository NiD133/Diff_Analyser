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

public class LookupTranslator_ESTestTest6 extends LookupTranslator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        CharSequence[][] charSequenceArray0 = new CharSequence[0][9];
        LookupTranslator lookupTranslator0 = new LookupTranslator(charSequenceArray0);
        CharBuffer charBuffer0 = CharBuffer.allocate(1);
        StringWriter stringWriter0 = new StringWriter(1);
        // Undeclared exception!
        try {
            lookupTranslator0.translate((CharSequence) charBuffer0, 1, (Writer) stringWriter0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.nio.Buffer", e);
        }
    }
}
