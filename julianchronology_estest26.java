package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.evosuite.runtime.mock.java.time.MockLocalDate;
import org.evosuite.runtime.mock.java.time.MockLocalDateTime;
import org.evosuite.runtime.mock.java.time.MockOffsetDateTime;
import org.evosuite.runtime.mock.java.time.chrono.MockHijrahDate;
import org.evosuite.runtime.mock.java.time.chrono.MockJapaneseDate;
import org.junit.runner.RunWith;

public class JulianChronology_ESTestTest26 extends JulianChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        JulianChronology julianChronology0 = new JulianChronology();
        HijrahDate hijrahDate0 = MockHijrahDate.now();
        PaxDate paxDate0 = PaxDate.from(hijrahDate0);
        ChronoUnit chronoUnit0 = ChronoUnit.MILLENNIA;
        PaxDate paxDate1 = paxDate0.minus((-2458L), (TemporalUnit) chronoUnit0);
        PaxDate paxDate2 = paxDate1.plusYears(1308L);
        // Undeclared exception!
        try {
            julianChronology0.INSTANCE.date((TemporalAccessor) paxDate2);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            //
            // Invalid value for Year (valid values -999998 - 999999): 2461271
            //
            verifyException("java.time.temporal.ValueRange", e);
        }
    }
}
