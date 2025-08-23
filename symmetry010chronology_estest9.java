package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.Clock;
// ... many more unused imports
import org.evosuite.runtime.mock.java.time.MockYear;
import org.junit.runner.RunWith;

public class Symmetry010Chronology_ESTestTest9 extends Symmetry010Chronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        long long0 = Symmetry010Chronology.getLeapYearsBefore(560L);
        assertEquals(99L, long0);
    }
}