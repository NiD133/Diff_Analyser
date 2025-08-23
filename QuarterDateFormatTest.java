package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link QuarterDateFormat} class.
 */
public class QuarterDateFormatTest {

    // Common, descriptive constants to avoid duplication and magic values
    private static final TimeZone TZ_GMT = TimeZone.getTimeZone("GMT");
    private static final TimeZone TZ_PST = TimeZone.getTimeZone("PST");
    private static final String[] QUARTERS_1234 = {"1", "2", "3", "4"};
    private static final String[] QUARTERS_A234 = {"A", "2", "3", "4"};

    // Helper factory methods make intent explicit at call sites
    private static QuarterDateFormat qdf(TimeZone tz, String[] symbols) {
        return new QuarterDateFormat(tz, symbols);
    }

    private static QuarterDateFormat qdf(TimeZone tz, String[] symbols, boolean quarterFirst) {
        return new QuarterDateFormat(tz, symbols, quarterFirst);
    }

    @Test
    public void equals_returnsTrueForSameValues() {
        QuarterDateFormat a = qdf(TZ_GMT, QUARTERS_1234);
        QuarterDateFormat b = qdf(TZ_GMT, QUARTERS_1234);

        assertEquals(a, b);
        assertEquals(b, a); // symmetric
    }

    @Test
    public void equals_distinguishesDifferentTimeZone() {
        QuarterDateFormat base = qdf(TZ_GMT, QUARTERS_1234);
        QuarterDateFormat differentTz = qdf(TZ_PST, QUARTERS_1234);

        assertNotEquals(base, differentTz);

        QuarterDateFormat sameAsDifferentTz = qdf(TZ_PST, QUARTERS_1234);
        assertEquals(differentTz, sameAsDifferentTz);
    }

    @Test
    public void equals_distinguishesDifferentQuarterSymbols() {
        QuarterDateFormat base = qdf(TZ_PST, QUARTERS_1234);
        QuarterDateFormat differentSymbols = qdf(TZ_PST, QUARTERS_A234);

        assertNotEquals(base, differentSymbols);

        QuarterDateFormat sameAsDifferentSymbols = qdf(TZ_PST, QUARTERS_A234);
        assertEquals(differentSymbols, sameAsDifferentSymbols);
    }

    @Test
    public void equals_distinguishesQuarterFirstFlag() {
        QuarterDateFormat base = qdf(TZ_PST, QUARTERS_A234, false);
        QuarterDateFormat differentFlag = qdf(TZ_PST, QUARTERS_A234, true);

        assertNotEquals(base, differentFlag);

        QuarterDateFormat sameAsDifferentFlag = qdf(TZ_PST, QUARTERS_A234, true);
        assertEquals(differentFlag, sameAsDifferentFlag);
    }

    @Test
    public void equals_contract_basicCases() {
        QuarterDateFormat a = qdf(TZ_GMT, QUARTERS_1234);

        // reflexive
        assertEquals(a, a);

        // null
        assertNotEquals(a, null);

        // different type
        assertNotEquals(a, new Object());
    }

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void hashCode_equalObjectsProduceSameHash() {
        QuarterDateFormat a = qdf(TZ_GMT, QUARTERS_1234);
        QuarterDateFormat b = qdf(TZ_GMT, QUARTERS_1234);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    /**
     * Confirm that cloning creates an equal but distinct instance.
     */
    @Test
    public void cloning_createsEqualButDistinctInstance() {
        QuarterDateFormat original = qdf(TZ_GMT, QUARTERS_1234);

        QuarterDateFormat clone = (QuarterDateFormat) original.clone();

        assertNotSame(original, clone);
        assertSame(original.getClass(), clone.getClass());
        assertEquals(original, clone);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void serialization_roundTripPreservesEquality() {
        QuarterDateFormat original = qdf(TZ_GMT, QUARTERS_1234);

        QuarterDateFormat restored = TestUtils.serialised(original);

        assertEquals(original, restored);
    }
}