package org.apache.commons.io.file.attribute;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.Date;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.junit.runner.RunWith;

public class FileTimes_ESTestTest29 extends FileTimes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        BigDecimal bigDecimal0 = new BigDecimal(82.953588012);
        // Undeclared exception!
        try {
            FileTimes.ntfsTimeToInstant(bigDecimal0);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // Rounding necessary
            //
            verifyException("java.math.BigDecimal", e);
        }
    }
}
