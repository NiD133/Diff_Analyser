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

public class AppendableJoiner_ESTestTest18 extends AppendableJoiner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        StringBuffer stringBuffer0 = new StringBuffer();
        StringBuilder stringBuilder0 = new StringBuilder(stringBuffer0);
        FailableBiConsumer<Appendable, StringBuilder, IOException> failableBiConsumer0 = FailableBiConsumer.nop();
        StringBuilder[] stringBuilderArray0 = new StringBuilder[0];
        StringBuilder stringBuilder1 = AppendableJoiner.joinSB(stringBuilder0, (CharSequence) stringBuffer0, (CharSequence) stringBuilder0, (CharSequence) stringBuilder0, failableBiConsumer0, stringBuilderArray0);
        assertSame(stringBuilder1, stringBuilder0);
    }
}