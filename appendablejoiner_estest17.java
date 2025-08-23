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

public class AppendableJoiner_ESTestTest17 extends AppendableJoiner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        AppendableJoiner.Builder<StringBuilder> appendableJoiner_Builder0 = new AppendableJoiner.Builder<StringBuilder>();
        StringBuilder stringBuilder0 = new StringBuilder();
        AppendableJoiner<StringBuilder> appendableJoiner0 = appendableJoiner_Builder0.get();
        StringBuilder stringBuilder1 = appendableJoiner0.join(stringBuilder0, (Iterable<StringBuilder>) null);
        assertEquals("", stringBuilder1.toString());
    }
}
