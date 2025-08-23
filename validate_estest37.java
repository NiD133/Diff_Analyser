package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.FormatFlagsConversionMismatchException;
import java.util.IllegalFormatConversionException;
import java.util.IllegalFormatFlagsException;
import java.util.IllegalFormatWidthException;
import java.util.MissingFormatArgumentException;
import java.util.MissingFormatWidthException;
import java.util.UnknownFormatConversionException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Validate_ESTestTest37 extends Validate_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test36() throws Throwable {
        // Undeclared exception!
        try {
            Validate.ensureNotNull((Object) null, "1WS}t~\"$s?(X!g,(jF!", (Object[]) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // 1WS}t~\"$s?(X!g,(jF!
            //
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}
