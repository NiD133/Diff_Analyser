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

public class AppendableJoiner_ESTestTest3 extends AppendableJoiner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        AppendableJoiner.Builder<StringBuilder> appendableJoiner_Builder0 = AppendableJoiner.builder();
        StringBuilder stringBuilder0 = new StringBuilder();
        StringBuilder[] stringBuilderArray0 = new StringBuilder[4];
        stringBuilderArray0[2] = stringBuilder0;
        StringBuffer stringBuffer0 = new StringBuffer(stringBuilder0);
        AppendableJoiner.Builder<StringBuilder> appendableJoiner_Builder1 = appendableJoiner_Builder0.setDelimiter(stringBuffer0);
        AppendableJoiner<StringBuilder> appendableJoiner0 = appendableJoiner_Builder1.get();
        appendableJoiner0.join(stringBuilderArray0[2], stringBuilderArray0);
        assertEquals("nullnullnullnullnull", stringBuilder0.toString());
    }
}
