package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import org.apache.commons.lang3.function.FailableIntFunction;

/**
 * Test suite for ArrayFill utility class.
 * Tests the fill operations for all primitive array types and object arrays.
 */
public class ArrayFillTest {

    // ========== Boolean Array Tests ==========
    
    @Test
    public void testFillBooleanArray_WithValues() {
        boolean[] array = new boolean[4];
        
        boolean[] result = ArrayFill.fill(array, true);
        
        assertSame("Should return the same array instance", array, result);
        assertArrayEquals("All elements should be true", 
            new boolean[]{true, true, true, true}, result);
    }
    
    @Test
    public void testFillBooleanArray_EmptyArray() {
        boolean[] emptyArray = new boolean[0];
        
        boolean[] result = ArrayFill.fill(emptyArray, true);
        
        assertSame("Should return the same array instance", emptyArray, result);
        assertEquals("Array should remain empty", 0, result.length);
    }
    
    @Test
    public void testFillBooleanArray_NullArray() {
        boolean[] result = ArrayFill.fill((boolean[]) null, true);
        
        assertNull("Should return null when input is null", result);
    }

    // ========== Byte Array Tests ==========
    
    @Test
    public void testFillByteArray_WithValues() {
        byte[] array = new byte[1];
        
        byte[] result = ArrayFill.fill(array, (byte) 76);
        
        assertSame("Should return the same array instance", array, result);
        assertArrayEquals("All elements should be 76", 
            new byte[]{(byte) 76}, result);
    }
    
    @Test
    public void testFillByteArray_EmptyArray() {
        byte[] emptyArray = new byte[0];
        
        byte[] result = ArrayFill.fill(emptyArray, (byte) 0);
        
        assertSame("Should return the same array instance", emptyArray, result);
        assertEquals("Array should remain empty", 0, result.length);
    }
    
    @Test
    public void testFillByteArray_NullArray() {
        byte[] result = ArrayFill.fill((byte[]) null, (byte) 87);
        
        assertNull("Should return null when input is null", result);
    }

    // ========== Character Array Tests ==========
    
    @Test
    public void testFillCharArray_WithValues() {
        char[] array = new char[1];
        
        char[] result = ArrayFill.fill(array, ']');
        
        assertSame("Should return the same array instance", array, result);
        assertArrayEquals("All elements should be ']'", 
            new char[]{']'}, result);
    }
    
    @Test
    public void testFillCharArray_EmptyArray() {
        char[] emptyArray = new char[0];
        
        char[] result = ArrayFill.fill(emptyArray, 's');
        
        assertSame("Should return the same array instance", emptyArray, result);
        assertEquals("Array should remain empty", 0, result.length);
    }
    
    @Test
    public void testFillCharArray_NullArray() {
        char[] result = ArrayFill.fill((char[]) null, 'B');
        
        assertNull("Should return null when input is null", result);
    }

    // ========== Short Array Tests ==========
    
    @Test
    public void testFillShortArray_WithValues() {
        short[] array = new short[6];
        
        short[] result = ArrayFill.fill(array, (short) -838);
        
        assertSame("Should return the same array instance", array, result);
        assertArrayEquals("All elements should be -838", 
            new short[]{-838, -838, -838, -838, -838, -838}, result);
    }
    
    @Test
    public void testFillShortArray_EmptyArray() {
        short[] emptyArray = new short[0];
        
        short[] result = ArrayFill.fill(emptyArray, (short) 2);
        
        assertSame("Should return the same array instance", emptyArray, result);
        assertEquals("Array should remain empty", 0, result.length);
    }
    
    @Test
    public void testFillShortArray_NullArray() {
        short[] result = ArrayFill.fill((short[]) null, (short) -3333);
        
        assertNull("Should return null when input is null", result);
    }

    // ========== Integer Array Tests ==========
    
    @Test
    public void testFillIntArray_WithValues() {
        int[] array = new int[7];
        
        int[] result = ArrayFill.fill(array, -1);
        
        assertSame("Should return the same array instance", array, result);
        assertArrayEquals("All elements should be -1", 
            new int[]{-1, -1, -1, -1, -1, -1, -1}, result);
    }
    
    @Test
    public void testFillIntArray_EmptyArray() {
        int[] emptyArray = new int[0];
        
        int[] result = ArrayFill.fill(emptyArray, 0);
        
        assertSame("Should return the same array instance", emptyArray, result);
        assertEquals("Array should remain empty", 0, result.length);
    }
    
    @Test
    public void testFillIntArray_NullArray() {
        int[] result = ArrayFill.fill((int[]) null, 0);
        
        assertNull("Should return null when input is null", result);
    }

    // ========== Long Array Tests ==========
    
    @Test
    public void testFillLongArray_WithValues() {
        long[] array = new long[8];
        
        long[] result = ArrayFill.fill(array, -1L);
        
        assertSame("Should return the same array instance", array, result);
        assertArrayEquals("All elements should be -1L", 
            new long[]{-1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L}, result);
    }
    
