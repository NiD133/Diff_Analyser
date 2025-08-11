package org.jsoup.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StringUtil Tests")
class StringUtilTest {

    @Test
    void testConstructorIsPrivate() throws Exception {
        Constructor<StringUtil> constructor = StringUtil.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        assertNotNull(constructor.newInstance());
    }

    @Nested
    @DisplayName("Character Type Tests")
    class CharacterTypeTests {
        @ParameterizedTest
        @ValueSource(chars = {'0', '9', 'a', 'f', 'A', 'F'})
        void isHexDigit_withValidHexCharacters_returnsTrue(char c) {
            assertTrue(StringUtil.isHexDigit(c));
        }

        @ParameterizedTest
        @ValueSource(chars = {'g', 'G', ' ', 'z', 'Z', '!'})
        void isHexDigit_withInvalidCharacters_returnsFalse(char c) {
            assertFalse(StringUtil.isHexDigit(c));
        }

        @ParameterizedTest
        @ValueSource(chars = {'a', 'z', 'A', 'Z'})
        void isAsciiLetter_withAsciiLetters_returnsTrue(char c) {
            assertTrue(StringUtil.isAsciiLetter(c));
        }

        @ParameterizedTest
        @ValueSource(chars = {'0', ' ', '!', '[', '`'})
        void isAsciiLetter_withNonAsciiLetters_returnsFalse(char c) {
            assertFalse(StringUtil.isAsciiLetter(c));
        }

        @Test
        void isNumeric_withNumericString_returnsTrue() {
            assertTrue(StringUtil.isNumeric("12345"));
        }

        @Test
        void isNumeric_withNonNumericString_returnsFalse() {
            assertFalse(StringUtil.isNumeric("123a"));
        }

        @Test
        void isNumeric_withEmptyString_returnsFalse() {
            assertFalse(StringUtil.isNumeric(""));
        }

        @Test
        void isNumeric_withNullString_returnsFalse() {
            assertFalse(StringUtil.isNumeric(null));
        }

        @Test
        void isAscii_withAsciiString_returnsTrue() {
            assertTrue(StringUtil.isAscii("Hello World 123!@#"));
        }

        @Test
        void isAscii_withNonAsciiString_returnsFalse() {
            assertFalse(StringUtil.isAscii("Hello World é"));
        }
    }

    @Nested
    @DisplayName("Whitespace Handling Tests")
    class WhitespaceTests {
        @Test
        void isBlank_withNullString_returnsTrue() {
            assertTrue(StringUtil.isBlank(null));
        }

        @Test
        void isBlank_withEmptyString_returnsTrue() {
            assertTrue(StringUtil.isBlank(""));
        }

        @Test
        void isBlank_withWhitespaceString_returnsTrue() {
            assertTrue(StringUtil.isBlank("   \t\r\n "));
        }

        @Test
        void isBlank_withContentString_returnsFalse() {
            assertFalse(StringUtil.isBlank(" a "));
        }

        @ParameterizedTest
        @ValueSource(ints = {' ', '\t', '\n', '\f', '\r'})
        void isWhitespace_withStandardWhitespace_returnsTrue(int c) {
            assertTrue(StringUtil.isWhitespace(c));
        }

        @Test
        void isWhitespace_withNonBreakingSpace_returnsFalse() {
            assertFalse(StringUtil.isWhitespace(160)); // No-break space
        }

        @ParameterizedTest
        @ValueSource(ints = {' ', '\t', '\n', '\f', '\r', 160})
        void isActuallyWhitespace_withAllWhitespaceTypes_returnsTrue(int c) {
            assertTrue(StringUtil.isActuallyWhitespace(c));
        }

        @Test
        void isActuallyWhitespace_withNonWhitespace_returnsFalse() {
            assertFalse(StringUtil.isActuallyWhitespace('a'));
        }

        @ParameterizedTest
        @ValueSource(ints = {173, 8203, 8204, 8205})
        void isInvisibleChar_withInvisibleChars_returnsTrue(int c) {
            assertTrue(StringUtil.isInvisibleChar(c));
        }

        @Test
        void isInvisibleChar_withVisibleChar_returnsFalse() {
            assertFalse(StringUtil.isInvisibleChar('a'));
        }

        @Test
        void normaliseWhitespace_collapsesSpacesAndTrims() {
            String input = "  Hello\tthere \n world  ";
            String expected = "Hello there world";
            assertEquals(expected, StringUtil.normaliseWhitespace(input));
        }

