package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the PosixParser class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class PosixParser_ESTest extends PosixParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testBurstTokenWithEmptyString() throws Throwable {
        PosixParser posixParser = new PosixParser();
        posixParser.burstToken("", true);
    }

    @Test(timeout = 4000)
    public void testFlattenWithNullOptionsAndArguments() throws Throwable {
        PosixParser posixParser = new PosixParser();
        try {
            posixParser.flatten(null, null, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testFlattenWithRequiredOption() throws Throwable {
        Options options = new Options();
        options.addRequiredOption("j", "j", true, "j");
        PosixParser posixParser = new PosixParser();
        String[] args = {"", "-j", "", "", ""};
        String[] result = posixParser.flatten(options, args, true);
        assertEquals(1, result.length);
    }

    @Test(timeout = 4000)
    public void testFlattenWithComplexOption() throws Throwable {
        Options options = new Options();
        options.addRequiredOption("j", "org.apache.commons.cli.PosixParser", true, "j");
        PosixParser posixParser = new PosixParser();
        String[] args = {"", "", "-org.apache.commons.cli.PosixParser", "", "", "", "", "", ""};
        posixParser.flatten(options, args, false);
        posixParser.burstToken(";-", true);
    }

    @Test(timeout = 4000)
    public void testFlattenWithNonexistentOption() throws Throwable {
        Options options = new Options();
        PosixParser posixParser = new PosixParser();
        String[] args = {"", "-j", "", "", ""};
        String[] result = posixParser.flatten(options, args, true);
        assertEquals(4, result.length);
    }

    @Test(timeout = 4000)
    public void testFlattenWithMultipleOptions() throws Throwable {
        Options options = new Options();
        options.addRequiredOption("j", "j", false, "j");
        PosixParser posixParser = new PosixParser();
        String[] args = {"", "", "", "", "", "--=<iy", "", "", "", "", ""};
        String[] result1 = posixParser.flatten(options, args, false);
        String[] result2 = posixParser.flatten(options, result1, true);
        assertEquals(3, result2.length);
    }

    @Test(timeout = 4000)
    public void testFlattenWithAmbiguousOption() throws Throwable {
        Options options = new Options();
        options.addRequiredOption("j", "j", false, "j");
        options.addOption("j", "--WS", false, "--WS");
        PosixParser posixParser = new PosixParser();
        String[] args = {"", "", "", "", "", "", "", "", "", "", "--=<ibn", ""};
        try {
            posixParser.flatten(options, args, false);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.cli.PosixParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testFlattenWithSingleOption() throws Throwable {
        String[] args = new String[37];
        args[19] = "--K";
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        String[] result = posixParser.flatten(options, args, false);
        assertEquals(1, result.length);
    }

    @Test(timeout = 4000)
    public void testFlattenWithSpecialCharacters() throws Throwable {
        Options options = new Options();
        PosixParser posixParser = new PosixParser();
        String[] args = {"", "", "", "", "", "--=<q;n", "", "", ""};
        String[] result = posixParser.flatten(options, args, false);
        assertEquals(1, result.length);
    }

    @Test(timeout = 4000)
    public void testFlattenWithISOCountries() throws Throwable {
        Options options = new Options();
        String[] isoCountries = Locale.getISOCountries();
        PosixParser posixParser = new PosixParser();
        String[] result1 = posixParser.flatten(options, isoCountries, true);
        String[] result2 = posixParser.flatten(options, result1, true);
        assertEquals(252, result2.length);
        assertEquals(251, result1.length);
    }

    @Test(timeout = 4000)
    public void testFlattenWithSingleDash() throws Throwable {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        String[] args = {"", "", "", "", "-"};
        String[] result = posixParser.flatten(options, args, true);
        assertEquals(1, result.length);
    }

    @Test(timeout = 4000)
    public void testBurstTokenWithDoubleDash() throws Throwable {
        Options options = new Options();
        options.addRequiredOption("j", "", true, "");
        PosixParser posixParser = new PosixParser();
        String[] isoCountries = Locale.getISOCountries();
        posixParser.flatten(options, isoCountries, true);
        posixParser.burstToken("--", true);
    }

    @Test(timeout = 4000)
    public void testBurstTokenWithDoubleDashAndNonOption() throws Throwable {
        Options options = new Options();
        options.addRequiredOption("q", "", false, "q");
        PosixParser posixParser = new PosixParser();
        String[] isoCountries = Locale.getISOCountries();
        posixParser.flatten(options, isoCountries, false);
        posixParser.burstToken("--", false);
    }

    @Test(timeout = 4000)
    public void testFlattenWithSlashOption() throws Throwable {
        Options options = new Options();
        String[] args = {"", "", "", "-/C", "", ""};
        PosixParser posixParser = new PosixParser();
        String[] result = posixParser.flatten(options, args, false);
        assertEquals(1, result.length);
    }

    @Test(timeout = 4000)
    public void testBurstTokenWithSpecialCharacters() throws Throwable {
        Options options = new Options();
        options.addRequiredOption("j", "", true, "j");
        PosixParser posixParser = new PosixParser();
        String[] isoCountries = Locale.getISOCountries();
        posixParser.flatten(options, isoCountries, true);
        posixParser.burstToken("$--", true);
    }

    @Test(timeout = 4000)
    public void testBurstTokenWithSemicolonDash() throws Throwable {
        PosixParser posixParser = new PosixParser();
        try {
            posixParser.burstToken(";-", true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.cli.PosixParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testFlattenAndBurstToken() throws Throwable {
        Options options = new Options();
        PosixParser posixParser = new PosixParser();
        String[] args = new String[9];
        posixParser.flatten(options, args, false);
        posixParser.burstToken(";-", true);
    }
}