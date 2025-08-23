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

public class FileTimes_ESTestTest50 extends FileTimes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test49() throws Throwable {
        FileTime fileTime0 = FileTimes.ntfsTimeToFileTime(369L);
        FileTime fileTime1 = FileTimes.minusMillis(fileTime0, 0L);
        assertTrue(fileTime1.equals((Object) fileTime0));
    }
}
