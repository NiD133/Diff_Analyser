package org.apache.commons.lang3;

import org.apache.commons.lang3.exception.UncheckedException;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Readable unit tests for AppendableJoiner.
 *
 * These tests focus on:
 * - Happy-path joining for arrays and Iterables.
 * - Prefix, suffix, and delimiter behavior.
 * - Handling of null elements.
 * - Custom element appender behavior including exception handling.
 * - Basic defensive checks (null targets, no-op on null sources).
 */
public class AppendableJoinerTest {

    private static AppendableJoiner<String> joiner(final String prefix, final String suffix, final String delimiter) {
        return AppendableJoiner.<String>builder()
                .setPrefix(prefix)
                .setSuffix(suffix)
                .setDelimiter(delimiter)
                .get();
    }

    @Test
    public void join_array_basicFormatting() {
        // Given a joiner with prefix, suffix, and delimiter
        final AppendableJoiner<String> j = joiner("[", "]", ", ");

        // When joining into a StringBuilder
        final StringBuilder sb = new StringBuilder();
        final StringBuilder returned = j.join(sb, "A", "B", "C");

        // Then we get the formatted list and the same instance back
        assertEquals("[A, B, C]", sb.toString());
        assertSame(sb, returned);
    }

    @Test
    public void join_iterable_basicFormatting() {
        final AppendableJoiner<String> j = joiner("{", "}", " | ");

        final StringBuilder sb = new StringBuilder();
        final StringBuilder returned = j.join(sb, Arrays.asList("alpha", "beta"));

        assertEquals("{alpha | beta}", sb.toString());
        assertSame(sb, returned);
    }

    @Test
    public void join_array_withNullElements_usesStringValueOf() {
        // String.valueOf(null) -> "null"
        final AppendableJoiner<String> j = joiner("<", ">", "|");

        final StringBuilder sb = new StringBuilder();
        j.join(sb, "A", null, "C");

        assertEquals("<A|null|C>", sb.toString());
    }

    @Test
    public void join_doesNotOverwriteExistingContent() {
        // Demonstrates appending to existing content
        final AppendableJoiner<String> j = joiner("[", "]", ", ");

        final StringBuilder sb = new StringBuilder("1");
        j.join(sb, "A", "B");
        sb.append("2");
        j.join(sb, "C");

        assertEquals("1[A, B]2[C]", sb.toString());
    }

    @Test
    public void join_iterableNull_isNoOpAndReturnsTarget() {
        final AppendableJoiner<String> j = joiner("[", "]", ", ");

        final StringBuilder sb = new StringBuilder("x");
        final StringBuilder returned = j.join(sb, (Iterable<String>) null);

        assertEquals("x", sb.toString());
        assertSame(sb, returned);
    }

    @Test(expected = NullPointerException.class)
    public void join_nullTargetStringBuilder_throwsNPE() {
        final AppendableJoiner<String> j = joiner("[", "]", ", ");
        j.join((StringBuilder) null, "A");
    }

    @Test
    public void stringBuilderVariant_wrapsIOExceptionInUncheckedException() {
        // Custom appender that throws a checked IOException
        final AppendableJoiner<String> j = AppendableJoiner.<String>builder()
                .setElementAppender((a, e) -> { throw new IOException("boom"); })
                .get();

        try {
            j.join(new StringBuilder(), "x");
            fail("Expected UncheckedException");
        } catch (final UncheckedException ex) {
            assertTrue(ex.getCause() instanceof IOException);
            assertEquals("boom", ex.getCause().getMessage());
        }
    }

    @Test
    public void appendableVariant_propagatesIOException() {
        // The Appendable variant declares throws IOException, so it should not wrap.
        final AppendableJoiner<String> j = AppendableJoiner.<String>builder()
                .setElementAppender((a, e) -> { throw new IOException("io-error"); })
                .get();

        try {
            j.joinA(new StringBuilder(), "x");
            fail("Expected IOException");
        } catch (final IOException ex) {
            assertEquals("io-error", ex.getMessage());
        }
    }

    @Test
    public void builder_acceptsNullPrefixSuffixDelimiter_treatedAsEmpty() {
        // Passing null for prefix/suffix/delimiter should be treated as empty strings.
        final AppendableJoiner<String> j = AppendableJoiner.<String>builder()
                .setPrefix(null)
                .setSuffix(null)
                .setDelimiter(null)
                .get();

        final StringBuilder sb = new StringBuilder();
        j.join(sb, "A", "B");

        assertEquals("AB", sb.toString());
    }
}