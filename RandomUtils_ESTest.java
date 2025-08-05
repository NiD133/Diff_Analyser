package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import java.security.SecureRandom;
import java.util.Random;
import org.apache.commons.lang3.RandomUtils;

/**
 * Test suite for RandomUtils class functionality.
 * Tests cover three main RandomUtils instances: secure(), secureStrong(), and insecure(),
 * as well as deprecated static methods.
 */
public class RandomUtils_ESTest {

    // ========== Factory Method Tests ==========
    
    @Test
    public void testSecureFactoryMethod() {
        RandomUtils secureUtils = RandomUtils.secure();
        assertNotNull("Secure RandomUtils instance should not be null", secureUtils);
        
        Random random = secureUtils.random();
        assertNotNull("Underlying random should not be null", random);
    }
    
    @Test
    public void testSecureStrongFactoryMethod() {
        RandomUtils secureStrongUtils = RandomUtils.secureStrong();
        assertNotNull("SecureStrong RandomUtils instance should not be null", secureStrongUtils);
    }
    
    @Test
    public void testInsecureFactoryMethod() {
        RandomUtils insecureUtils = RandomUtils.insecure();
        assertNotNull("Insecure RandomUtils instance should not be null", insecureUtils);
    }
    
    @Test
    public void testSecureRandomMethod() {
        SecureRandom secureRandom = RandomUtils.secureRandom();
        assertNotNull("SecureRandom instance should not be null", secureRandom);
    }

    // ========== Boolean Generation Tests ==========
    
    @Test
    public void testRandomBooleanGeneration() {
        RandomUtils utils = RandomUtils.insecure();
        boolean result = utils.randomBoolean();
        // Boolean can be either true or false - just verify no exception is thrown
        assertTrue("Boolean should be true or false", result == true || result == false);
    }
    
    @Test
    public void testDeprecatedNextBoolean() {
        boolean result = RandomUtils.nextBoolean();
        assertTrue("Boolean should be true or false", result == true || result == false);
    }

    // ========== Byte Array Generation Tests ==========
    
    @Test
    public void testRandomBytesWithZeroLength() {
        RandomUtils utils = RandomUtils.secure();
        byte[] result = utils.randomBytes(0);
        assertEquals("Empty byte array should have length 0", 0, result.length);
    }
    
