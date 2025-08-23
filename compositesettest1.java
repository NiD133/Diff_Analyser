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

public class CompositeSetTestTest1<E> extends AbstractSetTest<E> {

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
    void testAddComposited() {
        final Set<E> one = buildOne();
        final Set<E> two = buildTwo();
        final CompositeSet<E> set = new CompositeSet<>();
        set.addComposited(one, two);
        set.addComposited((Set<E>) null);
        set.addComposited((Set<E>[]) null);
        set.addComposited(null, null);
        set.addComposited(null, null, null);
        final CompositeSet<E> set2 = new CompositeSet<>(buildOne());
        set2.addComposited(buildTwo());
        assertEquals(set, set2);
        final HashSet<E> set3 = new HashSet<>();
        set3.add((E) "1");
        set3.add((E) "2");
        set3.add((E) "3");
        final HashSet<E> set4 = new HashSet<>();
        set4.add((E) "4");
        final CompositeSet<E> set5 = new CompositeSet<>(set3);
        set5.addComposited(set4);
        assertEquals(set, set5);
        assertThrows(UnsupportedOperationException.class, () -> set.addComposited(set3), "Expecting UnsupportedOperationException.");
    }
}
