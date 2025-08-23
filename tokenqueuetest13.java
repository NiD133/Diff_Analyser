package org.jsoup.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.api.Named;

/**
 * Tests for CSS identifier parsing and escaping in TokenQueue.
 * Includes tests from the Web Platform Tests (WPT) to ensure spec compliance.
 */
public class CssIdentifierTest {
    private static final String REPLACEMENT_CHAR = "\uFFFD";

    @Nested
    @DisplayName("TokenQueue.escapeCssIdentifier()")
    class EscapeCssIdentifierTests {

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("escapeTestCases")
        void correctlyEscapesCssIdentifiers(Named<Arguments> namedArgs) {
            Arguments args = namedArgs.getPayload();
            String expected = (String) args.get()[0];
            String input = (String) args.get()[1];
            assertEquals(expected, TokenQueue.escapeCssIdentifier(input));
        }

        static Stream<Named<Arguments>> escapeTestCases() {
            // Combines WPT tests with additional jsoup-specific tests.
            return Stream.of(
                webPlatformTests(),
                additionalTests()
            ).flatMap(s -> s);
        }

        // Source: https://github.com/web-platform-tests/wpt/blob/328fa1c67bf5dfa6f24571d4c41dd10224b6d247/css/cssom/escape.html
        private static Stream<Named<Arguments>> webPlatformTests() {
            return Stream.of(
                // Basic cases
                Named.of("Empty string", arguments("", "")),
                Named.of("Handles simple identifiers", arguments("abcdefghijklmnopqrstuvwxyz", "abcdefghijklmnopqrstuvwxyz")),
                Named.of("Handles uppercase identifiers", arguments("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ")),
                Named.of("Handles mixed case identifiers with numbers", arguments("a0123456789b", "a0123456789b")),

                // Null bytes are replaced
                Named.of("Null byte -> replacement char", arguments(REPLACEMENT_CHAR, "\0")),
                Named.of("Leading char + null byte", arguments("a" + REPLACEMENT_CHAR, "a\0")),
                Named.of("Trailing char + null byte", arguments(REPLACEMENT_CHAR + "b", "\0b")),
                Named.of("Char sandwiching null byte", arguments("a" + REPLACEMENT_CHAR + "b", "a\0b")),

                // Replacement characters are passed through
                Named.of("Replacement char", arguments(REPLACEMENT_CHAR, REPLACEMENT_CHAR)),
                Named.of("Leading char + replacement char", arguments("a" + REPLACEMENT_CHAR, "a" + REPLACEMENT_CHAR)),

                // Numeric prefixes are escaped
                Named.of("Numeric prefix '0a'", arguments("\\30 a", "0a")),
                Named.of("Numeric prefix '9a'", arguments("\\39 a", "9a")),

                // Numbers after a letter are not escaped
                Named.of("Letter followed by numbers", arguments("a0b", "a0b")),
                Named.of("Letter followed by numbers", arguments("a9b", "a9b")),

                // Dash followed by numeric prefix is escaped
                Named.of("Dash + numeric prefix '-0a'", arguments("-\\30 a", "-0a")),
                Named.of("Dash + numeric prefix '-9a'", arguments("-\\39 a", "-9a")),

                // Double-dash prefix is not escaped
                Named.of("Double-dash prefix", arguments("--a", "--a")),

                // Control characters are escaped
                Named.of("Control characters U+0001 to U+001F", arguments("\\1 \\2 \\1e \\1f ", "\u0001\u0002\u001E\u001F")),
                Named.of("DEL char and non-ASCII control chars", arguments("\\7f \u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B\u008C\u008D\u008E\u008F\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F", "\u007F\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B\u008C\u008D\u008E\u008F\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F")),

                // High-codepoint characters are preserved
                Named.of("Non-ASCII chars are preserved", arguments("\u0080\u002D\u005F\u00A9", "\u0080\u002D\u005F\u00A9")),
                Named.of("Non-ASCII chars are preserved", arguments("\u00A0\u00A1\u00A2", "\u00A0\u00A1\u00A2")),
                Named.of("High codepoint char is preserved", arguments("hello\u1234world", "hello\u1234world")),
                Named.of("Astral symbol (surrogate pair) is preserved", arguments("\uD834\uDF06", "\uD834\uDF06")),
                Named.of("Lone trailing surrogate is preserved", arguments("\uDF06", "\uDF06")),
                Named.of("Lone leading surrogate is preserved", arguments("\uD834", "\uD834")),

                // Special characters are escaped
                Named.of("Backslashes are escaped", arguments("hello\\\\world", "hello\\world")),
                Named.of("Single dash is escaped", arguments("\\-", "-")),
                Named.of("Special ASCII chars are escaped", arguments("\\ \\!xy", "\u0020\u0021\u0078\u0079"))
            );
        }

        private static Stream<Named<Arguments>> additionalTests() {
            return Stream.of(
                Named.of("Handles multiple special characters", arguments("one\\#two\\.three\\/four\\\\five", "one#two.three/four\\five")),
                Named.of("Identifier starting with a dash is preserved", arguments("-a", "-a")),
                Named.of("Double-dash is preserved", arguments("--", "--"))
            );
        }
    }

    @Nested
    @DisplayName("TokenQueue.consumeCssIdentifier()")
    class ConsumeCssIdentifierTests {

