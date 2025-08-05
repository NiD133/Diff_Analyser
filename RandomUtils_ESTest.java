package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.security.SecureRandom;
import java.util.Random;
import org.apache.commons.lang3.RandomUtils;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class RandomUtils_ESTest extends RandomUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testSecureRandomFloatInRange() throws Throwable {
        RandomUtils randomUtils = RandomUtils.secure();
        float result = randomUtils.randomFloat(1.0F, 1797.5656F);
        // Unstable assertion: assertEquals(102.1026F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testSecureRandomBytesWithZeroLength() throws Throwable {
        RandomUtils randomUtils = RandomUtils.secure();
        byte[] result = randomUtils.randomBytes(0);
        assertEquals(0, result.length);
    }

    @Test(timeout = 4000)
    public void testInsecureRandomFloatAndBoolean() throws Throwable {
        RandomUtils randomUtils = RandomUtils.insecure();
        float floatResult = RandomUtils.nextFloat();
        // Unstable assertion: assertEquals(6.2246244E37F, floatResult, 0.01F);

        boolean booleanResult = randomUtils.randomBoolean();
        // Unstable assertion: assertTrue(booleanResult);
    }

    @Test(timeout = 4000)
    public void testInsecureRandomInstanceNotNull() throws Throwable {
        RandomUtils randomUtils = RandomUtils.insecure();
        Random randomInstance = randomUtils.random();
        assertNotNull(randomInstance);
    }

    @Test(timeout = 4000)
    public void testNextLongWithZeroRange() throws Throwable {
        long result = RandomUtils.nextLong(0L, 0L);
        assertEquals(0L, result);
    }

    @Test(timeout = 4000)
    public void testNextIntWithEqualBounds() throws Throwable {
        int result = RandomUtils.nextInt(404, 404);
        assertEquals(404, result);
    }

    @Test(timeout = 4000)
    public void testNextFloatWithZeroRange() throws Throwable {
        float result = RandomUtils.nextFloat(0.0F, 0.0F);
        assertEquals(0.0F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testNextDoubleWithZeroRange() throws Throwable {
        double result = RandomUtils.nextDouble(0.0, 0.0);
        assertEquals(0.0, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testNextBytesWithZeroLength() throws Throwable {
        byte[] result = RandomUtils.nextBytes(0);
        assertEquals(0, result.length);
    }

    @Test(timeout = 4000)
    public void testSecureRandomLongWithInvalidRange() throws Throwable {
        RandomUtils randomUtils = RandomUtils.secureStrong();
        try {
            randomUtils.randomLong(-9, -9);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testSecureRandomLongWithStartGreaterThanEnd() throws Throwable {
        RandomUtils randomUtils = RandomUtils.secure();
        try {
            randomUtils.randomLong(1314, 0L);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testSecureRandomIntWithZeroRange() throws Throwable {
        RandomUtils randomUtils = RandomUtils.secureStrong();
        int result = randomUtils.randomInt(0, 0);
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testSecureRandomIntWithInvalidRange() throws Throwable {
        RandomUtils randomUtils = RandomUtils.secureStrong();
        try {
            randomUtils.randomInt(-1830, -1830);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testSecureRandomIntWithStartGreaterThanEnd() throws Throwable {
        RandomUtils randomUtils = RandomUtils.secureStrong();
        try {
            randomUtils.randomInt(0, -1163);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testRandomBytesWithNegativeCount() throws Throwable {
        RandomUtils randomUtils = new RandomUtils();
        try {
            randomUtils.randomBytes(-1);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testInsecureRandomBytesWithPositiveCount() throws Throwable {
        RandomUtils randomUtils = RandomUtils.insecure();
        byte[] result = randomUtils.randomBytes(404);
        assertEquals(404, result.length);
    }

    @Test(timeout = 4000)
    public void testNextLongWithEqualBounds() throws Throwable {
        long result = RandomUtils.nextLong(2144936217L, 2144936217L);
        assertEquals(2144936217L, result);
    }

    @Test(timeout = 4000)
    public void testNextLongWithStartGreaterThanEnd() throws Throwable {
        try {
            RandomUtils.nextLong(920L, 32L);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextIntWithZeroRange() throws Throwable {
        int result = RandomUtils.nextInt(0, 0);
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testNextIntWithStartGreaterThanEnd() throws Throwable {
        try {
            RandomUtils.nextInt(0, -114);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextFloatWithEqualBounds() throws Throwable {
        float result = RandomUtils.nextFloat(1.0F, 1.0F);
        assertEquals(1.0F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testNextFloatWithStartGreaterThanEnd() throws Throwable {
        try {
            RandomUtils.nextFloat(254551010162654302F, -145.13795F);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextDoubleWithEqualBounds() throws Throwable {
        double result = RandomUtils.nextDouble(269.70357193641604, 269.70357193641604);
        assertEquals(269.70357193641604, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testNextDoubleWithStartGreaterThanEnd() throws Throwable {
        try {
            RandomUtils.nextDouble(1.7976931348623157E308, 2616.0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextBytesWithNegativeCount() throws Throwable {
        try {
            RandomUtils.nextBytes(-669);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testSecureRandomInstanceNotNull() throws Throwable {
        SecureRandom secureRandom = RandomUtils.secureRandom();
        assertNotNull(secureRandom);
    }

    @Test(timeout = 4000)
    public void testSecureRandomFloatWithZeroRange() throws Throwable {
        RandomUtils randomUtils = RandomUtils.secureStrong();
        float result = randomUtils.randomFloat(0.0F, 0.0F);
        assertEquals(0.0F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testSecureRandomToStringNotNull() throws Throwable {
        RandomUtils randomUtils = RandomUtils.secure();
        String result = randomUtils.toString();
        assertNotNull(result);
    }
}