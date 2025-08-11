package org.apache.commons.cli;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Readable, behavior-oriented tests for PosixParser#flatten.
 *
 * Notes:
 * - These tests focus on the public flatten contract and observable behavior.
 * - We avoid testing internal/protected methods (like burstToken) directly.
 * - Each test name describes the scenario and expected outcome.
 */
public class PosixParserTest {

    /**
     * Small helper to make tests concise.
     */
    private static String[] flatten(Options opts, boolean stopAtNonOption, String... args) throws Exception {
        return new PosixParser().flatten(opts, args, stopAtNonOption);
    }

    private static Options opts(Option... options) {
        Options o = new Options();
        for (Option opt : options) {
            o.addOption(opt);
        }
        return o;
    }

    private static Option s(String opt, boolean hasArg) {
        return new Option(opt, hasArg, "test option " + opt);
    }

    // ---------------------------------------------------------------------------------------------
    // Null-safety
    // ---------------------------------------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void flatten_withNullOptionsAndArgs_throwsNPE() throws Exception {
        new PosixParser().flatten(null, null, true);
    }

    // ---------------------------------------------------------------------------------------------
    // Basic short option handling and bursting
    // ---------------------------------------------------------------------------------------------

    @Test
    public void combinedShortOptions_lastOneConsumesAttachedValue() throws Exception {
        // -a and -b have no args, -c requires an argument attached to the same token.
        Options options = opts(s("a", false), s("b", false), s("c", true));

        String[] flat = flatten(options, false, "-abcvalue");

        assertArrayEquals(new String[] { "-a", "-b", "-c", "value" }, flat);
    }

    @Test
    public void shortOption_withSeparateValue_preserved() throws Exception {
        Options options = opts(s("c", true));

        String[] flat = flatten(options, false, "-c", "value");

        assertArrayEquals(new String[] { "-c", "value" }, flat);
    }

    // ---------------------------------------------------------------------------------------------
    // Non-option handling with/without stopAtNonOption
    // ---------------------------------------------------------------------------------------------

    @Test
    public void unknownShortOption_isCopiedWhenStopAtNonOptionTrue() throws Exception {
        // Only -a is known; -x is unknown. With stopAtNonOption=true, copy remaining entries verbatim.
        Options options = opts(s("a", false));

        String[] args = { "-x", "file" };
        String[] flat = flatten(options, true, args);

        assertArrayEquals(new String[] { "-x", "file" }, flat);
    }

    @Test
    public void unknownShortOption_isIgnoredWhenStopAtNonOptionFalse() throws Exception {
        // Only -a is known; -x is unknown. With stopAtNonOption=false, the unknown entry is ignored.
        Options options = opts(s("a", false));

        String[] flat = flatten(options, false, "-x", "-a");

        assertArrayEquals(new String[] { "-a" }, flat);
    }

    @Test
    public void afterFirstNonOption_andStopAtNonOptionTrue_restIsNotBurst() throws Exception {
        // Once a non-option ("file") is seen with stopAtNonOption=true, PosixParser should:
        // - inject "--"
        // - copy the current and remaining tokens as-is (no further bursting of "-b")
        Options options = opts(s("a", false), s("b", false));

        String[] flat = flatten(options, true, "-a", "file", "-b");

        assertArrayEquals(new String[] { "-a", "--", "file", "-b" }, flat);
    }

    // ---------------------------------------------------------------------------------------------
    // Special tokens
    // ---------------------------------------------------------------------------------------------

    @Test
    public void doubleDash_isPreserved() throws Exception {
        Options options = opts(s("a", false));

        String[] flat = flatten(options, false, "--", "-a", "file");

        assertArrayEquals(new String[] { "--", "-a", "file" }, flat);
    }

    @Test
    public void loneDash_isPreserved() throws Exception {
        Options options = new Options();

        String[] flat = flatten(options, false, "-");

        assertArrayEquals(new String[] { "-" }, flat);
    }
}