package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.io.PipedWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.nio.charset.Charset;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import org.apache.commons.lang3.function.FailableBiConsumer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class AppendableJoiner_ESTestTest4 extends AppendableJoiner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        StringBuffer stringBuffer0 = new StringBuffer();
        StringBuilder stringBuilder0 = new StringBuilder(stringBuffer0);
        StringBuilder[] stringBuilderArray0 = new StringBuilder[1];
        // Undeclared exception!
        try {
            AppendableJoiner.joinA(stringBuilder0, (CharSequence) stringBuffer0, (CharSequence) stringBuilderArray0[0], (CharSequence) stringBuilder0, (FailableBiConsumer<Appendable, Object, IOException>) null, (Object[]) stringBuilderArray0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.lang3.AppendableJoiner", e);
        }
    }
}
