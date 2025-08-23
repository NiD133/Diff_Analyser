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

public class AppendableJoiner_ESTestTest16 extends AppendableJoiner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        AppendableJoiner.Builder<SQLNonTransientConnectionException> appendableJoiner_Builder0 = new AppendableJoiner.Builder<SQLNonTransientConnectionException>();
        FailableBiConsumer<Appendable, SQLNonTransientConnectionException, IOException> failableBiConsumer0 = FailableBiConsumer.nop();
        AppendableJoiner.Builder<SQLNonTransientConnectionException> appendableJoiner_Builder1 = appendableJoiner_Builder0.setElementAppender(failableBiConsumer0);
        AppendableJoiner<SQLNonTransientConnectionException> appendableJoiner0 = appendableJoiner_Builder1.get();
        assertNotNull(appendableJoiner0);
    }
}
