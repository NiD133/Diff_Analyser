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
import org.apache.commons.lang3.AppendableJoiner;
import org.apache.commons.lang3.function.FailableBiConsumer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class AppendableJoinerTest extends AppendableJoiner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testJoiningEmptyStringBuilders() throws Throwable {
        StringBuilder mainBuilder = new StringBuilder();
        StringBuffer buffer = new StringBuffer(mainBuilder);
        FailableBiConsumer<Appendable, Locale.Category, IOException> noOpConsumer = FailableBiConsumer.nop();
        EnumSet<Locale.Category> localeCategories = EnumSet.allOf(Locale.Category.class);

        StringBuilder resultBuilder = AppendableJoiner.joinI(mainBuilder, mainBuilder, buffer, buffer, noOpConsumer, localeCategories);
        assertEquals("", resultBuilder.toString());
    }

    @Test(timeout = 4000)
    public void testJoiningWithEmptyCollection() throws Throwable {
        AppendableJoiner.Builder<Object> joinerBuilder = AppendableJoiner.builder();
        StringBuilder suffixBuilder = new StringBuilder();
        joinerBuilder.setSuffix(suffixBuilder);
        AppendableJoiner<Object> joiner = joinerBuilder.get();
        ArrayDeque<Object> emptyDeque = new ArrayDeque<>(1910);

        StringBuilder resultBuilder = joiner.join(suffixBuilder, emptyDeque);
        assertEquals("", resultBuilder.toString());
    }

    @Test(timeout = 4000)
    public void testJoiningWithDelimiter() throws Throwable {
        AppendableJoiner.Builder<StringBuilder> joinerBuilder = AppendableJoiner.builder();
        StringBuilder mainBuilder = new StringBuilder();
        StringBuilder[] builderArray = new StringBuilder[4];
        builderArray[2] = mainBuilder;
        StringBuffer buffer = new StringBuffer(mainBuilder);

        joinerBuilder.setDelimiter(buffer);
        AppendableJoiner<StringBuilder> joiner = joinerBuilder.get();
        joiner.join(builderArray[2], builderArray);

        assertEquals("nullnullnullnullnull", mainBuilder.toString());
    }

    @Test(timeout = 4000)
    public void testJoinWithNullPointerException() throws Throwable {
        StringBuilder mainBuilder = new StringBuilder();
        StringBuffer buffer = new StringBuffer(mainBuilder);
        StringBuilder[] builderArray = new StringBuilder[1];

        try {
            AppendableJoiner.joinA(mainBuilder, buffer, builderArray[0], mainBuilder, null, builderArray);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.lang3.AppendableJoiner", e);
        }
    }

    @Test(timeout = 4000)
    public void testJoinWithNullPointerExceptionInJoinSB() throws Throwable {
        StringBuilder mainBuilder = new StringBuilder();
        FailableBiConsumer<Appendable, StringBuilder, IOException> noOpConsumer = FailableBiConsumer.nop();
        StringBuilder[] builderArray = new StringBuilder[0];

        try {
            AppendableJoiner.joinSB(null, mainBuilder, null, mainBuilder, noOpConsumer, builderArray);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.lang3.AppendableJoiner", e);
        }
    }

    @Test(timeout = 4000)
    public void testJoinWithIndexOutOfBoundsException() throws Throwable {
        StringBuilder mainBuilder = new StringBuilder();
        FailableBiConsumer<Appendable, StringBuilder, IOException> noOpConsumer = FailableBiConsumer.nop();
        char[] charArray = new char[2];
        CharBuffer charBuffer = CharBuffer.wrap(charArray);
        Charset charset = Charset.defaultCharset();
        CharBuffer wrappedBuffer = CharBuffer.wrap(charBuffer);
        charset.encode(charBuffer);
        StringBuilder[] builderArray = new StringBuilder[4];

        try {
            AppendableJoiner.joinSB(mainBuilder, wrappedBuffer, wrappedBuffer, "{1C", noOpConsumer, builderArray);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testJoinWithNullPointerExceptionInJoinI() throws Throwable {
        FailableBiConsumer<Appendable, StringBuilder, IOException> noOpConsumer = FailableBiConsumer.nop();
        ArrayDeque<StringBuilder> emptyDeque = new ArrayDeque<>();

        try {
            AppendableJoiner.joinI(null, null, null, null, noOpConsumer, emptyDeque);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.lang3.AppendableJoiner", e);
        }
    }

    @Test(timeout = 4000)
    public void testJoinWithReadOnlyBufferException() throws Throwable {
        AppendableJoiner.Builder<StringBuilder> joinerBuilder = AppendableJoiner.builder();
        AppendableJoiner<StringBuilder> joiner = joinerBuilder.get();
        StringBuffer buffer = new StringBuffer();
        CharBuffer charBuffer = CharBuffer.wrap(buffer, 0, 0);
        StringBuilder[] builderArray = new StringBuilder[5];

        try {
            joiner.joinA(charBuffer, builderArray);
            fail("Expecting exception: ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
            verifyException("java.nio.CharBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testJoinWithNullPointerExceptionInJoinA() throws Throwable {
        AppendableJoiner.Builder<StringBuilder> joinerBuilder = AppendableJoiner.builder();
        AppendableJoiner<StringBuilder> joiner = joinerBuilder.get();
        StringBuilder[] builderArray = new StringBuilder[0];

        try {
            joiner.joinA(null, builderArray);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.lang3.AppendableJoiner", e);
        }
    }

    @Test(timeout = 4000)
    public void testJoinWithIOException() throws Throwable {
        AppendableJoiner.Builder<StringBuilder> joinerBuilder = AppendableJoiner.builder();
        AppendableJoiner<StringBuilder> joiner = joinerBuilder.get();
        PipedWriter pipedWriter = new PipedWriter();

        try {
            joiner.joinA(pipedWriter, (StringBuilder[]) null);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedWriter", e);
        }
    }

    @Test(timeout = 4000)
    public void testJoinWithReadOnlyBufferExceptionInJoinA() throws Throwable {
        StringBuilder mainBuilder = new StringBuilder(2);
        AppendableJoiner.Builder<Appendable> joinerBuilder = AppendableJoiner.builder();
        AppendableJoiner<Appendable> joiner = joinerBuilder.get();
        CharBuffer charBuffer = CharBuffer.wrap(mainBuilder);
        ArrayDeque<StringBuilder> emptyDeque = new ArrayDeque<>();
        LinkedHashSet<Appendable> emptySet = new LinkedHashSet<>(emptyDeque);

        try {
            joiner.joinA(charBuffer, emptySet);
            fail("Expecting exception: ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
            verifyException("java.nio.CharBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testJoinWithNullPointerExceptionInJoinAWithIterable() throws Throwable {
        AppendableJoiner.Builder<StringBuilder> joinerBuilder = new AppendableJoiner.Builder<>();
        AppendableJoiner<StringBuilder> joiner = joinerBuilder.get();
        ArrayDeque<StringBuilder> emptyDeque = new ArrayDeque<>();

        try {
            joiner.joinA(null, emptyDeque);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.lang3.AppendableJoiner", e);
        }
    }

    @Test(timeout = 4000)
    public void testJoinWithReadOnlyBufferExceptionInJoinAWithAppendable() throws Throwable {
        StringBuffer buffer = new StringBuffer("7.6@B+");
        StringBuilder mainBuilder = new StringBuilder();
        CharBuffer charBuffer = CharBuffer.wrap(mainBuilder);
        FailableBiConsumer<Appendable, Appendable, IOException> noOpConsumer = FailableBiConsumer.nop();

        try {
            AppendableJoiner.joinA(charBuffer, buffer, charBuffer, charBuffer, noOpConsumer, (Appendable[]) null);
            fail("Expecting exception: ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
            verifyException("java.nio.CharBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testJoinWithNullPointerExceptionInJoin() throws Throwable {
        AppendableJoiner.Builder<StringBuilder> joinerBuilder = AppendableJoiner.builder();
        AppendableJoiner<StringBuilder> joiner = joinerBuilder.get();

        try {
            joiner.join(null, (StringBuilder[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.lang3.AppendableJoiner", e);
        }
    }

    @Test(timeout = 4000)
    public void testJoinWithNoOpConsumer() throws Throwable {
        StringBuilder mainBuilder = new StringBuilder();
        FailableBiConsumer<Appendable, StringBuilder, IOException> noOpConsumer = FailableBiConsumer.nop();
        StringBuilder[] builderArray = new StringBuilder[6];

        StringBuilder resultBuilder = AppendableJoiner.joinA(mainBuilder, mainBuilder, mainBuilder, mainBuilder, noOpConsumer, builderArray);
        assertSame(resultBuilder, mainBuilder);
    }

    @Test(timeout = 4000)
    public void testBuilderWithElementAppender() throws Throwable {
        AppendableJoiner.Builder<SQLNonTransientConnectionException> joinerBuilder = new AppendableJoiner.Builder<>();
        FailableBiConsumer<Appendable, SQLNonTransientConnectionException, IOException> noOpConsumer = FailableBiConsumer.nop();
        joinerBuilder.setElementAppender(noOpConsumer);

        AppendableJoiner<SQLNonTransientConnectionException> joiner = joinerBuilder.get();
        assertNotNull(joiner);
    }

    @Test(timeout = 4000)
    public void testJoinWithNullIterable() throws Throwable {
        AppendableJoiner.Builder<StringBuilder> joinerBuilder = new AppendableJoiner.Builder<>();
        StringBuilder mainBuilder = new StringBuilder();
        AppendableJoiner<StringBuilder> joiner = joinerBuilder.get();

        StringBuilder resultBuilder = joiner.join(mainBuilder, (Iterable<StringBuilder>) null);
        assertEquals("", resultBuilder.toString());
    }

    @Test(timeout = 4000)
    public void testJoinSBWithNoOpConsumer() throws Throwable {
        StringBuffer buffer = new StringBuffer();
        StringBuilder mainBuilder = new StringBuilder(buffer);
        FailableBiConsumer<Appendable, StringBuilder, IOException> noOpConsumer = FailableBiConsumer.nop();
        StringBuilder[] builderArray = new StringBuilder[0];

        StringBuilder resultBuilder = AppendableJoiner.joinSB(mainBuilder, buffer, mainBuilder, mainBuilder, noOpConsumer, builderArray);
        assertSame(resultBuilder, mainBuilder);
    }

    @Test(timeout = 4000)
    public void testJoinAWithEmptySet() throws Throwable {
        AppendableJoiner.Builder<SQLNonTransientConnectionException> joinerBuilder = new AppendableJoiner.Builder<>();
        StringBuffer buffer = new StringBuffer(1078);
        StringBuilder mainBuilder = new StringBuilder(buffer);
        AppendableJoiner<SQLNonTransientConnectionException> joiner = joinerBuilder.get();
        LinkedHashSet<SQLNonTransientConnectionException> emptySet = new LinkedHashSet<>();

        StringBuilder resultBuilder = joiner.joinA(mainBuilder, emptySet);
        assertEquals("", resultBuilder.toString());
    }

    @Test(timeout = 4000)
    public void testBuilderWithPrefix() throws Throwable {
        AppendableJoiner.Builder<StringBuilder> joinerBuilder = AppendableJoiner.builder();
        StringBuffer buffer = new StringBuffer(1078);

        AppendableJoiner.Builder<StringBuilder> resultBuilder = joinerBuilder.setPrefix(buffer);
        assertSame(resultBuilder, joinerBuilder);
    }

    @Test(timeout = 4000)
    public void testJoinWithNullPointerExceptionInJoinWithIterable() throws Throwable {
        AppendableJoiner.Builder<StringBuilder> joinerBuilder = AppendableJoiner.builder();
        AppendableJoiner<StringBuilder> joiner = joinerBuilder.get();
        ArrayList<StringBuilder> emptyList = new ArrayList<>();

        try {
            joiner.join(null, emptyList);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.lang3.AppendableJoiner", e);
        }
    }

    @Test(timeout = 4000)
    public void testJoinAWithEmptyIterable() throws Throwable {
        AppendableJoiner.Builder<SQLNonTransientConnectionException> joinerBuilder = new AppendableJoiner.Builder<>();
        StringBuffer buffer = new StringBuffer(1078);
        StringBuilder mainBuilder = new StringBuilder(buffer);
        AppendableJoiner<SQLNonTransientConnectionException> joiner = joinerBuilder.get();
        LinkedHashSet<SQLNonTransientConnectionException> emptySet = new LinkedHashSet<>();

        StringBuilder resultBuilder = joiner.joinA(mainBuilder, emptySet);
        assertEquals("", resultBuilder.toString());
    }

    @Test(timeout = 4000)
    public void testJoinAWithBufferOverflowException() throws Throwable {
        AppendableJoiner.Builder<StringBuilder> joinerBuilder = new AppendableJoiner.Builder<>();
        CharBuffer charBuffer = CharBuffer.allocate(0);
        AppendableJoiner<StringBuilder> joiner = joinerBuilder.get();
        StringBuilder[] builderArray = new StringBuilder[6];

        try {
            joiner.joinA(charBuffer, builderArray);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            verifyException("java.nio.CharBuffer", e);
        }
    }
}