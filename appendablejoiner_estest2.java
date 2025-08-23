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

public class AppendableJoiner_ESTestTest2 extends AppendableJoiner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        AppendableJoiner.Builder<Object> appendableJoiner_Builder0 = AppendableJoiner.builder();
        StringBuilder stringBuilder0 = new StringBuilder();
        AppendableJoiner.Builder<Object> appendableJoiner_Builder1 = appendableJoiner_Builder0.setSuffix(stringBuilder0);
        AppendableJoiner<Object> appendableJoiner0 = appendableJoiner_Builder1.get();
        ArrayDeque<Object> arrayDeque0 = new ArrayDeque<Object>(1910);
        StringBuilder stringBuilder1 = appendableJoiner0.join(stringBuilder0, (Iterable<Object>) arrayDeque0);
        assertEquals("", stringBuilder1.toString());
    }
}
