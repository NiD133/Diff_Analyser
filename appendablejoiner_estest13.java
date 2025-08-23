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

public class AppendableJoiner_ESTestTest13 extends AppendableJoiner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        StringBuffer stringBuffer0 = new StringBuffer("7.6@B+");
        StringBuilder stringBuilder0 = new StringBuilder();
        CharBuffer charBuffer0 = CharBuffer.wrap((CharSequence) stringBuilder0);
        FailableBiConsumer<Appendable, Appendable, IOException> failableBiConsumer0 = FailableBiConsumer.nop();
        // Undeclared exception!
        try {
            AppendableJoiner.joinA((Appendable) charBuffer0, (CharSequence) stringBuffer0, (CharSequence) charBuffer0, (CharSequence) charBuffer0, failableBiConsumer0, (Appendable[]) null);
            fail("Expecting exception: ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.nio.CharBuffer", e);
        }
    }
}
