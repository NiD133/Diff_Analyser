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

public class AppendableJoiner_ESTestTest11 extends AppendableJoiner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder(2);
        AppendableJoiner.Builder<Appendable> appendableJoiner_Builder0 = AppendableJoiner.builder();
        AppendableJoiner<Appendable> appendableJoiner0 = appendableJoiner_Builder0.get();
        CharBuffer charBuffer0 = CharBuffer.wrap((CharSequence) stringBuilder0);
        ArrayDeque<StringBuilder> arrayDeque0 = new ArrayDeque<StringBuilder>();
        LinkedHashSet<Appendable> linkedHashSet0 = new LinkedHashSet<Appendable>(arrayDeque0);
        // Undeclared exception!
        try {
            appendableJoiner0.joinA((Appendable) charBuffer0, (Iterable<Appendable>) linkedHashSet0);
            fail("Expecting exception: ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.nio.CharBuffer", e);
        }
    }
}
