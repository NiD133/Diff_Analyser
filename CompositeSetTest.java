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

    private Set<E> createSetWithElements(E... elements) {
        final HashSet<E> set = new HashSet<>();
        for (E element : elements) {
            set.add(element);
        }
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
        final CompositeSet<E> set = new CompositeSet<>(contained);
        set.setMutator(new EmptySetMutator<>(contained));
        return set;
    }

    @Test
    void testAddCompositedSets() {
        final Set<E> setOne = createSetWithElements((E) "1", (E) "2");
        final Set<E> setTwo = createSetWithElements((E) "3", (E) "4");
        final CompositeSet<E> compositeSet = new CompositeSet<>();
        compositeSet.addComposited(setOne, setTwo);

        // Test adding null sets
        compositeSet.addComposited((Set<E>) null);
        compositeSet.addComposited((Set<E>[]) null);
        compositeSet.addComposited(null, null);
        compositeSet.addComposited(null, null, null);

        // Verify composite set equality
        final CompositeSet<E> expectedSet = new CompositeSet<>(createSetWithElements((E) "1", (E) "2"));
        expectedSet.addComposited(createSetWithElements((E) "3", (E) "4"));
        assertEquals(expectedSet, compositeSet);

        // Test unsupported operation
        final HashSet<E> additionalSet = createSetWithElements((E) "1", (E) "2", (E) "3");
        assertThrows(UnsupportedOperationException.class, () -> compositeSet.addComposited(additionalSet));
    }

    @Test
    void testAddCompositedWithCollision() {
        final HashSet<E> setOne = createSetWithElements((E) "1", (E) "2", (E) "3");
        final HashSet<E> setTwo = createSetWithElements((E) "4");
        final CompositeSet<E> compositeSet = new CompositeSet<>(setOne);

        // Test collision scenarios
        assertThrows(UnsupportedOperationException.class, () -> compositeSet.addComposited(setOne, createSetWithElements((E) "1", (E) "2")));
        assertThrows(UnsupportedOperationException.class, () -> compositeSet.addComposited(setOne, createSetWithElements((E) "1", (E) "2"), createSetWithElements((E) "3", (E) "4")));
    }

    @Test
    void testContainsElement() {
        final CompositeSet<E> compositeSet = new CompositeSet<>(createSetWithElements((E) "1", (E) "2"), createSetWithElements((E) "3", (E) "4"));
        assertTrue(compositeSet.contains("1"));
    }

    @Test
    void testContainsAllElements() {
        final CompositeSet<E> compositeSet = new CompositeSet<>(createSetWithElements((E) "1", (E) "2"), createSetWithElements((E) "3", (E) "4"));
        assertFalse(compositeSet.containsAll(null));
    }

    @Test
    void testFailedCollisionResolution() {
        final Set<E> setOne = createSetWithElements((E) "1", (E) "2");
        final Set<E> setTwo = createSetWithElements((E) "3", (E) "4");
        final CompositeSet<E> compositeSet = new CompositeSet<>(setOne, setTwo);

        compositeSet.setMutator(new SetMutator<E>() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean add(final CompositeSet<E> composite, final List<Set<E>> collections, final E obj) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean addAll(final CompositeSet<E> composite, final List<Set<E>> collections, final Collection<? extends E> coll) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void resolveCollision(final CompositeSet<E> comp, final Set<E> existing, final Set<E> added, final Collection<E> intersects) {
                // No operation
            }
        });

        final HashSet<E> conflictingSet = createSetWithElements((E) "1");
        assertThrows(IllegalArgumentException.class, () -> compositeSet.addComposited(conflictingSet));
    }

    @Test
    void testRemoveAllElements() {
        final CompositeSet<E> compositeSet = new CompositeSet<>(createSetWithElements((E) "1", (E) "2"), createSetWithElements((E) "3", (E) "4"));
        assertFalse(compositeSet.removeAll(null));
    }

    @Test
    void testRemoveCompositedElement() {
        final Set<E> setOne = createSetWithElements((E) "1", (E) "2");
        final Set<E> setTwo = createSetWithElements((E) "3", (E) "4");
        final CompositeSet<E> compositeSet = new CompositeSet<>(setOne, setTwo);

        compositeSet.remove("1");
        assertFalse(setOne.contains("1"));

        compositeSet.remove("3");
        assertFalse(setOne.contains("3"));
    }

    @Test
    void testRemoveUnderlyingElement() {
        final Set<E> setOne = createSetWithElements((E) "1", (E) "2");
        final Set<E> setTwo = createSetWithElements((E) "3", (E) "4");
        final CompositeSet<E> compositeSet = new CompositeSet<>(setOne, setTwo);

        setOne.remove("1");
        assertFalse(compositeSet.contains("1"));

        setTwo.remove("3");
        assertFalse(compositeSet.contains("3"));
    }
}