    @Test
    public void testFillLongArray_EmptyArray() {
        long[] emptyArray = new long[0];
        
        long[] result = ArrayFill.fill(emptyArray, 0L);
        
        assertSame("Should return the same array instance", emptyArray, result);
        assertEquals("Array should remain empty", 0, result.length);
    }
    
    @Test
    public void testFillLongArray_NullArray() {
        long[] result = ArrayFill.fill((long[]) null, 1003L);
        
        assertNull("Should return null when input is null", result);
    }

    // ========== Float Array Tests ==========
    
    @Test
    public void testFillFloatArray_WithValues() {
        float[] array = new float[5];
        
        float[] result = ArrayFill.fill(array, 95.0F);
        
        assertSame("Should return the same array instance", array, result);
        assertArrayEquals("All elements should be 95.0F", 
            new float[]{95.0F, 95.0F, 95.0F, 95.0F, 95.0F}, result, 0.01F);
    }
    
    @Test
    public void testFillFloatArray_EmptyArray() {
        float[] emptyArray = new float[0];
        
        float[] result = ArrayFill.fill(emptyArray, 1.0F);
        
        assertSame("Should return the same array instance", emptyArray, result);
        assertEquals("Array should remain empty", 0, result.length);
    }
    
    @Test
    public void testFillFloatArray_NullArray() {
        float[] result = ArrayFill.fill((float[]) null, -4645.361F);
        
        assertNull("Should return null when input is null", result);
    }

    // ========== Double Array Tests ==========
    
    @Test
    public void testFillDoubleArray_WithValues() {
        double[] array = new double[8];
        
        double[] result = ArrayFill.fill(array, 0.0);
        
        assertSame("Should return the same array instance", array, result);
        assertArrayEquals("All elements should be 0.0", 
            new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, result, 0.01);
    }
    
    @Test
    public void testFillDoubleArray_EmptyArray() {
        double[] emptyArray = new double[0];
        
        double[] result = ArrayFill.fill(emptyArray, -259.0);
        
        assertSame("Should return the same array instance", emptyArray, result);
        assertEquals("Array should remain empty", 0, result.length);
    }
    
    @Test
    public void testFillDoubleArray_NullArray() {
        double[] result = ArrayFill.fill((double[]) null, -3333.0);
        
        assertNull("Should return null when input is null", result);
    }

    // ========== Object Array Tests ==========
    
    @Test
    public void testFillObjectArray_WithValue() {
        String[] array = new String[3];
        String testValue = "test";
        
        String[] result = ArrayFill.fill(array, testValue);
        
        assertSame("Should return the same array instance", array, result);
        for (String element : result) {
            assertSame("All elements should reference the same object", testValue, element);
        }
    }
    
    @Test
    public void testFillObjectArray_EmptyArray() {
        Object[] emptyArray = new Object[0];
        Object testValue = new Object();
        
        Object[] result = ArrayFill.fill(emptyArray, testValue);
        
        assertSame("Should return the same array instance", emptyArray, result);
        assertEquals("Array should remain empty", 0, result.length);
    }
    
    @Test
    public void testFillObjectArray_NullArray() {
        Object testValue = new Object();
        
        Object[] result = ArrayFill.fill((Object[]) null, testValue);
        
        assertNull("Should return null when input is null", result);
    }

    // ========== Function-based Fill Tests ==========
    
    @Test
    public void testFillWithFunction_EmptyArray() {
        Object[] emptyArray = new Object[0];
        FailableIntFunction<Object, Throwable> generator = FailableIntFunction.nop();
        
        Object[] result = ArrayFill.fill(emptyArray, generator);
        
        assertSame("Should return the same array instance", emptyArray, result);
        assertEquals("Array should remain empty", 0, result.length);
    }
    
    @Test
    public void testFillWithFunction_WithValues() {
        Object[] array = new Object[6];
        FailableIntFunction<Object, Throwable> generator = FailableIntFunction.nop();
        
        Object[] result = ArrayFill.fill(array, generator);
        
        assertSame("Should return the same array instance", array, result);
        // Note: nop() function returns null for all indices
        for (Object element : result) {
            assertNull("All elements should be null (nop function)", element);
        }
    }
    
    @Test
    public void testFillWithFunction_NullArray() {
        FailableIntFunction<Object, Throwable> generator = FailableIntFunction.nop();
        
        Object[] result = ArrayFill.fill((Object[]) null, generator);
        
        assertNull("Should return null when input array is null", result);
    }
    
    @Test
    public void testFillWithFunction_NullFunction() {
        Object[] array = new Object[4];
        
        Object[] result = ArrayFill.fill(array, (FailableIntFunction<?, Throwable>) null);
        
        assertSame("Should return the same array instance", array, result);
        // Behavior with null function depends on implementation
    }
    
    @Test(expected = ArrayStoreException.class)
    public void testFillObjectArray_IncompatibleType() {
        // Test type safety - trying to store incompatible object type
        Throwable[] throwableArray = new Throwable[3];
        FailableIntFunction<Object, Throwable> generator = FailableIntFunction.nop();
        
        // This should throw ArrayStoreException because we're trying to store 
        // a FailableIntFunction in a Throwable array
        ArrayFill.fill((Object[]) throwableArray, (Object) generator);
    }
}