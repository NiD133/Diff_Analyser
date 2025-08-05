package org.apache.commons.io.function;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.function.Supplier;
import org.apache.commons.io.function.*;

/**
 * Test suite for the Uncheck utility class.
 * Tests the various methods that wrap IO operations to throw UncheckedIOException instead of IOException.
 */
public class UncheckTest {

    // ========== Consumer Tests ==========
    
    @Test
    public void testAcceptTriConsumer_WithNoOpConsumer_ShouldExecuteSuccessfully() {
        IOTriConsumer<String, String, String> consumer = IOTriConsumer.noop();
        
        Uncheck.accept(consumer, "first", "second", "third");
        
        // No exception should be thrown
    }

    @Test
    public void testAcceptBiConsumer_WithNoOpConsumer_ShouldExecuteSuccessfully() {
        IOBiConsumer<String, String> consumer = IOBiConsumer.noop();
        
        Uncheck.accept(consumer, "input1", "input2");
        
        // No exception should be thrown
    }

    @Test
    public void testAcceptConsumer_WithNoOpConsumer_ShouldExecuteSuccessfully() {
        IOConsumer<String> consumer = IOConsumer.noop();
        
        Uncheck.accept(consumer, "input");
        
        // No exception should be thrown
    }

    @Test
    public void testAcceptIntConsumer_WithMockedConsumer_ShouldExecuteSuccessfully() {
        IOIntConsumer consumer = mock(IOIntConsumer.class);
        
        Uncheck.accept(consumer, 111);
        
        // No exception should be thrown
    }

    // ========== Predicate Tests ==========
    
    @Test
    public void testTest_WithAlwaysTruePredicate_ShouldReturnTrue() {
        IOPredicate<String> predicate = IOPredicate.alwaysTrue();
        
        boolean result = Uncheck.test(predicate, "test-input");
        
        assertTrue(result);
    }

    @Test
    public void testTest_WithAlwaysFalsePredicate_ShouldReturnFalse() {
        IOPredicate<String> predicate = IOPredicate.alwaysFalse();
        
        boolean result = Uncheck.test(predicate, "test-input");
        
        assertFalse(result);
    }

    // ========== Supplier Tests ==========
    
    @Test
    public void testGetAsLong_WithZeroValue_ShouldReturnZero() {
        IOLongSupplier supplier = mock(IOLongSupplier.class);
        when(supplier.getAsLong()).thenReturn(0L);
        
        long result = Uncheck.getAsLong(supplier);
        
        assertEquals(0L, result);
    }

    @Test
    public void testGetAsLong_WithPositiveValue_ShouldReturnValue() {
        IOLongSupplier supplier = mock(IOLongSupplier.class);
        when(supplier.getAsLong()).thenReturn(2444L);
        
        long result = Uncheck.getAsLong(supplier);
        
        assertEquals(2444L, result);
    }

    @Test
    public void testGetAsLong_WithNegativeValue_ShouldReturnValue() {
        IOLongSupplier supplier = mock(IOLongSupplier.class);
        when(supplier.getAsLong()).thenReturn(-2307L);
        
        long result = Uncheck.getAsLong(supplier);
        
        assertEquals(-2307L, result);
    }

    @Test
    public void testGetAsLong_WithMessageSupplier_ShouldReturnValue() {
        IOLongSupplier supplier = mock(IOLongSupplier.class);
        when(supplier.getAsLong()).thenReturn(1139L);
        
        long result = Uncheck.getAsLong(supplier, (Supplier<String>) null);
        
        assertEquals(1139L, result);
    }

    @Test
    public void testGetAsInt_WithZeroValue_ShouldReturnZero() {
        IOIntSupplier supplier = mock(IOIntSupplier.class);
        when(supplier.getAsInt()).thenReturn(0);
        
        int result = Uncheck.getAsInt(supplier);
        
        assertEquals(0, result);
    }

    @Test
    public void testGetAsInt_WithPositiveValue_ShouldReturnValue() {
        IOIntSupplier supplier = mock(IOIntSupplier.class);
        when(supplier.getAsInt()).thenReturn(1165);
        
        int result = Uncheck.getAsInt(supplier);
        
        assertEquals(1165, result);
    }

    @Test
    public void testGetAsInt_WithNegativeValue_ShouldReturnValue() {
        IOIntSupplier supplier = mock(IOIntSupplier.class);
        when(supplier.getAsInt()).thenReturn(-1830);
        
        int result = Uncheck.getAsInt(supplier);
        
        assertEquals(-1830, result);
    }

