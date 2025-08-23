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

public class AppendableJoiner_ESTestTest1 extends AppendableJoiner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        StringBuffer stringBuffer0 = new StringBuffer();
        StringBuilder stringBuilder0 = new StringBuilder(stringBuffer0);
        StringBuffer stringBuffer1 = new StringBuffer(stringBuilder0);
        FailableBiConsumer<Appendable, Locale.Category, IOException> failableBiConsumer0 = FailableBiConsumer.nop();
        Class<Locale.Category> class0 = Locale.Category.class;
        EnumSet<Locale.Category> enumSet0 = EnumSet.allOf(class0);
        StringBuilder stringBuilder1 = AppendableJoiner.joinI(stringBuilder0, (CharSequence) stringBuilder0, (CharSequence) stringBuffer1, (CharSequence) stringBuffer0, failableBiConsumer0, (Iterable<Locale.Category>) enumSet0);
        assertEquals("", stringBuilder1.toString());
    }
}