        @Test
        void normaliseWhitespace_withEmptyString_returnsEmptyString() {
            assertEquals("", StringUtil.normaliseWhitespace(""));
        }

        @Test
        void appendNormalisedWhitespace_appendsAndCollapses() {
            StringBuilder sb = new StringBuilder("Prefix:");
            StringUtil.appendNormalisedWhitespace(sb, "  foo\t bar  ", false);
            assertEquals("Prefix: foo bar ", sb.toString());
        }

        @Test
        void appendNormalisedWhitespace_withStripLeading_removesLeadingSpace() {
            StringBuilder sb = new StringBuilder("Prefix:");
            StringUtil.appendNormalisedWhitespace(sb, "  foo\t bar  ", true);
            assertEquals("Prefix:foo bar ", sb.toString());
        }

        @Test
        void startsWithNewline_withNewline_returnsTrue() {
            assertTrue(StringUtil.startsWithNewline("\nHello"));
        }

        @Test
        void startsWithNewline_withoutNewline_returnsFalse() {
            assertFalse(StringUtil.startsWithNewline("Hello"));
        }

        @Test
        void startsWithNewline_withNullOrEmpty_returnsFalse() {
            assertFalse(StringUtil.startsWithNewline(null));
            assertFalse(StringUtil.startsWithNewline(""));
        }
    }

    @Nested
    @DisplayName("Join Tests")
    class JoinTests {
        @Test
        void join_collection_withMultipleElements_returnsJoinedString() {
            List<String> strings = Arrays.asList("one", "two", "three");
            assertEquals("one, two, three", StringUtil.join(strings, ", "));
        }

        @Test
        void join_collection_withSingleElement_returnsElementItself() {
            List<String> strings = Collections.singletonList("one");
            assertEquals("one", StringUtil.join(strings, ", "));
        }


        @Test
        void join_collection_withEmptyCollection_returnsEmptyString() {
            assertEquals("", StringUtil.join(Collections.emptyList(), ", "));
        }

        @Test
        void join_array_withMultipleElements_returnsJoinedString() {
            String[] strings = {"one", "two", "three"};
            assertEquals("one|two|three", StringUtil.join(strings, "|"));
        }

        @Test
        void join_iterator_withMultipleElements_returnsJoinedString() {
            List<String> strings = Arrays.asList("one", "two", "three");
            assertEquals("one-two-three", StringUtil.join(strings.iterator(), "-"));
        }

        @Test
        void join_onRecursiveCollection_throwsStackOverflowError() {
            // A collection that contains itself will cause a stack overflow.
            List<Object> list = new LinkedList<>();
            list.add(list);
            assertThrows(StackOverflowError.class, () -> StringUtil.join(list, ", "));
        }

        @Test
        void join_onIterator_withConcurrentModification_throwsException() {
            List<String> list = new LinkedList<>(Arrays.asList("one", "two"));
            Iterator<String> iterator = list.iterator();
            iterator.next();
            list.add("three"); // Modify list while iterating
            assertThrows(ConcurrentModificationException.class, () -> StringUtil.join(iterator, ", "));
        }

        @Test
        void joining_collector_worksLikeStreamJoining() {
            List<String> strings = Arrays.asList("one", "two", "three");
            String expected = strings.stream().collect(Collectors.joining(", "));
            String actual = strings.stream().collect(StringUtil.joining(", "));
            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("Padding Tests")
    class PaddingTests {
        @Test
        void padding_withZeroWidth_returnsEmptyString() {
            assertEquals("", StringUtil.padding(0));
        }

        @Test
        void padding_withPositiveWidth_returnsStringOfSpaces() {
            assertEquals("     ", StringUtil.padding(5));
        }

        @Test
        void padding_withNegativeWidth_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () -> StringUtil.padding(-1));
        }

        @Test
        void padding_withWidthAndMax_respectsMaxWidth() {
            assertEquals("   ", StringUtil.padding(5, 3));
            assertEquals("", StringUtil.padding(5, 0));
            assertEquals("", StringUtil.padding(5, -1)); // Negative max width is treated as 0
        }
    }

    @Nested
    @DisplayName("URL Resolution Tests")
    class UrlResolutionTests {
        private final URL baseUrl;

        UrlResolutionTests() throws MalformedURLException {
            baseUrl = new URL("http://example.com/path/to/file");
        }

        @Test
        void resolve_withRelativeUrl_returnsCorrectAbsoluteUrl() throws MalformedURLException {
            URL resolved = StringUtil.resolve(baseUrl, "../other.html");
            assertEquals("http://example.com/path/other.html", resolved.toExternalForm());
        }

