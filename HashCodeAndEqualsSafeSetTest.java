/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Tests for HashCodeAndEqualsSafeSet.
 *
 * This set wraps elements to avoid calling their equals/hashCode.
 * We use a type whose equals/hashCode throw to verify the set stays usable.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Rule public MockitoRule r = MockitoJUnit.rule();

    // Mockito mock of a type whose equals/hashCode throw if invoked.
    @Mock private UnmockableHashCodeAndEquals failingMock;

    @Test
    public void should_add_element_even_if_hashCode_throws() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();

        boolean added = set.add(failingMock);

        assertThat(added).isTrue();
        // Use set.contains directly to avoid assert library invoking equals/hashCode
        assertThat(set.contains(failingMock)).isTrue();
    }

    @Test
    public void contains_works_even_if_equals_throws() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(failingMock);

        assertThat(set.contains(failingMock)).isTrue();

        // Different mock instance is not contained
        UnmockableHashCodeAndEquals other = mock(UnmockableHashCodeAndEquals.class);
        assertThat(set.contains(other)).isFalse();
    }

    @Test
    public void remove_removes_element() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(failingMock);

        boolean removed = set.remove(failingMock);

        assertThat(removed).isTrue();
        assertThat(set.isEmpty()).isTrue();
    }

    @Test
    public void addAll_adds_all_elements() {
        HashCodeAndEqualsSafeSet source = HashCodeAndEqualsSafeSet.of(failingMock, mock(Observer.class));
        HashCodeAndEqualsSafeSet target = new HashCodeAndEqualsSafeSet();

        boolean changed = target.addAll(source);

        assertThat(changed).isTrue();
        assertThat(target.containsAll(source)).isTrue();
    }

    @Test
    public void retainAll_keeps_only_specified_elements() {
        HashCodeAndEqualsSafeSet toRetain = HashCodeAndEqualsSafeSet.of(failingMock, mock(Observer.class));
        HashCodeAndEqualsSafeSet working = new HashCodeAndEqualsSafeSet();

        working.addAll(toRetain);
        working.add(mock(List.class)); // extra element

        boolean changed = working.retainAll(toRetain);

        assertThat(changed).isTrue();
        assertThat(working.containsAll(toRetain)).isTrue();
        assertThat(working.size()).isEqualTo(toRetain.size());
    }

    @Test
    public void removeAll_removes_specified_elements() {
        HashCodeAndEqualsSafeSet toRemove = HashCodeAndEqualsSafeSet.of(failingMock, mock(Observer.class));
        HashCodeAndEqualsSafeSet working = new HashCodeAndEqualsSafeSet();

        working.addAll(toRemove);
        working.add(mock(List.class)); // keep this one

        boolean changed = working.removeAll(toRemove);

        assertThat(changed).isTrue();
        assertThat(working.containsAll(toRemove)).isFalse();
        assertThat(working.size()).isEqualTo(1);
    }

    @Test
    public void iterator_visits_all_elements() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(failingMock, mock(Observer.class));

        List<Object> visited = new ArrayList<>();
        for (Object o : set) {
            visited.add(o);
        }

        assertThat(visited).isNotEmpty();
        assertThat(visited.size()).isEqualTo(set.size());
    }

    @Test
    public void toArray_returns_unwrapped_elements() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(failingMock);

        Object[] asObjects = set.toArray();
        assertThat(asObjects).hasSize(1);
        assertThat(asObjects[0]).isSameAs(failingMock);

        UnmockableHashCodeAndEquals[] typed = set.toArray(new UnmockableHashCodeAndEquals[0]);
        assertThat(typed).hasSize(1);
        assertThat(typed[0]).isSameAs(failingMock);
    }

    @Test
    public void cloneIsNotSupported() {
        assertThatThrownBy(() -> HashCodeAndEqualsSafeSet.of().clone())
                .isInstanceOf(CloneNotSupportedException.class);
    }

    @Test
    public void clear_makes_set_empty() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(failingMock);

        set.clear();

        assertThat(set).isEmpty();
    }

    @Test
    @SuppressWarnings("SelfAssertion") // https://github.com/google/error-prone/issues/5131
    public void equals_is_reflexive() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(failingMock);
        assertThat(set).isEqualTo(set);
    }

    @Test
    public void notEqual_to_different_collection_type() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of();
        assertThat(set).isNotEqualTo(new HashSet<Object>());
    }

    @Test
    public void notEqual_when_content_differs() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(failingMock);

        assertThat(set).isNotEqualTo(HashCodeAndEqualsSafeSet.of());
    }

    @Test
    public void hashCode_equal_when_content_equal() {
        HashCodeAndEqualsSafeSet a = HashCodeAndEqualsSafeSet.of(failingMock);
        HashCodeAndEqualsSafeSet b = HashCodeAndEqualsSafeSet.of(failingMock);

        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    public void toString_is_not_empty() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(failingMock);

        assertThat(set.toString()).isNotEmpty();
    }

    @Test
    public void iterator_remove_removes_current_element() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(failingMock);

        Iterator<Object> it = set.iterator();
        it.next();
        it.remove();

        assertThat(set).isEmpty();
    }

    /**
     * Class with equals/hashCode that always throw. We use Mockito to create mocks of this type
     * so any accidental call to equals/hashCode would fail the test unless properly guarded.
     */
    private static class UnmockableHashCodeAndEquals {
        @Override
        public final int hashCode() {
            throw new NullPointerException("I'm failing on hashCode and I don't care");
        }

        @Override
        public final boolean equals(Object obj) {
            throw new NullPointerException("I'm failing on equals and I don't care");
        }
    }
}