package org.apache.commons.io.function;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class UncheckTest extends UncheckTestScaffolding {

    private static final String SAMPLE_STRING = "SampleString";
    private static final String EMPTY_STRING = "";

    @Test(timeout = 4000)
    public void testNoopTriConsumer() {
        IOTriConsumer<String, String, String> triConsumer = IOTriConsumer.noop();
        Uncheck.accept(triConsumer, EMPTY_STRING, SAMPLE_STRING, SAMPLE_STRING);
    }

    @Test(timeout = 4000)
    public void testNoopBiConsumer() {
        IOBiConsumer<String, String> biConsumer = IOBiConsumer.noop();
        Uncheck.accept(biConsumer, SAMPLE_STRING, SAMPLE_STRING);
    }

    @Test(timeout = 4000)
    public void testNoopConsumer() {
        IOConsumer<String> consumer = IOConsumer.noop();
        Uncheck.accept(consumer, EMPTY_STRING);
    }

    @Test(timeout = 4000)
    public void testAlwaysTruePredicate() {
        IOPredicate<String> predicate = IOPredicate.alwaysTrue();
        assertTrue(Uncheck.test(predicate, SAMPLE_STRING));
    }

    @Test(timeout = 4000)
    public void testMockedLongSupplierReturnsZero() {
        IOLongSupplier longSupplier = mock(IOLongSupplier.class);
        doReturn(0L).when(longSupplier).getAsLong();
        assertEquals(0L, Uncheck.getAsLong(longSupplier));
    }

    @Test(timeout = 4000)
    public void testMockedLongSupplierReturnsPositiveValue() {
        IOLongSupplier longSupplier = mock(IOLongSupplier.class);
        doReturn(1139L).when(longSupplier).getAsLong();
        assertEquals(1139L, Uncheck.getAsLong(longSupplier));
    }

    @Test(timeout = 4000)
    public void testMockedLongSupplierReturnsNegativeValue() {
        IOLongSupplier longSupplier = mock(IOLongSupplier.class);
        doReturn(-890L).when(longSupplier).getAsLong();
        assertEquals(-890L, Uncheck.getAsLong(longSupplier));
    }

    @Test(timeout = 4000)
    public void testMockedIntSupplierReturnsZero() {
        IOIntSupplier intSupplier = mock(IOIntSupplier.class);
        doReturn(0).when(intSupplier).getAsInt();
        assertEquals(0, Uncheck.getAsInt(intSupplier));
    }

    @Test(timeout = 4000)
    public void testMockedIntSupplierReturnsPositiveValue() {
        IOIntSupplier intSupplier = mock(IOIntSupplier.class);
        doReturn(2726).when(intSupplier).getAsInt();
        assertEquals(2726, Uncheck.getAsInt(intSupplier));
    }

    @Test(timeout = 4000)
    public void testMockedIntSupplierReturnsNegativeValue() {
        IOIntSupplier intSupplier = mock(IOIntSupplier.class);
        doReturn(-1830).when(intSupplier).getAsInt();
        assertEquals(-1830, Uncheck.getAsInt(intSupplier));
    }

    @Test(timeout = 4000)
    public void testMockedBooleanSupplierReturnsTrue() {
        IOBooleanSupplier booleanSupplier = mock(IOBooleanSupplier.class);
        doReturn(true).when(booleanSupplier).getAsBoolean();
        assertTrue(Uncheck.getAsBoolean(booleanSupplier));
    }

    @Test(timeout = 4000)
    public void testMockedBooleanSupplierReturnsFalse() {
        IOBooleanSupplier booleanSupplier = mock(IOBooleanSupplier.class);
        doReturn(false).when(booleanSupplier).getAsBoolean();
        assertFalse(Uncheck.getAsBoolean(booleanSupplier));
    }

    @Test(timeout = 4000)
    public void testIdentityFunctionReturnsInput() {
        IOFunction<String, String> identityFunction = IOFunction.identity();
        assertEquals(SAMPLE_STRING, Uncheck.apply(identityFunction, SAMPLE_STRING));
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnNullTriFunction() {
        try {
            Uncheck.apply((IOTriFunction<String, String, String, String>) null, SAMPLE_STRING, SAMPLE_STRING, SAMPLE_STRING);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnNullBiFunction() {
        try {
            Uncheck.apply((IOBiFunction<String, String, String>) null, SAMPLE_STRING, SAMPLE_STRING);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnNullRunnable() {
        try {
            Uncheck.run((IORunnable) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Additional tests can be added here following the same pattern
}