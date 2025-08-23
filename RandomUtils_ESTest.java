package org.apache.commons.lang3;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for RandomUtils.
 * These tests avoid asserting specific random values; instead they verify ranges, lengths, nullability,
 * and well-defined boundary and error behavior.
 */
class RandomUtilsReadableTest {

    // -------- Instance factories --------

    @Test
    void insecure_returnsInstanceAndRandom() {
        RandomUtils utils = RandomUtils.insecure();
        assertNotNull(utils, "insecure() must return a non-null instance");
        Random rnd = utils.random();
        assertNotNull(rnd, "insecure().random() must return a non-null Random");
    }

    @Test
    void secure_returnsInstance() {
        RandomUtils utils = RandomUtils.secure();
        assertNotNull(utils, "secure() must return a non-null instance");
    }

    @Test
    void secureStrong_returnsInstance() {
        RandomUtils utils = RandomUtils.secureStrong();
        assertNotNull(utils, "secureStrong() must return a non-null instance");
    }

    @Test
    void secureRandom_returnsNonNullSecureRandom() {
        SecureRandom sr = RandomUtils.secureRandom();
        assertNotNull(sr, "secureRandom() must return a non-null SecureRandom");
    }

    // -------- Static methods: boundary behavior --------

    @Test
    void nextLong_whenStartEqualsEnd_returnsStart() {
        assertEquals(0L, RandomUtils.nextLong(0L, 0L));
        assertEquals(2144936217L, RandomUtils.nextLong(2144936217L, 2144936217L));
    }

    @Test
    void nextInt_whenStartEqualsEnd_returnsStart() {
        assertEquals(404, RandomUtils.nextInt(404, 404));
        assertEquals(0, RandomUtils.nextInt(0, 0));
    }

    @Test
    void nextFloat_whenStartEqualsEnd_returnsStart() {
        assertEquals(0.0F, RandomUtils.nextFloat(0.0F, 0.0F), 0.0F);
        assertEquals(1.0F, RandomUtils.nextFloat(1.0F, 1.0F), 0.0F);
    }

    @Test
    void nextDouble_whenStartEqualsEnd_returnsStart() {
        assertEquals(0.0, RandomUtils.nextDouble(0.0, 0.0), 0.0);
        double v = 269.70357193641604;
        assertEquals(v, RandomUtils.nextDouble(v, v), 0.0);
    }

    // -------- Static methods: valid ranges --------

    @Test
    void nextPrimitives_areInDocumentedRanges() {
        int i = RandomUtils.nextInt();
        long l = RandomUtils.nextLong();
        float f = RandomUtils.nextFloat();
        double d = RandomUtils.nextDouble();

        assertAll(
            () -> assertTrue(i >= 0, "nextInt() must be >= 0"),
            () -> assertTrue(i < Integer.MAX_VALUE, "nextInt() must be < Integer.MAX_VALUE"),

            () -> assertTrue(l >= 0L, "nextLong() must be >= 0"),
            () -> assertTrue(l < Long.MAX_VALUE, "nextLong() must be < Long.MAX_VALUE"),

            () -> assertTrue(Float.isFinite(f) && f >= 0.0F, "nextFloat() must be finite and >= 0"),
            () -> assertTrue(f < Float.MAX_VALUE, "nextFloat() must be < Float.MAX_VALUE"),

            () -> assertTrue(Double.isFinite(d) && d >= 0.0, "nextDouble() must be finite and >= 0"),
            () -> assertTrue(d < Double.MAX_VALUE, "nextDouble() must be < Double.MAX_VALUE")
        );
    }

    @Test
    void nextBytes_zeroLength_returnsEmptyArray() {
        assertEquals(0, RandomUtils.nextBytes(0).length);
    }

    // -------- Static methods: invalid arguments --------

    @Test
    void nextLong_invalidRange_throwsIAE() {
        assertThrows(IllegalArgumentException.class, () -> RandomUtils.nextLong(920L, 32L));
    }

    @Test
    void nextInt_invalidRange_throwsIAE() {
        assertThrows(IllegalArgumentException.class, () -> RandomUtils.nextInt(0, -114));
    }

    @Test
    void nextFloat_invalidRange_throwsIAE() {
        assertThrows(IllegalArgumentException.class, () -> RandomUtils.nextFloat(2.0F, 1.0F));
        assertThrows(IllegalArgumentException.class, () -> RandomUtils.nextFloat(-1.0F, -1.0F));
    }

    @Test
    void nextDouble_invalidRange_throwsIAE() {
        assertThrows(IllegalArgumentException.class, () -> RandomUtils.nextDouble(10.0, 1.0));
        assertThrows(IllegalArgumentException.class, () -> RandomUtils.nextDouble(-5.0, -5.0));
    }

