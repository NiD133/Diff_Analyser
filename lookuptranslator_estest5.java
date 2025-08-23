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

public class LookupTranslator_ESTestTest5 extends LookupTranslator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        CharSequence[][] charSequenceArray0 = new CharSequence[0][3];
        LookupTranslator lookupTranslator0 = new LookupTranslator(charSequenceArray0);
        StringWriter stringWriter0 = new StringWriter();
        StringBuffer stringBuffer0 = stringWriter0.getBuffer();
        // Undeclared exception!
        try {
            lookupTranslator0.translate((CharSequence) stringBuffer0, 2955, (Writer) stringWriter0);
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
        }
    }
}
