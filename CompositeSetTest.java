package org.apache.commons.collections4.set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.set.CompositeSet.SetMutator;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CompositeSet}.
 * 
 * The tests favor clarity by:
 * - Using concrete type parameters (String) instead of generics with casts.
 * - Using descriptive method and variable names.
 * - Separating scenarios into focused test methods with comments.
 */
public class CompositeSetTest extends AbstractSetTest<String> {

    // Test data
    private static final String A = "1";
    private static final String B = "2";
    private static final String C = "3";
    private static final String D = "4";

    private static HashSet<String> newHashSet(final String... values) {
        final HashSet<String> set = new HashSet<>();
        Collections.addAll(set, values);
        return set;
    }

    private Set<String> buildOne() {
        return newHashSet(A, B);
    }

    private Set<String> buildTwo() {
        return newHashSet(C, D);
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
    public CompositeSet<String> makeObject() {
        final HashSet<String> contained = new HashSet<>();
        final CompositeSet<String> set = new CompositeSet<>(contained);
        set.setMutator(new EmptySetMutator<>(contained));
        return set;
    }

    @Test
    void testAddComposited_IgnoresNullArgsAndVarargs() {
        final Set<String> one = buildOne();
        final Set<String> two = buildTwo();

        final CompositeSet<String> composite = new CompositeSet<>();
        composite.addComposited(one, two);
        // These calls should be no-ops
        composite.addComposited((Set<String>) null);
        composite.addComposited((Set<String>[]) null);
        composite.addComposited(null, null);
        composite.addComposited(null, null, null);

        final CompositeSet<String> expected = new CompositeSet<>(buildOne());
        expected.addComposited(buildTwo());

        assertEquals(expected, composite);
    }

    @Test
    void testAddComposited_EqualsRegardlessOfCompositionShape() {
        // Composite of [1,2] and [3,4]
        final CompositeSet<String> composite = new CompositeSet<>(buildOne(), buildTwo());

        // Single set containing [1,2,3] composited with set [4]
        final HashSet<String> combined = newHashSet(A, B, C);
        final HashSet<String> last = newHashSet(D);
        final CompositeSet<String> differentlyComposed = new CompositeSet<>(combined);
        differentlyComposed.addComposited(last);

        assertEquals(composite, differentlyComposed);
    }

    @Test
    void testAddComposited_ThrowsWhenOverlappingSetIsAddedWithoutMutator() {
        final CompositeSet<String> composite = new CompositeSet<>(buildOne(), buildTwo());
        // Overlaps with existing sets (contains 1 and 2)
        final HashSet<String> overlapping = newHashSet(A, B, C);

        assertThrows(UnsupportedOperationException.class,
                () -> composite.addComposited(overlapping),
                "Adding an overlapping set without a mutator should be unsupported");
    }

    @Test
    void testAddCompositedCollision_NoMutatorWithExplicitParams_Throws() {
        final HashSet<String> existing = newHashSet(A, B, C);
        final CompositeSet<String> composite = new CompositeSet<>(existing);

        // Both of these attempts include collisions with 'existing'
        assertThrows(UnsupportedOperationException.class,
                () -> composite.addComposited(existing, buildOne()),
                "Expecting UnsupportedOperationException for collision with no mutator");

        assertThrows(UnsupportedOperationException.class,
                () -> composite.addComposited(existing, buildOne(), buildTwo()),
                "Expecting UnsupportedOperationException for collision with no mutator");
    }

    @Test
    void testContains() {
        final CompositeSet<String> composite = new CompositeSet<>(buildOne(), buildTwo());
        assertTrue(composite.contains(A));
    }

    @Test
    void testContainsAll_WithNull_ReturnsFalse() {
        final CompositeSet<String> composite = new CompositeSet<>(buildOne(), buildTwo());
        assertFalse(composite.containsAll(null));
    }

    @Test
    void testAddComposited_FailedCollisionResolution_ThrowsIllegalArgumentException() {
        final Set<String> one = buildOne();
        final Set<String> two = buildTwo();
        final CompositeSet<String> composite = new CompositeSet<>(one, two);

        // Mutator does not resolve collisions (resolveCollision is a no-op)
        composite.setMutator(new SetMutator<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean add(final CompositeSet<String> c, final List<Set<String>> sets, final String obj) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean addAll(final CompositeSet<String> c, final List<Set<String>> sets,
                                  final Collection<? extends String> coll) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void resolveCollision(final CompositeSet<String> c, final Set<String> existing,
                                         final Set<String> added, final Collection<String> intersects) {
                // Intentionally do nothing to simulate a failed resolution
            }
        });

        final HashSet<String> overlapping = newHashSet(A);

        assertThrows(IllegalArgumentException.class,
                () -> composite.addComposited(overlapping),
                "When a mutator fails to resolve collisions, an IllegalArgumentException is expected");
    }

    @Test
    void testRemoveAll_WithNull_ReturnsFalse() {
        final CompositeSet<String> composite = new CompositeSet<>(buildOne(), buildTwo());
        assertFalse(composite.removeAll(null));
    }

    @Test
    void testRemove_DelegatesToUnderlyingSetHoldingTheElement() {
        final Set<String> one = buildOne();
        final Set<String> two = buildTwo();
        final CompositeSet<String> composite = new CompositeSet<>(one, two);

        // Remove from first underlying set
        composite.remove(A);
        assertFalse(one.contains(A));

        // Remove from second underlying set
        composite.remove(C);
        assertFalse(two.contains(C));
    }

    @Test
    void testReflectsUnderlyingSetRemovals() {
        final Set<String> one = buildOne();
        final Set<String> two = buildTwo();
        final CompositeSet<String> composite = new CompositeSet<>(one, two);

        one.remove(A);
        assertFalse(composite.contains(A));

        two.remove(C);
        assertFalse(composite.contains(C));
    }

//    Helper to (re)create serialized forms if needed.
//    void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) getCollection(), "src/test/resources/data/test/CompositeSet.emptyCollection.version4.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) getCollection(), "src/test/resources/data/test/CompositeSet.fullCollection.version4.obj");
//    }
}