    @Test
    void nextBytes_negativeCount_throwsIAE() {
        assertThrows(IllegalArgumentException.class, () -> RandomUtils.nextBytes(-1));
    }

    // -------- Instance methods: boundary behavior --------

    @Test
    void instance_randomLong_whenStartEqualsEnd_returnsStart() {
        RandomUtils utils = RandomUtils.insecure();
        assertEquals(0L, utils.randomLong(0L, 0L));
    }

    @Test
    void instance_randomInt_whenStartEqualsEnd_returnsStart() {
        RandomUtils utils = RandomUtils.secureStrong();
        assertEquals(0, utils.randomInt(0, 0));
    }

    @Test
    void instance_randomFloat_whenStartEqualsEnd_returnsStart() {
        RandomUtils utils = RandomUtils.secureStrong();
        assertEquals(0.0F, utils.randomFloat(0.0F, 0.0F), 0.0F);
    }

    @Test
    void instance_randomDouble_whenStartEqualsEnd_returnsStart() {
        RandomUtils utils = RandomUtils.insecure();
        assertEquals(0.0, utils.randomDouble(0.0, 0.0), 0.0);
    }

    // -------- Instance methods: valid ranges --------

    @Test
    void instance_randomPrimitives_areInDocumentedRanges() {
        RandomUtils utils = RandomUtils.secure();

        int i = utils.randomInt();
        long l = utils.randomLong();
        float f = utils.randomFloat();
        double d = utils.randomDouble();

        assertAll(
            () -> assertTrue(i >= 0, "randomInt() must be >= 0"),
            () -> assertTrue(i < Integer.MAX_VALUE, "randomInt() must be < Integer.MAX_VALUE"),

            () -> assertTrue(l >= 0L, "randomLong() must be >= 0"),
            () -> assertTrue(l < Long.MAX_VALUE, "randomLong() must be < Long.MAX_VALUE"),

            () -> assertTrue(Float.isFinite(f) && f >= 0.0F, "randomFloat() must be finite and >= 0"),
            () -> assertTrue(f < Float.MAX_VALUE, "randomFloat() must be < Float.MAX_VALUE"),

            () -> assertTrue(Double.isFinite(d) && d >= 0.0, "randomDouble() must be finite and >= 0"),
            () -> assertTrue(d < Double.MAX_VALUE, "randomDouble() must be < Double.MAX_VALUE")
        );
    }

    @Test
    void instance_randomBytes_respectsRequestedLength() {
        RandomUtils utils = RandomUtils.insecure();
        assertEquals(0, utils.randomBytes(0).length);
        assertEquals(404, utils.randomBytes(404).length);
    }

    @Test
    void instance_randomBoolean_returnsABoolean() {
        RandomUtils utils = RandomUtils.insecure();
        // We cannot assert its value; just ensure it returns without error.
        boolean value = utils.randomBoolean();
        assertTrue(value || !value, "randomBoolean() must return a boolean value");
    }

    // -------- Instance methods: invalid arguments --------

    @Test
    void instance_randomLong_invalidRanges_throwIAE() {
        RandomUtils utils = RandomUtils.secureStrong();
        assertThrows(IllegalArgumentException.class, () -> utils.randomLong(-9L, -9L));
        assertThrows(IllegalArgumentException.class, () -> utils.randomLong(1314L, 0L));
    }

    @Test
    void instance_randomInt_invalidRanges_throwIAE() {
        RandomUtils utils = RandomUtils.secureStrong();
        assertThrows(IllegalArgumentException.class, () -> utils.randomInt(-1830, -1830));
        assertThrows(IllegalArgumentException.class, () -> utils.randomInt(0, -1163));
    }

    @Test
    void instance_randomFloat_invalidRanges_throwIAE() {
        RandomUtils utils = RandomUtils.insecure();
        assertThrows(IllegalArgumentException.class, () -> utils.randomFloat(-3238.355F, -3238.355F));
        assertThrows(IllegalArgumentException.class, () -> utils.randomFloat(3.0e1F, 1.0F)); // start > end
    }

    @Test
    void instance_randomDouble_invalidRanges_throwIAE() {
        RandomUtils utils = RandomUtils.insecure();
        assertThrows(IllegalArgumentException.class, () -> utils.randomDouble(-5.0, -5.0));
        assertThrows(IllegalArgumentException.class, () -> utils.randomDouble(2316.0, 0.0));
    }

    @Test
    void instance_randomBytes_negativeCount_throwsIAE() {
        RandomUtils utils = new RandomUtils(); // Deprecated ctor, used to exercise instance path
        assertThrows(IllegalArgumentException.class, () -> utils.randomBytes(-1));
    }

    // -------- Misc --------

    @Test
    void toString_isNonNull() {
        assertNotNull(RandomUtils.secure().toString());
    }
}