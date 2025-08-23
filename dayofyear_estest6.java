package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.HijrahDate;
import java.time.chrono.ThaiBuddhistDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQuery;
import java.time.temporal.UnsupportedTemporalTypeException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockYear;
import org.evosuite.runtime.mock.java.time.MockYearMonth;
import org.evosuite.runtime.mock.java.time.MockZonedDateTime;
import org.evosuite.runtime.mock.java.time.chrono.MockHijrahDate;
import org.evosuite.runtime.mock.java.time.chrono.MockThaiBuddhistDate;
import org.junit.runner.RunWith;

public class DayOfYear_ESTestTest6 extends DayOfYear_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        DayOfYear dayOfYear0 = DayOfYear.of(1);
        Year year0 = MockYear.now();
        LocalDate localDate0 = dayOfYear0.atYear(year0);
        TemporalQuery<ChronoLocalDate> temporalQuery0 = (TemporalQuery<ChronoLocalDate>) mock(TemporalQuery.class, new ViolatedAssumptionAnswer());
        doReturn(localDate0).when(temporalQuery0).queryFrom(any(java.time.temporal.TemporalAccessor.class));
        ChronoLocalDate chronoLocalDate0 = dayOfYear0.query(temporalQuery0);
        assertSame(localDate0, chronoLocalDate0);
    }
}
