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

public class LookupTranslator_ESTestTest7 extends LookupTranslator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        CharSequence[][] charSequenceArray0 = new CharSequence[1][9];
        CharSequence[] charSequenceArray1 = new CharSequence[2];
        StringWriter stringWriter0 = new StringWriter();
        StringBuffer stringBuffer0 = stringWriter0.getBuffer();
        charSequenceArray1[1] = (CharSequence) stringBuffer0;
        charSequenceArray0[0] = charSequenceArray1;
        charSequenceArray1[0] = (CharSequence) stringBuffer0;
        LookupTranslator lookupTranslator0 = null;
        try {
            lookupTranslator0 = new LookupTranslator(charSequenceArray0);
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
        }
    }
}
