package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.evosuite.runtime.mock.java.time.MockLocalDate;
import org.evosuite.runtime.mock.java.time.MockOffsetDateTime;
import org.evosuite.runtime.mock.java.time.MockYear;
import org.junit.runner.RunWith;

public class Symmetry010Chronology_ESTestTest25 extends Symmetry010Chronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        Symmetry010Chronology symmetry010Chronology0 = Symmetry010Chronology.INSTANCE;
        Clock clock0 = MockClock.systemUTC();
        Duration duration0 = Duration.ofDays(365250000L);
        Clock clock1 = MockClock.offset(clock0, duration0);
        // Undeclared exception!
        try {
            symmetry010Chronology0.dateNow(clock1);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            //
            // Invalid value for EpochDay (valid values -365961480 - 364523156): 365266118
            //
            verifyException("java.time.temporal.ValueRange", e);
        }
    }
}