    @Test
    public void testGetAsInt_WithMessageSupplier_ShouldReturnValue() {
        IOIntSupplier supplier = mock(IOIntSupplier.class);
        when(supplier.getAsInt()).thenReturn(2726);
        
        int result = Uncheck.getAsInt(supplier, (Supplier<String>) null);
        
        assertEquals(2726, result);
    }

    @Test
    public void testGetAsBoolean_WithTrueValue_ShouldReturnTrue() {
        IOBooleanSupplier supplier = mock(IOBooleanSupplier.class);
        when(supplier.getAsBoolean()).thenReturn(true);
        
        boolean result = Uncheck.getAsBoolean(supplier);
        
        assertTrue(result);
    }

    @Test
    public void testGetAsBoolean_WithFalseValue_ShouldReturnFalse() {
        IOBooleanSupplier supplier = mock(IOBooleanSupplier.class);
        when(supplier.getAsBoolean()).thenReturn(false);
        
        boolean result = Uncheck.getAsBoolean(supplier);
        
        assertFalse(result);
    }

    @Test
    public void testGet_WithNullSupplier_ShouldReturnNull() {
        IOSupplier<Object> supplier = mock(IOSupplier.class);
        when(supplier.get()).thenReturn(null);
        
        Object result = Uncheck.get(supplier);
        
        assertNull(result);
    }

    @Test
    public void testGet_WithMessageSupplier_ShouldReturnNull() {
        IOSupplier<Object> supplier = mock(IOSupplier.class);
        when(supplier.get()).thenReturn(null);
        
        Object result = Uncheck.get(supplier, (Supplier<String>) null);
        
        assertNull(result);
    }

    // ========== Comparator Tests ==========
    
    @Test
    public void testCompare_WithPositiveResult_ShouldReturnPositive() {
        IOComparator<String> comparator = mock(IOComparator.class);
        when(comparator.compare(anyString(), anyString())).thenReturn(553);
        
        int result = Uncheck.compare(comparator, "string1", "string2");
        
        assertEquals(553, result);
    }

    @Test
    public void testCompare_WithNegativeResult_ShouldReturnNegative() {
        IOComparator<String> comparator = mock(IOComparator.class);
        when(comparator.compare(anyString(), anyString())).thenReturn(-502);
        
        int result = Uncheck.compare(comparator, "string1", "string2");
        
        assertEquals(-502, result);
    }

    // ========== Function Tests ==========
    
    @Test
    public void testApplyFunction_WithIdentityFunction_ShouldReturnInput() {
        IOFunction<String, String> function = IOFunction.identity();
        
        String result = Uncheck.apply(function, "test-input");
        
        assertEquals("test-input", result);
    }

    @Test
    public void testApplyFunction_WithNullInput_ShouldReturnNull() {
        IOFunction<String, String> function = IOFunction.identity();
        
        String result = Uncheck.apply(function, null);
        
        assertNull(result);
    }

    @Test
    public void testApplyBiFunction_WithMockedFunction_ShouldReturnMockedResult() {
        IOBiFunction<String, String, String> function = mock(IOBiFunction.class);
        when(function.apply(anyString(), anyString())).thenReturn("mocked-result");
        
        String result = Uncheck.apply(function, "input1", "input2");
        
        assertEquals("mocked-result", result);
    }

    @Test
    public void testApplyBiFunction_WithNullResult_ShouldReturnNull() {
        IOBiFunction<String, String, String> function = mock(IOBiFunction.class);
        when(function.apply(anyString(), anyString())).thenReturn(null);
        
        String result = Uncheck.apply(function, "input1", "input2");
        
        assertNull(result);
    }

    @Test
    public void testApplyTriFunction_WithMockedFunction_ShouldReturnMockedResult() {
        IOTriFunction<String, String, String, String> function = mock(IOTriFunction.class);
        when(function.apply(anyString(), anyString(), anyString())).thenReturn("tri-result");
        
        String result = Uncheck.apply(function, "arg1", "arg2", "arg3");
        
        assertEquals("tri-result", result);
    }

    @Test
    public void testApplyTriFunction_WithNullResult_ShouldReturnNull() {
        IOTriFunction<String, String, String, String> function = mock(IOTriFunction.class);
        when(function.apply(anyString(), anyString(), anyString())).thenReturn(null);
        
        String result = Uncheck.apply(function, "arg1", "arg2", "arg3");
        
        assertNull(result);
    }

    @Test
    public void testApplyQuadFunction_WithMockedFunction_ShouldReturnMockedResult() {
        IOQuadFunction<String, String, String, String, String> function = mock(IOQuadFunction.class);
        when(function.apply(anyString(), anyString(), anyString(), anyString())).thenReturn("quad-result");
        
        String result = Uncheck.apply(function, "arg1", "arg2", "arg3", "arg4");
        
        assertEquals("quad-result", result);
    }

