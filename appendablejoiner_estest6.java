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

public class AppendableJoiner_ESTestTest6 extends AppendableJoiner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder();
        FailableBiConsumer<Appendable, StringBuilder, IOException> failableBiConsumer0 = FailableBiConsumer.nop();
        char[] charArray0 = new char[2];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        Charset charset0 = Charset.defaultCharset();
        CharBuffer charBuffer1 = CharBuffer.wrap((CharSequence) charBuffer0);
        charset0.encode(charBuffer0);
        StringBuilder[] stringBuilderArray0 = new StringBuilder[4];
        // Undeclared exception!
        try {
            AppendableJoiner.joinSB(stringBuilder0, (CharSequence) charBuffer1, (CharSequence) charBuffer1, (CharSequence) "{1C", failableBiConsumer0, stringBuilderArray0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.nio.Buffer", e);
        }
    }
}
