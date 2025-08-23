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

public class AppendableJoiner_ESTestTest20 extends AppendableJoiner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        StringBuffer stringBuffer0 = new StringBuffer(1078);
        AppendableJoiner.Builder<StringBuilder> appendableJoiner_Builder0 = AppendableJoiner.builder();
        AppendableJoiner.Builder<StringBuilder> appendableJoiner_Builder1 = appendableJoiner_Builder0.setPrefix(stringBuffer0);
        assertSame(appendableJoiner_Builder1, appendableJoiner_Builder0);
    }
}