    @Test
    public void testApplyQuadFunction_WithNullResult_ShouldReturnNull() {
        IOQuadFunction<String, String, String, String, String> function = mock(IOQuadFunction.class);
        when(function.apply(anyString(), anyString(), anyString(), anyString())).thenReturn(null);
        
        String result = Uncheck.apply(function, "arg1", "arg2", "arg3", "arg4");
        
        assertNull(result);
    }

    // ========== Runnable Tests ==========
    
    @Test
    public void testRun_WithNoOpRunnable_ShouldExecuteSuccessfully() {
        IORunnable runnable = IORunnable.noop();
        
        Uncheck.run(runnable);
        
        // No exception should be thrown
    }

    @Test
    public void testRun_WithMessageSupplier_ShouldExecuteSuccessfully() {
        IORunnable runnable = IORunnable.noop();
        
        Uncheck.run(runnable, (Supplier<String>) null);
        
        // No exception should be thrown
    }

    // ========== Null Parameter Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void testTest_WithNullPredicate_ShouldThrowNullPointerException() {
        Uncheck.test(null, "input");
    }

    @Test(expected = NullPointerException.class)
    public void testRun_WithNullRunnable_ShouldThrowNullPointerException() {
        Uncheck.run(null);
    }

    @Test(expected = NullPointerException.class)
    public void testRun_WithNullRunnableAndMessage_ShouldThrowNullPointerException() {
        Uncheck.run(null, (Supplier<String>) null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetAsLong_WithNullSupplier_ShouldThrowNullPointerException() {
        Uncheck.getAsLong(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetAsLong_WithNullSupplierAndMessage_ShouldThrowNullPointerException() {
        Uncheck.getAsLong(null, (Supplier<String>) null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetAsInt_WithNullSupplier_ShouldThrowNullPointerException() {
        Uncheck.getAsInt(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetAsInt_WithNullSupplierAndMessage_ShouldThrowNullPointerException() {
        Uncheck.getAsInt(null, (Supplier<String>) null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetAsBoolean_WithNullSupplier_ShouldThrowNullPointerException() {
        Uncheck.getAsBoolean(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGet_WithNullSupplier_ShouldThrowNullPointerException() {
        Uncheck.get((IOSupplier<String>) null);
    }

    @Test(expected = NullPointerException.class)
    public void testGet_WithNullSupplierAndMessage_ShouldThrowNullPointerException() {
        Uncheck.get((IOSupplier<String>) null, (Supplier<String>) null);
    }

    @Test(expected = NullPointerException.class)
    public void testCompare_WithNullComparator_ShouldThrowNullPointerException() {
        Uncheck.compare(null, "string1", "string2");
    }

    @Test(expected = NullPointerException.class)
    public void testApplyFunction_WithNullFunction_ShouldThrowNullPointerException() {
        Uncheck.apply((IOFunction<String, String>) null, "input");
    }

    @Test(expected = NullPointerException.class)
    public void testApplyBiFunction_WithNullFunction_ShouldThrowNullPointerException() {
        Uncheck.apply((IOBiFunction<String, String, String>) null, "input1", "input2");
    }

    @Test(expected = NullPointerException.class)
    public void testApplyTriFunction_WithNullFunction_ShouldThrowNullPointerException() {
        Uncheck.apply((IOTriFunction<String, String, String, String>) null, "arg1", "arg2", "arg3");
    }

    @Test(expected = NullPointerException.class)
    public void testApplyQuadFunction_WithNullFunction_ShouldThrowNullPointerException() {
        Uncheck.apply((IOQuadFunction<String, String, String, String, String>) null, "arg1", "arg2", "arg3", "arg4");
    }

    @Test(expected = NullPointerException.class)
    public void testAcceptConsumer_WithNullConsumer_ShouldThrowNullPointerException() {
        Uncheck.accept((IOConsumer<String>) null, "input");
    }

    @Test(expected = NullPointerException.class)
    public void testAcceptBiConsumer_WithNullConsumer_ShouldThrowNullPointerException() {
        Uncheck.accept((IOBiConsumer<String, String>) null, "input1", "input2");
    }

    @Test(expected = NullPointerException.class)
    public void testAcceptTriConsumer_WithNullConsumer_ShouldThrowNullPointerException() {
        Uncheck.accept((IOTriConsumer<String, String, String>) null, "arg1", "arg2", "arg3");
    }

    @Test(expected = NullPointerException.class)
    public void testAcceptIntConsumer_WithNullConsumer_ShouldThrowNullPointerException() {
        Uncheck.accept((IOIntConsumer) null, 42);
    }
}