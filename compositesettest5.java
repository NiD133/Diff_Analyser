package org.apache.commons.collections4.set;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.set.CompositeSet.SetMutator;
import org.junit.jupiter.api.Test;

public class CompositeSetTestTest5<E> extends AbstractSetTest<E> {

    @SuppressWarnings("unchecked")
    public Set<E> buildOne() {
        final HashSet<E> set = new HashSet<>();
        set.add((E) "1");
        set.add((E) "2");
        return set;
    }

    @SuppressWarnings("unchecked")
    public Set<E> buildTwo() {
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
        final CompositeSet<E> set = new CompositeSet<>(contained);
        set.setMutator(new EmptySetMutator<>(contained));
        return set;
    }

    /**
     * Tests that addComposited throws an IllegalArgumentException if a collision
     * occurs and the configured SetMutator does not resolve it.
     */
    @Test
    @SuppressWarnings("unchecked")
    void addCompositedShouldThrowExceptionForCollidingSetWhenMutatorDoesNotResolve() {
        // Arrange
        final Set<E> set1 = buildOne(); // Contains "1", "2"
        final Set<E> set2 = buildTwo(); // Contains "3", "4"
        final CompositeSet<E> compositeSet = new CompositeSet<>(set1, set2);

        // Define a mutator that intentionally does nothing to resolve collisions.
        final SetMutator<E> noOpCollisionResolver = new SetMutator<E>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void resolveCollision(final CompositeSet<E> comp, final Set<E> existing,
                                         final Set<E> added, final Collection<E> intersects) {
                // This mutator intentionally leaves the collision unresolved.
            }

            @Override
            public boolean add(final CompositeSet<E> composite, final List<Set<E>> collections, final E obj) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean addAll(final CompositeSet<E> composite, final List<Set<E>> collections,
                                  final Collection<? extends E> coll) {
                throw new UnsupportedOperationException();
            }
        };
        compositeSet.setMutator(noOpCollisionResolver);

        // Create a new set that collides with an existing set in the composite ("1" is in set1).
        final Set<E> collidingSet = new HashSet<>();
        collidingSet.add((E) "1");

        // Act & Assert
        // An IllegalArgumentException is expected because the no-op mutator failed to
        // remove the intersecting element from the new set.
        assertThrows(IllegalArgumentException.class, () -> compositeSet.addComposited(collidingSet));
    }
}