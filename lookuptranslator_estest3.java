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

public class LookupTranslator_ESTestTest3 extends LookupTranslator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        StringWriter stringWriter0 = new StringWriter();
        LookupTranslator lookupTranslator0 = new LookupTranslator((CharSequence[][]) null);
        CharBuffer charBuffer0 = CharBuffer.wrap((CharSequence) "557");
        int int0 = lookupTranslator0.translate((CharSequence) charBuffer0, 0, (Writer) stringWriter0);
        assertEquals(0, int0);
    }
}