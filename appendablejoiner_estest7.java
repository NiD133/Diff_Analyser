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

public class AppendableJoiner_ESTestTest7 extends AppendableJoiner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        FailableBiConsumer<Appendable, StringBuilder, IOException> failableBiConsumer0 = FailableBiConsumer.nop();
        ArrayDeque<StringBuilder> arrayDeque0 = new ArrayDeque<StringBuilder>();
        // Undeclared exception!
        try {
            AppendableJoiner.joinI((StringBuilder) null, (CharSequence) null, (CharSequence) null, (CharSequence) null, failableBiConsumer0, (Iterable<StringBuilder>) arrayDeque0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.lang3.AppendableJoiner", e);
        }
    }
}
