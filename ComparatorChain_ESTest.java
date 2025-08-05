/*
 * Refactored test suite for better understandability and maintainability.
 */
package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.BitSet;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.comparators.ComparatorChain;
import org.apache.commons.collections4.functors.ClosureTransformer;
import org.apache.commons.collections4.functors.ComparatorPredicate;
import org.apache.commons.collections4.functors.ExceptionClosure;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class ComparatorChain_ESTest extends ComparatorChain_ESTest_scaffolding {

  // ====================== Construction & Initial State Tests ======================
  
  @Test(timeout = 4000)
  public void testConstructorWithNullListThrowsNPE() {
    try {
      ComparatorChain<ByteBuffer> chain = new ComparatorChain<>((List<Comparator<ByteBuffer>>) null);
      fail("Expected NullPointerException");
    } catch(NullPointerException e) {
      // Expected behavior
    }
  }

  @Test(timeout = 4000)
  public void testEmptyChainSizeIsZero() {
    ComparatorChain<ComparatorPredicate.Criterion> chain = new ComparatorChain<>();
    assertEquals(0, chain.size());
  }

  // ====================== Locking Behavior Tests ======================
  
  @Test(timeout = 4000)
  public void testLockedAfterComparison() throws Throwable {
    ToLongFunction<Object> mockFunc = mock(ToLongFunction.class);
    doReturn(513L, -971L).when(mockFunc).applyAsLong(any());
    Comparator<Object> comp = Comparator.comparingLong(mockFunc);
    ComparatorChain<Object> chain = new ComparatorChain<>(comp);
    
    chain.compare(new Object(), new Object()); // Perform comparison
    assertTrue("Chain should be locked after comparison", chain.isLocked());
  }

  @Test(timeout = 4000)
  public void testUnlockedBeforeComparison() {
    LinkedList<Comparator<Object>> comparators = new LinkedList<>();
    BitSet bits = new BitSet();
    ComparatorChain<Object> chain = new ComparatorChain<>(comparators, bits);
    assertFalse("Chain should not be locked initially", chain.isLocked());
  }

  // ====================== Comparator Modification Tests ======================
  
  @Test(timeout = 4000)
  public void testSetComparatorFailsAfterLocking() {
    ToIntFunction<Integer> mockFunc = mock(ToIntFunction.class);
    doReturn(780, -1576).when(mockFunc).applyAsInt(anyInt());
    Comparator<Integer> comp = Comparator.comparingInt(mockFunc);
    ComparatorChain<Integer> chain = new ComparatorChain<>(comp, true);
    Integer value = 780;
    
    chain.compare(value, value); // Lock the chain
    try {
      chain.setComparator(780, comp, false);
      fail("Expected UnsupportedOperationException");
    } catch(UnsupportedOperationException e) {
      assertEquals("Comparator ordering cannot be changed after the first comparison is performed", e.getMessage());
    }
  }

  @Test(timeout = 4000)
  public void testAddComparatorsBeforeLocking() {
    ComparatorChain<Integer> chain = new ComparatorChain<>();
    chain.addComparator(chain, true);
    chain.addComparator(chain, true);
    assertEquals(2, chain.size());
    assertFalse(chain.isLocked());
  }

  // ====================== Comparison Behavior Tests ======================
  
  @Test(timeout = 4000)
  public void testCompareThrowsWhenEmptyChain() {
    ComparatorChain<Object> chain = new ComparatorChain<>();
    try {
      chain.compare(chain, chain);
      fail("Expected UnsupportedOperationException");
    } catch(UnsupportedOperationException e) {
      assertEquals("ComparatorChains must contain at least one Comparator", e.getMessage());
    }
  }

  @Test(timeout = 4000)
  public void testCompareWithNullComparatorThrowsNPE() {
    ComparatorChain<Object> chain = new ComparatorChain<>((Comparator<Object>) null, false);
    Object obj = new Object();
    ComparatorChain<Integer> otherChain = new ComparatorChain<>();
    try {
      chain.compare(obj, otherChain);
      fail("Expected NullPointerException");
    } catch(NullPointerException e) {
      // Expected behavior
    }
  }

  @Test(timeout = 4000)
  public void testCompareHandlesEqualValues() {
    ToIntFunction<Integer> mockFunc = mock(ToIntFunction.class);
    doReturn(1, -1576, -1576, -206).when(mockFunc).applyAsInt(anyInt());
    Comparator<Integer> comp = Comparator.comparingInt(mockFunc);
    ComparatorChain<Integer> chain = new ComparatorChain<>(comp, true);
    Integer value = -318;
    
    int firstResult = chain.compare(value, value);
    int secondResult = chain.compare(value, value);
    
    assertTrue(chain.isLocked());
    assertEquals(1, firstResult);
    assertEquals(1, secondResult); // Note: Behavior might seem counterintuitive; chain is locked after first comparison
  }

  // ====================== Exception & Edge Case Tests ======================
  
  @Test(timeout = 4000)
  public void testSetReverseSortWithNegativeIndexThrowsException() {
    ComparatorChain<ComparatorPredicate.Criterion> chain = new ComparatorChain<>();
    try {
      chain.setReverseSort(-2145);
      fail("Expected IndexOutOfBoundsException");
    } catch(IndexOutOfBoundsException e) {
      assertEquals("bitIndex < 0: -2145", e.getMessage());
    }
  }

  @Test(timeout = 4000)
  public void testSizeWithNullListThrowsNPE() {
    ComparatorChain<Object> chain = new ComparatorChain<>(null, new BitSet());
    try {
      chain.size();
      fail("Expected NullPointerException");
    } catch(NullPointerException e) {
      // Expected behavior
    }
  }

  // ====================== Sort Order Configuration Tests ======================
  
  @Test(timeout = 4000)
  public void testSetReverseSortWhenEmptyChain() {
    ComparatorChain<ComparatorChain<Integer>> chain = new ComparatorChain<>();
    chain.setReverseSort(1306); // Should not throw with valid index
    assertEquals(0, chain.size());
  }

  @Test(timeout = 4000)
  public void testSetForwardSortWhenEmptyChain() {
    ComparatorChain<Comparator<Object>> chain = new ComparatorChain<>();
    chain.setForwardSort(0); // Should not throw
    assertEquals(0, chain.size());
  }

  // ====================== Equals & HashCode Tests ======================
  
  @Test(timeout = 4000)
  public void testHashCodeWithComparatorAdded() {
    ComparatorChain<Object> chain = new ComparatorChain<>();
    Comparator<Object> comp = Comparator.nullsLast(chain);
    chain.addComparator(comp, true);
    chain.hashCode(); // Should not throw
  }

  @Test(timeout = 4000)
  public void testEqualsForDifferentChains() {
    ComparatorChain<Object> chain1 = new ComparatorChain<>();
    ComparatorChain<Object> chain2 = new ComparatorChain<>(chain1, true);
    assertFalse("Chains with different configurations should not be equal", chain1.equals(chain2));
  }

  @Test(timeout = 4000)
  public void testEqualsForSameEmptyChains() {
    ComparatorChain<Object> chain1 = new ComparatorChain<>();
    ComparatorChain<Object> chain2 = new ComparatorChain<>();
    assertTrue("Empty chains should be equal", chain1.equals(chain2));
  }

  // ====================== Complex Interaction Tests ======================
  
  @Test(timeout = 4000)
  public void testComparatorChainWithClosureTransformerThrowsException() {
    LinkedList<Comparator<Object>> comparators = new LinkedList<>();
    ComparatorChain<Object> baseChain = new ComparatorChain<>(comparators);
    Closure<Object> exceptionClosure = ExceptionClosure.exceptionClosure();
    ClosureTransformer<Object> transformer = new ClosureTransformer<>(exceptionClosure);
    Comparator<ComparatorChain<Object>> comp = Comparator.comparing(transformer, baseChain);
    ComparatorChain<ComparatorChain<Object>> chain = new ComparatorChain<>(comp);
    
    try {
      chain.compare(baseChain, baseChain);
      fail("Expected RuntimeException from ExceptionClosure");
    } catch(RuntimeException e) {
      assertEquals("ExceptionClosure invoked", e.getMessage());
    }
  }
}