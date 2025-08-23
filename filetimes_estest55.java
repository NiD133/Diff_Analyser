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

public class FileTimes_ESTestTest55 extends FileTimes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test54() throws Throwable {
        FileTime fileTime0 = FileTime.fromMillis(120L);
        Date date0 = FileTimes.toDate(fileTime0);
        assertNotNull(date0);
        long long0 = FileTimes.toNtfsTime(date0);
        assertEquals(116444736001200000L, long0);
    }
}
