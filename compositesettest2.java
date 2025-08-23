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

public class CompositeSetTestTest2<E> extends AbstractSetTest<E> {

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

    @Test
    @SuppressWarnings("unchecked")
    void testAddCompositedCollision() {
        final HashSet<E> set1 = new HashSet<>();
        set1.add((E) "1");
        set1.add((E) "2");
        set1.add((E) "3");
        final HashSet<E> set2 = new HashSet<>();
        set2.add((E) "4");
        final CompositeSet<E> set3 = new CompositeSet<>(set1);
        assertThrows(UnsupportedOperationException.class, () -> set3.addComposited(set1, buildOne()), "Expecting UnsupportedOperationException.");
        assertThrows(UnsupportedOperationException.class, () -> set3.addComposited(set1, buildOne(), buildTwo()), "Expecting UnsupportedOperationException.");
    }
}
