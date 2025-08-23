package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.evosuite.runtime.mock.java.time.MockZonedDateTime;
import org.junit.runner.RunWith;

public class BritishCutoverChronology_ESTestTest3 extends BritishCutoverChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        BritishCutoverChronology britishCutoverChronology0 = new BritishCutoverChronology();
        HashMap<TemporalField, Long> hashMap0 = new HashMap<TemporalField, Long>();
        ChronoField chronoField0 = ChronoField.EPOCH_DAY;
        Long long0 = new Long(763L);
        hashMap0.putIfAbsent(chronoField0, long0);
        ResolverStyle resolverStyle0 = ResolverStyle.STRICT;
        BritishCutoverDate britishCutoverDate0 = britishCutoverChronology0.resolveDate(hashMap0, resolverStyle0);
        assertNotNull(britishCutoverDate0);
    }
}