        @Test
        void resolve_withAbsoluteUrl_returnsGivenUrl() throws MalformedURLException {
            URL resolved = StringUtil.resolve(baseUrl, "http://jsoup.org");
            assertEquals("http://jsoup.org", resolved.toExternalForm());
        }

        @Test
        void resolve_withFragment_returnsUrlWithFragment() throws MalformedURLException {
            URL resolved = StringUtil.resolve(baseUrl, "#hash");
            assertEquals("http://example.com/path/to/file#hash", resolved.toExternalForm());
        }

        @Test
        void resolve_stringVersion_withRelativeUrl_returnsCorrectAbsoluteUrl() {
            String resolved = StringUtil.resolve("http://example.com/path/", "page.html");
            assertEquals("http://example.com/path/page.html", resolved);
        }

        @Test
        void resolve_stringVersion_withMalformedBase_returnsEmptyString() {
            // The original StringUtil.resolve swallows MalformedURLException and returns an empty string.
            String resolved = StringUtil.resolve("not a url", "page.html");
            assertEquals("", resolved);
        }
    }

    @Nested
    @DisplayName("Search Tests")
    class SearchTests {
        private final String[] haystack = {"apple", "banana", "cherry"};

        @Test
        void in_whenValueInArray_returnsTrue() {
            assertTrue(StringUtil.in("banana", haystack));
        }

        @Test
        void in_whenValueNotInArray_returnsFalse() {
            assertFalse(StringUtil.in("durian", haystack));
        }

        @Test
        void in_withNullInHaystack_throwsNullPointerException() {
            String[] haystackWithNull = {"apple", null, "cherry"};
            assertThrows(NullPointerException.class, () -> StringUtil.in("banana", haystackWithNull));
        }

        @Test
        void inSorted_whenValueInArray_returnsTrue() {
            assertTrue(StringUtil.inSorted("banana", haystack));
        }

        @Test
        void inSorted_whenValueNotInArray_returnsFalse() {
            assertFalse(StringUtil.inSorted("durian", haystack));
        }
    }

    @Nested
    @DisplayName("StringBuilder Pool Tests")
    class StringBuilderPoolTests {
        @Test
        void borrowAndReleaseBuilder_cycleWorks() {
            StringBuilder sb = StringUtil.borrowBuilder();
            assertNotNull(sb);
            assertEquals(0, sb.length());
            StringUtil.releaseBuilder(sb);
        }

        @Test
        void releaseBuilder_clearsBuilderAndReturnsContent() {
            StringBuilder sb = StringUtil.borrowBuilder();
            sb.append("test");
            String content = StringUtil.releaseBuilder(sb);
            assertEquals("test", content);
            assertEquals(0, sb.length());
        }

        @Test
        void releaseBuilderVoid_clearsBuilder() {
            StringBuilder sb = StringUtil.borrowBuilder();
            sb.append("test");
            StringUtil.releaseBuilderVoid(sb);
            assertEquals(0, sb.length());
        }

        @Test
        void releaseBuilder_withLargeBuilder_truncatesBuilder() {
            StringBuilder sb = StringUtil.borrowBuilder();
            // Max size is 8K. Fill with more than that.
            String longString = Stream.generate(() -> "a").limit(10000).collect(Collectors.joining());
            sb.append(longString);
            assertTrue(sb.capacity() > 8 * 1024);

            StringUtil.releaseBuilder(sb);
            // After release, the builder is cleared and its capacity may be reset.
            assertTrue(sb.capacity() <= 8 * 1024);
        }

        @Test
        void releaseBuilder_withNull_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () -> StringUtil.releaseBuilder(null));
        }
    }

    @Nested
    @DisplayName("StringJoiner Tests")
    class StringJoinerTests {
        @Test
        void stringJoiner_buildsStringCorrectly() {
            StringUtil.StringJoiner joiner = new StringUtil.StringJoiner(", ");
            joiner.add("one");
            joiner.add("two");
            assertEquals("one, two", joiner.complete());
        }

        @Test
        void stringJoiner_withAppend_appendsToLastItem() {
            StringUtil.StringJoiner joiner = new StringUtil.StringJoiner(", ");
            joiner.add("one");
            joiner.append("A");
            joiner.add("two");
            joiner.append("B");
            assertEquals("oneA, twoB", joiner.complete());
        }

        @Test
        void stringJoiner_completeOnEmpty_returnsEmptyString() {
            StringUtil.StringJoiner joiner = new StringUtil.StringJoiner(", ");
            assertEquals("", joiner.complete());
        }
    }
}