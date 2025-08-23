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

public class FileTimes_ESTestTest26 extends FileTimes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        FileTime fileTime0 = FileTimes.fromUnixTime((-116444736000000000L));
        // Undeclared exception!
        try {
            FileTimes.plusMillis(fileTime0, (-116444736000000000L));
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            //
            // Instant exceeds minimum or maximum instant
            //
            verifyException("java.time.Instant", e);
        }
    }
}
