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

public class FileTimes_ESTestTest6 extends FileTimes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        BigDecimal bigDecimal0 = new BigDecimal((-9223372036854775808L));
        Date date0 = FileTimes.ntfsTimeToDate(bigDecimal0);
        long long0 = FileTimes.toNtfsTime(date0);
        assertEquals((-9223372036854775808L), long0);
        assertEquals("Thu Nov 14 21:11:54 GMT 27628", date0.toString());
    }
}
