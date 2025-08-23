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

public class LookupTranslator_ESTestTest11 extends LookupTranslator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        LookupTranslator lookupTranslator0 = new LookupTranslator((CharSequence[][]) null);
        StringWriter stringWriter0 = new StringWriter(7);
        // Undeclared exception!
        try {
            lookupTranslator0.translate((CharSequence) null, 7, (Writer) stringWriter0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.lang3.text.translate.LookupTranslator", e);
        }
    }
}
