package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.System;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.evosuite.runtime.mock.java.time.MockLocalDateTime;
import org.evosuite.runtime.mock.java.time.MockYear;
import org.evosuite.runtime.mock.java.time.MockZonedDateTime;
import org.junit.runner.RunWith;

public class InternationalFixedChronology_ESTestTest24 extends InternationalFixedChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        InternationalFixedChronology internationalFixedChronology0 = InternationalFixedChronology.INSTANCE;
        Instant instant0 = MockInstant.now();
        Period period0 = Period.ofDays((-2106965087));
        Instant instant1 = MockInstant.minus(instant0, (TemporalAmount) period0);
        ZoneOffset zoneOffset0 = ZoneOffset.MIN;
        // Undeclared exception!
        try {
            internationalFixedChronology0.zonedDateTime(instant1, (ZoneId) zoneOffset0);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            //
            // Unable to obtain ChronoLocalDateTime from TemporalAccessor: class java.time.LocalDateTime
            //
            verifyException("java.time.chrono.Chronology", e);
        }
    }
}
