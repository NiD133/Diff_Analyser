package org.apache.commons.collections4.set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.set.CompositeSet.SetMutator;
import org.junit.jupiter.api.Test;

/**
 * Test suite for the {@link CompositeSet} implementation.
 */
public class CompositeSetTest<E> extends AbstractSetTest<E> {

    /**
     * Builds a set containing elements "1" and "2".
     */
    @SuppressWarnings("unchecked")
    public Set<E> buildSetOne() {
        final HashSet<E> set = new HashSet<>();
        set.add((E) "1");
        set.add((E) "2");
        return set;
    }

    /**
     * Builds a set containing elements "3" and "4".
     */
    @SuppressWarnings("unchecked")
    public Set<E> buildSetTwo() {
        final HashSet<E> set = new HashSet<>();
        set.add((E) "3");
        set.add((E) "4");
        return set;
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    @Override
    public CompositeSet<E> makeObject() {
        final HashSet<E> contained = new HashSet<>();
        final CompositeSet<E> compositeSet = new CompositeSet<>(contained);
        compositeSet.setMutator(new EmptySetMutator<>(contained));
        return compositeSet;
    }

    @Test
    @SuppressWarnings("unchecked")
    void testAddCompositedSets() {
        final Set<E> setOne = buildSetOne();
        final Set<E> setTwo = buildSetTwo();
        final CompositeSet<E> compositeSet = new CompositeSet<>();
        
        // Add sets to the composite
        compositeSet.addComposited(setOne, setTwo);
        
        // Test adding null sets
        compositeSet.addComposited((Set<E>) null);
        compositeSet.addComposited((Set<E>[]) null);
        compositeSet.addComposited(null, null);
        compositeSet.addComposited(null, null, null);
        
        // Create another composite set for comparison
        final CompositeSet<E> compositeSet2 = new CompositeSet<>(buildSetOne());
        compositeSet2.addComposited(buildSetTwo());
        
        // Assert equality of composite sets
        assertEquals(compositeSet, compositeSet2);
        
        // Test adding sets with elements
        final HashSet<E> setThree = new HashSet<>();
        setThree.add((E) "1");
        setThree.add((E) "2");
        setThree.add((E) "3");
        
        final HashSet<E> setFour = new HashSet<>();
        setFour.add((E) "4");
        
        final CompositeSet<E> compositeSet3 = new CompositeSet<>(setThree);
        compositeSet3.addComposited(setFour);
        
        // Assert equality of composite sets
        assertEquals(compositeSet, compositeSet3);
        
        // Test unsupported operation exception
        assertThrows(UnsupportedOperationException.class, () -> compositeSet.addComposited(setThree),
                "Expecting UnsupportedOperationException.");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testAddCompositedCollision() {
        final HashSet<E> setOne = new HashSet<>();
        setOne.add((E) "1");
        setOne.add((E) "2");
        setOne.add((E) "3");
        
        final HashSet<E> setTwo = new HashSet<>();
        setTwo.add((E) "4");
        
        final CompositeSet<E> compositeSet = new CompositeSet<>(setOne);
        
        // Test unsupported operation exception for collisions
        assertThrows(UnsupportedOperationException.class, () -> compositeSet.addComposited(setOne, buildSetOne()),
                "Expecting UnsupportedOperationException.");
        assertThrows(UnsupportedOperationException.class, () -> compositeSet.addComposited(setOne, buildSetOne(), buildSetTwo()),
                "Expecting UnsupportedOperationException.");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testContainsElement() {
        final CompositeSet<E> compositeSet = new CompositeSet<>(buildSetOne(), buildSetTwo());
        assertTrue(compositeSet.contains("1"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testContainsAllElements() {
        final CompositeSet<E> compositeSet = new CompositeSet<>(buildSetOne(), buildSetTwo());
        assertFalse(compositeSet.containsAll(null));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testFailedCollisionResolution() {
        final Set<E> setOne = buildSetOne();
        final Set<E> setTwo = buildSetTwo();
        final CompositeSet<E> compositeSet = new CompositeSet<>(setOne, setTwo);
        
        // Set a mutator that throws UnsupportedOperationException
        compositeSet.setMutator(new SetMutator<E>() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean add(final CompositeSet<E> composite,
                    final List<Set<E>> collections, final E obj) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean addAll(final CompositeSet<E> composite,
                    final List<Set<E>> collections, final Collection<? extends E> coll) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void resolveCollision(final CompositeSet<E> comp, final Set<E> existing,
                final Set<E> added, final Collection<E> intersects) {
                // No operation
            }
        });

        final HashSet<E> setThree = new HashSet<>();
        setThree.add((E) "1");
        
        // Test illegal argument exception for collision
        assertThrows(IllegalArgumentException.class, () -> compositeSet.addComposited(setThree),
                "IllegalArgumentException should have been thrown");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testRemoveAllElements() {
        final CompositeSet<E> compositeSet = new CompositeSet<>(buildSetOne(), buildSetTwo());
        assertFalse(compositeSet.removeAll(null));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testRemoveCompositedElement() {
        final Set<E> setOne = buildSetOne();
        final Set<E> setTwo = buildSetTwo();
        final CompositeSet<E> compositeSet = new CompositeSet<>(setOne, setTwo);
        
        // Remove elements and assert their absence
        compositeSet.remove("1");
        assertFalse(setOne.contains("1"));

        compositeSet.remove("3");
        assertFalse(setOne.contains("3"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testRemoveUnderlyingElement() {
        final Set<E> setOne = buildSetOne();
        final Set<E> setTwo = buildSetTwo();
        final CompositeSet<E> compositeSet = new CompositeSet<>(setOne, setTwo);
        
        // Remove elements from underlying sets and assert their absence
        setOne.remove("1");
        assertFalse(compositeSet.contains("1"));

        setTwo.remove("3");
        assertFalse(compositeSet.contains("3"));
    }
}