    @Test
    public void testRandomBytesWithPositiveLength() {
        RandomUtils utils = RandomUtils.insecure();
        int expectedLength = 404;
        byte[] result = utils.randomBytes(expectedLength);
        assertEquals("Byte array should have requested length", expectedLength, result.length);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomBytesWithNegativeLengthThrowsException() {
        RandomUtils utils = new RandomUtils();
        utils.randomBytes(-1);
    }
    
    @Test
    public void testDeprecatedNextBytesWithZeroLength() {
        byte[] result = RandomUtils.nextBytes(0);
        assertEquals("Empty byte array should have length 0", 0, result.length);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedNextBytesWithNegativeLengthThrowsException() {
        RandomUtils.nextBytes(-669);
    }

    // ========== Integer Generation Tests ==========
    
    @Test
    public void testRandomIntGeneration() {
        RandomUtils utils = RandomUtils.insecure();
        int result = utils.randomInt();
        // Any integer is valid - just verify no exception is thrown
        assertTrue("Integer generation should complete without error", true);
    }
    
    @Test
    public void testRandomIntWithEqualBounds() {
        RandomUtils utils = RandomUtils.secureStrong();
        int expectedValue = 0;
        int result = utils.randomInt(expectedValue, expectedValue);
        assertEquals("Random int with equal bounds should return that value", expectedValue, result);
    }
    
    @Test
    public void testRandomIntWithValidRange() {
        RandomUtils utils = new RandomUtils();
        int start = 228;
        int end = Integer.MAX_VALUE;
        int result = utils.randomInt(start, end);
        assertTrue("Result should be within specified range", result >= start && result < end);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomIntWithNegativeBoundsThrowsException() {
        RandomUtils utils = RandomUtils.secureStrong();
        utils.randomInt(-1830, -1830);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomIntWithInvalidRangeThrowsException() {
        RandomUtils utils = RandomUtils.secureStrong();
        utils.randomInt(0, -1163); // start > end
    }
    
    @Test
    public void testDeprecatedNextIntWithEqualBounds() {
        int expectedValue = 404;
        int result = RandomUtils.nextInt(expectedValue, expectedValue);
        assertEquals("Next int with equal bounds should return that value", expectedValue, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedNextIntWithInvalidRangeThrowsException() {
        RandomUtils.nextInt(0, -114); // start > end
    }

    // ========== Long Generation Tests ==========
    
    @Test
    public void testRandomLongGeneration() {
        RandomUtils utils = RandomUtils.insecure();
        long result = utils.randomLong();
        // Any long is valid - just verify no exception is thrown
        assertTrue("Long generation should complete without error", true);
    }
    
    @Test
    public void testRandomLongWithEqualBounds() {
        RandomUtils utils = RandomUtils.insecure();
        long expectedValue = 0L;
        long result = utils.randomLong(expectedValue, expectedValue);
        assertEquals("Random long with equal bounds should return that value", expectedValue, result);
    }
    
    @Test
    public void testRandomLongWithValidRange() {
        RandomUtils utils = RandomUtils.secureStrong();
        long start = 0L;
        long end = 452L;
        long result = utils.randomLong(start, end);
        assertTrue("Result should be within specified range", result >= start && result < end);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomLongWithNegativeBoundsThrowsException() {
        RandomUtils utils = RandomUtils.secureStrong();
        utils.randomLong(-9L, -9L);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomLongWithInvalidRangeThrowsException() {
        RandomUtils utils = RandomUtils.secure();
        utils.randomLong(1314L, 0L); // start > end
    }
    
    @Test
    public void testDeprecatedNextLongWithEqualBounds() {
        long expectedValue = 0L;
        long result = RandomUtils.nextLong(expectedValue, expectedValue);
        assertEquals("Next long with equal bounds should return that value", expectedValue, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedNextLongWithInvalidRangeThrowsException() {
        RandomUtils.nextLong(920L, 32L); // start > end
    }

    // ========== Float Generation Tests ==========
    
    @Test
    public void testRandomFloatGeneration() {
        RandomUtils utils = RandomUtils.insecure();
        float result = utils.randomFloat();
        assertTrue("Float should be non-negative", result >= 0.0F);
    }
    
    @Test
    public void testRandomFloatWithValidRange() {
        RandomUtils utils = RandomUtils.secure();
        float start = 1.0F;
        float end = 1797.5656F;
        float result = utils.randomFloat(start, end);
        assertTrue("Result should be within specified range", result >= start && result < end);
    }
    
    @Test
    public void testRandomFloatWithEqualBounds() {
        RandomUtils utils = RandomUtils.secureStrong();
        float expectedValue = 0.0F;
        float result = utils.randomFloat(expectedValue, expectedValue);
        assertEquals("Random float with equal bounds should return that value", expectedValue, result, 0.01F);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomFloatWithNegativeBoundsThrowsException() {
        RandomUtils utils = new RandomUtils();
        utils.randomFloat(-3238.355F, -3238.355F);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomFloatWithInvalidRangeThrowsException() {
        RandomUtils utils = RandomUtils.insecure();
        utils.randomFloat(3.3724796E38F, 1.0F); // start > end
    }
    
    @Test
    public void testDeprecatedNextFloatWithEqualBounds() {
        float expectedValue = 0.0F;
        float result = RandomUtils.nextFloat(expectedValue, expectedValue);
        assertEquals("Next float with equal bounds should return that value", expectedValue, result, 0.01F);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedNextFloatWithInvalidRangeThrowsException() {
        RandomUtils.nextFloat(254551010162654302L, -145.13795F); // start > end
    }

    // ========== Double Generation Tests ==========
    
    @Test
    public void testRandomDoubleGeneration() {
        RandomUtils utils = new RandomUtils();
        double result = utils.randomDouble();
        assertTrue("Double should be non-negative", result >= 0.0);
    }
    
    @Test
    public void testRandomDoubleWithValidRange() {
        RandomUtils utils = RandomUtils.secure();
        double start = 117.68;
        double end = 2958.43561;
        double result = utils.randomDouble(start, end);
        assertTrue("Result should be within specified range", result >= start && result < end);
    }
    
    @Test
    public void testRandomDoubleWithEqualBounds() {
        RandomUtils utils = RandomUtils.insecure();
        double expectedValue = 0.0;
        double result = utils.randomDouble(expectedValue, expectedValue);
        assertEquals("Random double with equal bounds should return that value", expectedValue, result, 0.01);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomDoubleWithNegativeBoundsThrowsException() {
        RandomUtils utils = RandomUtils.insecure();
        utils.randomDouble(-5.0, -5.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomDoubleWithInvalidRangeThrowsException() {
        RandomUtils utils = RandomUtils.insecure();
        utils.randomDouble(2316.0, 0.0); // start > end
    }
    
    @Test
    public void testDeprecatedNextDoubleWithEqualBounds() {
        double expectedValue = 0.0;
        double result = RandomUtils.nextDouble(expectedValue, expectedValue);
        assertEquals("Next double with equal bounds should return that value", expectedValue, result, 0.01);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedNextDoubleWithInvalidRangeThrowsException() {
        RandomUtils.nextDouble(Double.MAX_VALUE, 2616.0); // start > end
    }

    // ========== Utility Method Tests ==========
    
    @Test
    public void testToStringMethod() {
        RandomUtils utils = RandomUtils.secure();
        String result = utils.toString();
        assertNotNull("toString should return non-null string", result);
    }
    
    // ========== Deprecated Static Method Tests ==========
    
    @Test
    public void testDeprecatedStaticMethods() {
        // Test that deprecated static methods still work
        RandomUtils.nextInt();
        RandomUtils.nextLong();
        RandomUtils.nextFloat();
        RandomUtils.nextDouble();
        RandomUtils.nextBoolean();
        RandomUtils.nextBytes(1);
        
        // Just verify no exceptions are thrown
        assertTrue("Deprecated static methods should work without error", true);
    }
}