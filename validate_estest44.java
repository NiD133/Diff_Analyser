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

public class Validate_ESTestTest44 extends Validate_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test43() throws Throwable {
        Object[] objectArray0 = new Object[2];
        objectArray0[1] = (Object) "";
        Validate.notNullParam(objectArray0[1], "p4%]]3_}o");
        assertEquals(2, objectArray0.length);
    }
}