        private void assertIdentifierConsumesTo(String expected, String input) {
            TokenQueue tq = new TokenQueue(input);
            String consumed = tq.consumeCssIdentifier();
            assertEquals(expected, consumed);
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("consumeTestCases")
        void correctlyConsumesCssIdentifiers(Named<Arguments> namedArgs) {
            Arguments args = namedArgs.getPayload();
            String expected = (String) args.get()[0];
            String input = (String) args.get()[1];
            assertIdentifierConsumesTo(expected, input);
        }

        static Stream<Named<Arguments>> consumeTestCases() {
            return Stream.of(
                webPlatformTests(),
                chromiumTests(),
                additionalJsoupTests()
            ).flatMap(s -> s);
        }

        // Source: https://github.com/web-platform-tests/wpt/blob/36036fb5212a3fc15fc5750cecb1923ba4071668/dom/nodes/ParentNode-querySelector-escapes.html
        private static Stream<Named<Arguments>> webPlatformTests() {
            return Stream.of(
                Named.of("Hex escape followed by space", arguments("0nextIsWhiteSpace", "\\30 nextIsWhiteSpace")),
                Named.of("Hex escape followed by non-hex", arguments("0nextIsNotHexLetters", "\\30nextIsNotHexLetters")),
                Named.of("6-digit hex escape", arguments("0connectHexMoreThan6Hex", "\\000030connectHexMoreThan6Hex")),
                Named.of("6-digit hex escape with space", arguments("0spaceMoreThan6Hex", "\\000030 spaceMoreThan6Hex")),
                Named.of("Zero escape sequence", arguments("zero" + REPLACEMENT_CHAR, "zero\\0")),
                Named.of("6-digit zero escape sequence", arguments("zero" + REPLACEMENT_CHAR, "zero\\000000")),
                Named.of("Escaped leading surrogate", arguments(REPLACEMENT_CHAR + "surrogateFirst", "\\d83d surrogateFirst")),
                Named.of("Escaped trailing surrogate", arguments("surrogateSecond" + REPLACEMENT_CHAR, "surrogateSecond\\dd11")),
                Named.of("Escaped surrogate pair", arguments("surrogatePair" + REPLACEMENT_CHAR + REPLACEMENT_CHAR, "surrogatePair\\d83d\\dd11")),
                Named.of("Out of range escape sequence", arguments("outOfRange" + REPLACEMENT_CHAR, "outOfRange\\110000")),
                Named.of("Simple escape", arguments(".comma", "\\.comma")),
                Named.of("Escaped dash", arguments("-minus", "\\-minus")),
                Named.of("Escaped letter", arguments("g", "\\g")),
                Named.of("BMP char escape with space", arguments("aBMPRegular", "\\61 BMPRegular")),
                Named.of("Non-BMP char escape with space", arguments("\uD83D\uDD11nonBMP", "\\1f511 nonBMP")),
                Named.of("Multiple hex escapes", arguments("00continueEscapes", "\\30\\30 continueEscapes")),
                Named.of("Multiple hex escapes with space", arguments("00continueEscapes", "\\30 \\30 continueEscapes"))
            );
        }

        // Source: Chromium CSS parser tests - https://goo.gl/3Cxdov
        private static Stream<Named<Arguments>> chromiumTests() {
            return Stream.of(
                Named.of("Simple ident", arguments("simple-ident", "simple-ident")),
                Named.of("Ident with numbers", arguments("testing123", "testing123")),
                Named.of("Ident with underscore", arguments("_underscore", "_underscore")),
                Named.of("Ident starting with dash", arguments("-text", "-text")),
                Named.of("Ident with double-dash", arguments("--abc", "--abc")),
                Named.of("Double-dash only", arguments("--", "--")),
                Named.of("Double-dash with numbers", arguments("--11", "--11")),
                Named.of("Triple-dash", "---", "---"),
                Named.of("Escaped hex in middle", arguments("hello", "hel\\6Co")),
                Named.of("Escaped special char with space", arguments("&B", "\\26 B")),
                Named.of("Escaped hex with space in middle", arguments("hello", "hel\\6C o")),
                Named.of("Escaped char with CR LF", arguments("spaces", "spac\\65\r\ns")),
                Named.of("Escaped chars with tab and form-feed", arguments("spaces", "sp\\61\tc\\65\fs")),
                Named.of("Escaped non-ascii", arguments("test\uD799", "test\\D799")),
                Named.of("Escaped private use area char", arguments("\uE000", "\\E000")),
                Named.of("Escaped backslash", arguments("test", "te\\s\\t")),
                Named.of("Escaped space and tab", arguments("spaces in\tident", "spaces\\ in\\\tident")),
                Named.of("Escaped punctuation", arguments(".,:!", "\\.\\,\\:\\!"))
            );
        }

        private static Stream<Named<Arguments>> additionalJsoupTests() {
            return Stream.of(
                Named.of("Hex escape with CR LF", arguments("1st", "\\31\r\nst")),
                Named.of("Hex escape with CR terminator", arguments("1", "\\31\r")),
                Named.of("Hex escape with CR and trailing chars", arguments("1a", "\\31\ra")),
                Named.of("Padded hex escape (3-digit)", arguments("1", "\\031")),
                Named.of("Padded hex escape (4-digit)", arguments("1", "\\0031")),
                Named.of("Padded hex escape (5-digit)", arguments("1", "\\00031")),
                Named.of("Padded hex escape (6-digit)", arguments("1", "a\\\nb")),
                Named.of("Escaped newline consumes only one char", arguments("a", "a\\\nb")),
                Named.of("Null char becomes replacement char", arguments("ab" + REPLACEMENT_CHAR + "c", "ab\u0000c"))
            );
        }

        @Test
        @DisplayName("Consumes non-compliant identifiers for backwards compatibility")
        void consumesNonCompliantIdentifiersForBackwardsCompatibility() {
            // These are not valid CSS identifiers, but jsoup supports them for legacy reasons.
            assertIdentifierConsumesTo("1", "1");
            assertIdentifierConsumesTo("-", "-");
            assertIdentifierConsumesTo("-1", "-1");
        }
    }
}