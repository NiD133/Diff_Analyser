package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PipedWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Understandable tests for {@link AppendableJoiner}.
 */
public class AppendableJoinerTest {

    private static final List<String> THREE_STRINGS_LIST = Arrays.asList("a", "b", "c");
    private static final String[] THREE_STRINGS_ARRAY = {"a", "b", "c"};
    private static final List<String> LIST_WITH_NULL = Arrays.asList("a", null, "c");
    private static final String[] ARRAY_WITH_NULL = {"a", null, "c"};

    // --- Basic Joining Tests ---

    @Test
    public void join_withIterable_shouldAppendElementsWithDelimiter() {
        AppendableJoiner<String> joiner = AppendableJoiner.<String>builder().setDelimiter(",").get();
        StringBuilder result = new StringBuilder();
        joiner.join(result, THREE_STRINGS_LIST);
        assertEquals("a,b,c", result.toString());
    }

    @Test
    public void join_withArray_shouldAppendElementsWithDelimiter() {
        AppendableJoiner<String> joiner = AppendableJoiner.<String>builder().setDelimiter(",").get();
        StringBuilder result = new StringBuilder();
        joiner.join(result, THREE_STRINGS_ARRAY);
        assertEquals("a,b,c", result.toString());
    }

    @Test
    public void join_withPrefixAndSuffix_shouldEncloseJoinedElements() {
        AppendableJoiner<String> joiner = AppendableJoiner.<String>builder()
                .setPrefix("[")
                .setSuffix("]")
                .setDelimiter(",")
                .get();
        StringBuilder result = new StringBuilder();
        joiner.join(result, THREE_STRINGS_LIST);
        assertEquals("[a,b,c]", result.toString());
    }

    @Test
    public void join_withSingleElement_shouldNotAppendDelimiter() {
        AppendableJoiner<String> joiner = AppendableJoiner.<String>builder()
                .setPrefix("[")
                .setSuffix("]")
                .setDelimiter(",")
                .get();
        StringBuilder result = new StringBuilder();
        joiner.join(result, Collections.singletonList("a"));
        assertEquals("[a]", result.toString());
    }

    // --- Null and Empty Element Tests ---

    @Test
    public void join_withEmptyIterable_shouldAppendOnlyPrefixAndSuffix() {
        AppendableJoiner<String> joiner = AppendableJoiner.<String>builder()
                .setPrefix("[")
                .setSuffix("]")
                .get();
        StringBuilder result = new StringBuilder();
        joiner.join(result, Collections.emptyList());
        assertEquals("[]", result.toString());
    }

    @Test
    public void join_withEmptyArray_shouldAppendOnlyPrefixAndSuffix() {
        AppendableJoiner<String> joiner = AppendableJoiner.<String>builder()
                .setPrefix("[")
                .setSuffix("]")
                .get();
        StringBuilder result = new StringBuilder();
        joiner.join(result, new String[]{});
        assertEquals("[]", result.toString());
    }

    @Test
    public void join_withNullIterable_shouldBeTreatedAsEmpty() {
        AppendableJoiner<String> joiner = AppendableJoiner.<String>builder()
                .setPrefix("[")
                .setSuffix("]")
                .get();
        StringBuilder result = new StringBuilder();
        joiner.join(result, (Iterable<String>) null);
        assertEquals("[]", result.toString());
    }

    @Test
    public void join_withNullArray_shouldBeTreatedAsEmpty() {
        AppendableJoiner<String> joiner = AppendableJoiner.<String>builder()
                .setPrefix("[")
                .setSuffix("]")
                .get();
        StringBuilder result = new StringBuilder();
        joiner.join(result, (String[]) null);
        assertEquals("[]", result.toString());
    }

    @Test
    public void join_withNullElements_shouldAppendTheStringNull() {
        AppendableJoiner<String> joiner = AppendableJoiner.<String>builder().setDelimiter(",").get();
        StringBuilder result = new StringBuilder();
        joiner.join(result, LIST_WITH_NULL);
        assertEquals("a,null,c", result.toString());
    }

    // --- Custom Appender Tests ---

    @Test
    public void join_withCustomElementAppender_shouldUseItToRenderElements() {
        // A custom appender that wraps each element in quotes.
        AppendableJoiner<String> joiner = AppendableJoiner.<String>builder()
                .setDelimiter(",")
                .setElementAppender((appendable, element) -> appendable.append('"').append(element).append('"'))
                .get();
        StringBuilder result = new StringBuilder();
        joiner.join(result, THREE_STRINGS_LIST);
        assertEquals("\"a\",\"b\",\"c\"", result.toString());
    }

    // --- Exception and Edge Case Tests ---

    @Test(expected = NullPointerException.class)
    public void join_toNullStringBuilder_shouldThrowNullPointerException() {
        AppendableJoiner<String> joiner = AppendableJoiner.<String>builder().get();
        joiner.join((StringBuilder) null, THREE_STRINGS_LIST);
    }

    @Test(expected = NullPointerException.class)
    public void joinA_toNullAppendable_shouldThrowNullPointerException() throws IOException {
        AppendableJoiner<String> joiner = AppendableJoiner.<String>builder().get();
        joiner.joinA(null, THREE_STRINGS_LIST);
    }

    @Test(expected = ReadOnlyBufferException.class)
    public void joinA_toReadOnlyBuffer_shouldThrowException() throws IOException {
        AppendableJoiner<String> joiner = AppendableJoiner.<String>builder().get();
        CharBuffer readOnlyBuffer = CharBuffer.wrap("test").asReadOnlyBuffer();
        joiner.joinA(readOnlyBuffer, THREE_STRINGS_LIST);
    }

    @Test(expected = BufferOverflowException.class)
    public void joinA_toFullBuffer_shouldThrowException() throws IOException {
        AppendableJoiner<String> joiner = AppendableJoiner.<String>builder().get();
        // A buffer with only 1 char of capacity
        CharBuffer smallBuffer = CharBuffer.allocate(1);
        // Joining "a", "b", "c" will overflow it
        joiner.joinA(smallBuffer, THREE_STRINGS_LIST);
    }

    @Test(expected = IOException.class)
    public void joinA_toFailingAppendable_shouldPropagateIOException() throws IOException {
        AppendableJoiner<String> joiner = AppendableJoiner.<String>builder().get();
        // A PipedWriter throws IOException if not connected to a PipedReader
        PipedWriter failingWriter = new PipedWriter();
        joiner.joinA(failingWriter, THREE_STRINGS_LIST);
    }

    // --- Builder Tests ---

    @Test
    public void builder_withDefaultSettings_shouldConcatenateElements() {
        // A joiner with default settings (empty prefix, suffix, delimiter)
        AppendableJoiner<String> joiner = AppendableJoiner.<String>builder().get();
        StringBuilder result = new StringBuilder();
        joiner.join(result, THREE_STRINGS_LIST);
        // Should just concatenate the elements
        assertEquals("abc", result.toString());
    }

    @Test
    public void builder_shouldBeFluent() {
        // This test verifies that the builder methods are chainable.
        // A Consumer that accepts a Builder and does nothing, to confirm the return type.
        Consumer<AppendableJoiner.Builder<Object>> fluentCheck = builder -> {};

        AppendableJoiner.Builder<Object> builder = AppendableJoiner.builder();
        fluentCheck.accept(builder.setPrefix("["));
        fluentCheck.accept(builder.setSuffix("]"));
        fluentCheck.accept(builder.setDelimiter(","));
        fluentCheck.accept(builder.setElementAppender((a, e) -> {}));
    }
}