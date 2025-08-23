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

public class Symmetry010Chronology_ESTestTest49 extends Symmetry010Chronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test48() throws Throwable {
        Symmetry010Date symmetry010Date0 = Symmetry010Date.ofEpochDay(146L);
        Symmetry010Chronology symmetry010Chronology0 = symmetry010Date0.getChronology();
        ChronoField chronoField0 = ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
        ValueRange valueRange0 = symmetry010Chronology0.range(chronoField0);
        assertNotNull(valueRange0);
    }
}