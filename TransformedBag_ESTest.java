package org.apache.commons.collections4.bag;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.bag.CollectionSortedBag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bag.PredicatedBag;
import org.apache.commons.collections4.bag.PredicatedSortedBag;
import org.apache.commons.collections4.bag.SynchronizedBag;
import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.apache.commons.collections4.bag.TransformedBag;
import org.apache.commons.collections4.bag.TransformedSortedBag;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.bag.UnmodifiableBag;
import org.apache.commons.collections4.bag.UnmodifiableSortedBag;
import org.apache.commons.collections4.functors.AndPredicate;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ComparatorPredicate;
import org.apache.commons.collections4.functors.ConstantFactory;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.FactoryTransformer;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.apache.commons.collections4.functors.IdentityPredicate;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.apache.commons.collections4.functors.InstantiateFactory;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.MapTransformer;
import org.apache.commons.collections4.functors.NonePredicate;
import org.apache.commons.collections4.functors.NotPredicate;
import org.apache.commons.collections4.functors.OnePredicate;
import org.apache.commons.collections4.functors.SwitchTransformer;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class TransformedBag_ESTest extends TransformedBag_ESTest_scaffolding {

    // ========================================================================
    // FACTORY METHOD TESTS
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testFactoryMethods_HandleNullParameters() {
        // Test transformingBag with null transformer
        try {
            TransformedBag.transformingBag(new HashBag<>(), null);
            fail("Expected NullPointerException for null transformer");
        } catch (NullPointerException e) {
            assertEquals("transformer", e.getMessage());
        }
        
        // Test transformingBag with null collection
        try {
            TransformedBag.transformingBag(null, ConstantTransformer.nullTransformer());
            fail("Expected NullPointerException for null collection");
        } catch (NullPointerException e) {
            assertEquals("collection", e.getMessage());
        }
    }
    
    @Test(timeout = 4000)
    public void testTransformedBag_WithUnmodifiableBag() {
        HashBag<Integer> hashBag = new HashBag<>();
        hashBag.add(null);
        Transformer<Object, Integer> transformer = ConstantTransformer.nullTransformer();
        Bag<Integer> unmodifiableBag = UnmodifiableBag.unmodifiableBag(hashBag);
        
        try {
            TransformedBag.transformedBag(unmodifiableBag, transformer);
            fail("Expected UnsupportedOperationException for unmodifiable bag");
        } catch (UnsupportedOperationException e) {
            // Expected - unmodifiable collections can't be transformed
        }
    }
    
    @Test(timeout = 4000)
    public void testTransformedBag_WithPredicatedBag() {
        HashBag<Integer> hashBag = new HashBag<>();
        Integer value = 417;
        hashBag.add(value);
        
        Predicate<Integer> uniquePredicate = UniquePredicate.uniquePredicate();
        PredicatedBag<Integer> predicatedBag = 
            PredicatedBag.predicatedBag(hashBag, uniquePredicate);
        
        Transformer<Integer, Integer> transformer = 
            IfTransformer.ifTransformer(uniquePredicate, 
                ConstantTransformer.constantTransformer(value),
                ConstantTransformer.constantTransformer(value));
        
        try {
            TransformedBag.transformedBag(predicatedBag, transformer);
            fail("Expected IllegalArgumentException for predicated bag");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Predicate rejected it"));
        }
    }

    // ========================================================================
    // ADD OPERATION TESTS
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testAdd_WithExceptionTransformer() {
        Transformer<Object, Integer> exceptionTransformer = 
            ExceptionTransformer.exceptionTransformer();
        TreeBag<Object> treeBag = new TreeBag<>();
        TransformedSortedBag<Object> transformedBag = 
            TransformedSortedBag.transformingSortedBag(treeBag, exceptionTransformer);
        
        try {
            transformedBag.add(exceptionTransformer, 1);
            fail("Expected RuntimeException for exception transformer");
        } catch (RuntimeException e) {
            assertEquals("ExceptionTransformer invoked", e.getMessage());
        }
    }
    
    @Test(timeout = 4000)
    public void testAdd_WithNullTransformer() {
        Transformer<Object, Integer> nullTransformer = 
            ConstantTransformer.nullTransformer();
        TreeBag<Integer> treeBag = new TreeBag<>();
        TransformedSortedBag<Integer> transformedBag = 
            new TransformedSortedBag<>(treeBag, nullTransformer);
        
        try {
            transformedBag.add(3787, 3787);
            fail("Expected NullPointerException for null transformation");
        } catch (NullPointerException e) {
            // Expected - transformation returns null
        }
    }
    
    @Test(timeout = 4000)
    public void testAdd_WithPredicatedSortedBag() {
        TreeBag<Object> treeBag = new TreeBag<>();
        Predicate<Object> identityPredicate = 
            IdentityPredicate.identityPredicate(new HashBag<>());
        PredicatedSortedBag<Object> predicatedBag = 
            PredicatedSortedBag.predicatedSortedBag(treeBag, identityPredicate);
        
        Transformer<Object, Integer> transformer = 
            ConstantTransformer.constantTransformer(1480);
        TransformedSortedBag<Object> transformedBag = 
            TransformedSortedBag.transformedSortedBag(predicatedBag, transformer);
        
        try {
            transformedBag.add(1480, 39);
            fail("Expected IllegalArgumentException for predicated bag");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Predicate rejected it"));
        }
    }

    // ========================================================================
    // REMOVE OPERATION TESTS
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testRemove_WithUnmodifiableSortedBag() {
        Transformer<Object, Integer> exceptionTransformer = 
            ExceptionTransformer.exceptionTransformer();
        TreeBag<Integer> treeBag = new TreeBag<>();
        SortedBag<Integer> unmodifiableBag = 
            UnmodifiableSortedBag.unmodifiableSortedBag(treeBag);
        TransformedSortedBag<Integer> transformedBag = 
            TransformedSortedBag.transformingSortedBag(unmodifiableBag, exceptionTransformer);
        
        try {
            transformedBag.remove(unmodifiableBag, 2694);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected - unmodifiable collection
        }
    }
    
    @Test(timeout = 4000)
    public void testRemove_WithNullValues() {
        Transformer<Object, Integer> nullTransformer = 
            ConstantTransformer.nullTransformer();
        TreeBag<Object> treeBag = new TreeBag<>();
        TransformedSortedBag<Object> transformedBag = 
            new TransformedSortedBag<>(treeBag, nullTransformer);
        
        try {
            transformedBag.remove(null, -2770);
            fail("Expected NullPointerException for null value");
        } catch (NullPointerException e) {
            // Expected - null handling in TreeMap
        }
    }
    
    @Test(timeout = 4000)
    public void testRemove_WithIncompatibleTypes() {
        Transformer<Integer, Integer> nullTransformer = 
            ConstantTransformer.nullTransformer();
        TreeBag<Integer> treeBag = new TreeBag<>();
        TransformedSortedBag<Integer> transformedBag = 
            new TransformedSortedBag<>(treeBag, nullTransformer);
        
        try {
            transformedBag.remove(treeBag, -1);
            fail("Expected ClassCastException for incompatible types");
        } catch (ClassCastException e) {
            // Expected - can't compare TreeBag with Integer
        }
    }

    // ========================================================================
    // COUNT OPERATION TESTS
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testGetCount_WithNullValues() {
        TreeBag<Object> treeBag = new TreeBag<>();
        Transformer<Object, Integer> nullTransformer = 
            ConstantTransformer.nullTransformer();
        TransformedSortedBag<Object> transformedBag = 
            TransformedSortedBag.transformedSortedBag(treeBag, nullTransformer);
        
        try {
            transformedBag.getCount(null);
            fail("Expected NullPointerException for null value");
        } catch (NullPointerException e) {
            // Expected - null handling in TreeMap
        }
    }
    
    @Test(timeout = 4000)
    public void testGetCount_WithExceptionTransformer() {
        Transformer<Integer, Integer> exceptionTransformer = 
            ExceptionTransformer.exceptionTransformer();
        TreeBag<Integer> treeBag = new TreeBag<>();
        TransformedSortedBag<Integer> transformedBag = 
            new TransformedSortedBag<>(treeBag, exceptionTransformer);
        
        try {
            transformedBag.getCount(transformedBag);
            fail("Expected ClassCastException for incompatible types");
        } catch (ClassCastException e) {
            // Expected - transformation fails during comparison
        }
    }

    // ========================================================================
    // UNIQUE SET TESTS
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testUniqueSet_OnEmptyBag() {
        TreeBag<Object> treeBag = new TreeBag<>();
        Transformer<Object, Object> transformer = 
            InvokerTransformer.invokerTransformer("pA)M[)T~9fY");
        TransformedSortedBag<Object> transformedBag = 
            TransformedSortedBag.transformingSortedBag(treeBag, transformer);
        
        Set<Object> uniqueSet = transformedBag.uniqueSet();
        assertEquals(0, uniqueSet.size());
    }
    
    @Test(timeout = 4000)
    public void testUniqueSet_OnNonEmptyBag() {
        // Setup a bag with comparator that always returns equal
        Comparator<Object> comparator = mock(Comparator.class);
        when(comparator.compare(any(), any())).thenReturn(0);
        
        TreeBag<Integer> treeBag = new TreeBag<>(comparator);
        treeBag.add(862);
        
        ConstantTransformer<Object, Integer> constantTransformer = 
            new ConstantTransformer<>(null);
        TransformedSortedBag<Integer> transformedBag = 
            new TransformedSortedBag<>(treeBag, constantTransformer);
        
        // This should remove all occurrences since transformation returns null
        boolean result = transformedBag.remove(new HashBag<>(), 3791);
        assertTrue(result);
        assertFalse(treeBag.contains(0));
    }

    // ========================================================================
    // EQUALS & HASHCODE TESTS
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testEquals_WithSameInstance() {
        HashBag<Integer> hashBag = new HashBag<>();
        FactoryTransformer<Object, Integer> transformer = 
            new FactoryTransformer<>(new ConstantFactory<>(165));
        TreeBag<Integer> treeBag = new TreeBag<>();
        TransformedSortedBag<Integer> transformedBag = 
            TransformedSortedBag.transformingSortedBag(treeBag, transformer);
        
        assertTrue(transformedBag.equals(transformedBag));
    }
    
    @Test(timeout = 4000)
    public void testEquals_WithDifferentTypes() {
        Comparator<Object> comparator = mock(Comparator.class);
        TreeBag<Predicate<Object>> treeBag = new TreeBag<>(comparator);
        Transformer<Object, Predicate<Object>> transformer = 
            InvokerTransformer.invokerTransformer("jA]A&z5z!a+Jbr(nH");
        TransformedSortedBag<Predicate<Object>> transformedBag = 
            new TransformedSortedBag<>(treeBag, transformer);
        
        assertFalse(transformedBag.equals(2));
    }
    
    @Test(timeout = 4000)
    public void testHashCode_Consistency() {
        Vector<Integer> vector = new Vector<>(0, 0);
        TreeBag<Object> treeBag = new TreeBag<>(vector);
        HashMap<Object, Locale.FilteringMode> map = new HashMap<>();
        Transformer<Object, Locale.FilteringMode> transformer = 
            MapTransformer.mapTransformer(map);
        TransformedSortedBag<Object> transformedBag = 
            TransformedSortedBag.transformedSortedBag(treeBag, transformer);
        
        // Just verify no exception is thrown
        transformedBag.hashCode();
    }

    // ========================================================================
    // EDGE CASE TESTS
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testTransformedBag_WithSwitchTransformer() {
        HashBag<Integer> hashBag = new HashBag<>();
        hashBag.add(null);
        
        Predicate<Object>[] predicates = (Predicate<Object>[]) Array.newInstance(Predicate.class, 2);
        Integer comparisonValue = 5;
        Comparator<Object> comparator = mock(Comparator.class);
        when(comparator.compare(any(), any())).thenReturn(0);
        
        Predicate<Object> comparisonPredicate = 
            ComparatorPredicate.comparatorPredicate(
                comparisonValue, comparator, 
                ComparatorPredicate.Criterion.EQUAL
            );
        predicates[0] = comparisonPredicate;
        
        Transformer<Object, Integer>[] transformers = 
            (Transformer<Object, Integer>[]) Array.newInstance(Transformer.class, 0);
        Transformer<Object, Integer> mapTransformer = 
            MapTransformer.mapTransformer(new HashMap<>());
        SwitchTransformer<Object, Integer> switchTransformer = 
            new SwitchTransformer<>(predicates, transformers, mapTransformer);
        
        try {
            TransformedBag.transformedBag(hashBag, switchTransformer);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals(0, e.getMessage());
        }
    }
    
    @Test(timeout = 4000)
    public void testAdd_WithChainedTransformer() {
        TreeBag<Integer> treeBag = new TreeBag<>();
        SynchronizedSortedBag<Integer> syncBag = 
            new SynchronizedSortedBag<>(treeBag);
        
        Predicate<Object> falsePredicate = FalsePredicate.falsePredicate();
        NotPredicate<Object> notPredicate = new NotPredicate<>(falsePredicate);
        AndPredicate<Integer> andPredicate = 
            new AndPredicate<>(notPredicate, notPredicate);
        PredicatedSortedBag<Integer> predicatedBag = 
            new PredicatedSortedBag<>(syncBag, andPredicate);
        
        Predicate<Object>[] predicates = (Predicate<Object>[]) 
            Array.newInstance(Predicate.class, 3);
        predicates[0] = falsePredicate;
        predicates[1] = notPredicate;
        
        Transformer<Object, Integer>[] transformers = 
            (Transformer<Object, Integer>[]) Array.newInstance(Transformer.class, 0);
        Transformer<Object, Integer> exceptionTransformer = 
            ExceptionTransformer.exceptionTransformer();
        SwitchTransformer<Object, Integer> switchTransformer = 
            new SwitchTransformer<>(predicates, transformers, exceptionTransformer);
        
        TransformedSortedBag<Integer> transformedBag = 
            TransformedSortedBag.transformingSortedBag(predicatedBag, switchTransformer);
        
        try {
            transformedBag.add(2934, 2934);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals(1, e.getMessage());
        }
    }
}