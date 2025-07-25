/**
 * Tests the functionality of the ArrayBasedUnicodeEscaper class.
 *
 * @author David Beaumont
 */
@GwtCompatible
@NullMarked
public class ArrayBasedUnicodeEscaperTest extends TestCase {

    private static final ImmutableMap<Character, String> NO_REPLACEMENTS = ImmutableMap.of();
    private static final ImmutableMap<Character, String> SIMPLE_REPLACEMENTS =
            ImmutableMap.of(
                    '\n', "<newline>",
                    '\t', "<tab>",
                    '&', "<and>");
    private static final char[] NO_CHARS = new char[0];

    /**
     * Tests that characters with replacements are correctly escaped.
     */
    public void testReplacements() throws IOException {
        // Create an escaper with simple replacements
        UnicodeEscaper escaper =
                new ArrayBasedUnicodeEscaper(
                        SIMPLE_REPLACEMENTS, Character.MIN_VALUE, Character.MAX_CODE_POINT, null) {
                    @Override
                    protected char[] escapeUnsafe(int c) {
                        return NO_CHARS;
                    }
                };

        // Verify basic escaper functionality
        EscaperAsserts.assertBasic(escaper);

        // Verify that characters with replacements are correctly escaped
        String input = "\tFish & Chips\n";
        String expectedOutput = "<tab>Fish <and> Chips<newline>";
        assertThat(escaper.escape(input)).isEqualTo(expectedOutput);

        // Verify that characters without replacements are left unescaped
        String safeChars = "\0\u0100\uD800\uDC00\uFFFF";
        assertThat(escaper.escape(safeChars)).isEqualTo(safeChars);

        // Verify that Unicode escapers behave correctly with badly formed input
        String badUnicode = "\uDC00\uD800";
        assertThrows(IllegalArgumentException.class, () -> escaper.escape(badUnicode));
    }

    /**
     * Tests that characters in the safe range are left unescaped.
     */
    public void testSafeRange() throws IOException {
        // Create an escaper that wraps unsafe characters in curly braces
        UnicodeEscaper wrappingEscaper =
                new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, 'A', 'Z', null) {
                    @Override
                    protected char[] escapeUnsafe(int c) {
                        return ("{" + (char) c + "}").toCharArray();
                    }
                };

        // Verify basic escaper functionality
        EscaperAsserts.assertBasic(wrappingEscaper);

        // Verify that characters in the safe range are left unescaped
        String input = "[FOO@BAR]";
        String expectedOutput = "{[}FOO{@}BAR{]}";
        assertThat(wrappingEscaper.escape(input)).isEqualTo(expectedOutput);
    }

    /**
     * Tests that characters outside the safe range are deleted.
     */
    public void testDeleteUnsafeChars() throws IOException {
        // Create an escaper that deletes characters outside the safe range
        UnicodeEscaper deletingEscaper =
                new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, ' ', '~', null) {
                    @Override
                    protected char[] escapeUnsafe(int c) {
                        return NO_CHARS;
                    }
                };

        // Verify basic escaper functionality
        EscaperAsserts.assertBasic(deletingEscaper);

        // Verify that characters outside the safe range are deleted
        String input = "\tEverything\0 outside the\uD800\uDC00 " + "printable ASCII \uFFFFrange is \u007Fdeleted.\n";
        String expectedOutput = "Everything outside the printable ASCII range is deleted.";
        assertThat(deletingEscaper.escape(input)).isEqualTo(expectedOutput);
    }

    /**
     * Tests that replacement priority is correctly handled.
     */
    public void testReplacementPriority() throws IOException {
        // Create an escaper with simple replacements and a default replacement
        UnicodeEscaper replacingEscaper =
                new ArrayBasedUnicodeEscaper(SIMPLE_REPLACEMENTS, ' ', '~', null) {
                    private final char[] unknown = new char[] {'?'};

                    @Override
                    protected char[] escapeUnsafe(int c) {
                        return unknown;
                    }
                };

        // Verify basic escaper functionality
        EscaperAsserts.assertBasic(replacingEscaper);

        // Verify that replacement priority is correctly handled
        String input = "\tFish &\0 Chips\r\n";
        String expectedOutput = "<tab>Fish <and>? Chips?<newline>";
        assertThat(replacingEscaper.escape(input)).isEqualTo(expectedOutput);
    }

    /**
     * Tests that code points from surrogate pairs are correctly handled.
     */
    public void testCodePointsFromSurrogatePairs() throws IOException {
        // Create an escaper with a safe range that includes surrogate pairs
        UnicodeEscaper surrogateEscaper =
                new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, 0, 0x20000, null) {
                    private final char[] escaped = new char[] {'X'};

                    @Override
                    protected char[] escapeUnsafe(int c) {
                        return escaped;
                    }
                };

        // Verify basic escaper functionality
        EscaperAsserts.assertBasic(surrogateEscaper);

        // Verify that surrogate pairs within the safe range are left unescaped
        String safeInput = "\uD800\uDC00"; // 0x10000
        assertThat(surrogateEscaper.escape(safeInput)).isEqualTo(safeInput);

        // Verify that surrogate pairs outside the safe range are correctly escaped
        String unsafeInput = "\uDBFF\uDFFF"; // 0x10FFFF
        assertThat(surrogateEscaper.escape(unsafeInput)).isEqualTo("X");
    }
}