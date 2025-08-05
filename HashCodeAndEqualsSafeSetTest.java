/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class HashCodeAndEqualsSafeSetTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock private UnmockableHashCodeAndEquals mock1;

    @Test
    public void should_add_mock_with_failing_hashCode() {
        new HashCodeAndEqualsSafeSet().add(mock1);
    }

    @Test
    public void should_contain_mock_with_failing_equals_after_addition() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(mock1);

        assertThat(set.contains(mock1)).isTrue();
        assertThat(set.contains(mock(UnmockableHashCodeAndEquals.class))).isFalse();
    }

    @Test
    public void should_remove_mock() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(mock1);
        set.remove(mock1);

        assertThat(set).isEmpty();
    }

    @Test
    public void should_add_all_elements_from_collection() {
        HashCodeAndEqualsSafeSet sourceSet = HashCodeAndEqualsSafeSet.of(mock1, mock(Observer.class));
        HashCodeAndEqualsSafeSet targetSet = new HashCodeAndEqualsSafeSet();

        targetSet.addAll(sourceSet);

        assertThat(targetSet.containsAll(sourceSet)).isTrue();
    }

    @Test
    public void should_retain_only_specified_collection() {
        HashCodeAndEqualsSafeSet initialSet = HashCodeAndEqualsSafeSet.of(mock1, mock(Observer.class));
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.addAll(initialSet);
        set.add(mock(List.class));

        boolean changed = set.retainAll(initialSet);

        assertThat(changed).isTrue();
        assertThat(set).containsExactlyInAnyOrderElementsOf(initialSet);
    }

    @Test
    public void should_remove_all_elements_from_collection() {
        HashCodeAndEqualsSafeSet itemsToRemove = HashCodeAndEqualsSafeSet.of(mock1, mock(Observer.class));
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.addAll(itemsToRemove);
        set.add(mock(List.class));

        boolean changed = set.removeAll(itemsToRemove);

        assertThat(changed).isTrue();
        assertThat(set).doesNotContainAnyElementsOf(itemsToRemove);
    }

    @Test
    public void should_iterate_over_all_elements() {
        Object mock2 = mock(Observer.class);
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(mock1, mock2);

        Iterator<Object> iterator = set.iterator();
        assertThat(iterator.next()).isIn(mock1, mock2);
        assertThat(iterator.next()).isIn(mock1, mock2);
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void should_return_array_containing_elements() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(mock1);

        assertThat(set.toArray()).containsExactly(mock1);
    }

    @Test
    public void should_return_typed_array_containing_elements() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(mock1);

        UnmockableHashCodeAndEquals[] array = set.toArray(new UnmockableHashCodeAndEquals[0]);

        assertThat(array).containsExactly(mock1);
    }

    @Test
    public void should_throw_exception_on_clone() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of();

        assertThatThrownBy(set::clone)
                .isInstanceOf(CloneNotSupportedException.class);
    }

    @Test
    public void should_be_empty_after_clear() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(mock1);
        set.clear();

        assertThat(set).isEmpty();
    }

    @Test
    public void should_be_equal_to_itself() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(mock1);
        assertThat(set).isEqualTo(set);
    }

    @Test
    public void should_not_equal_different_set_implementation() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of();
        assertThat(set).isNotEqualTo(new HashSet<>());
    }

    @Test
    public void should_not_equal_set_with_different_content() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(mock1);
        assertThat(set).isNotEqualTo(HashCodeAndEqualsSafeSet.of());
    }

    @Test
    public void should_have_same_hashCode_for_equal_content() {
        HashCodeAndEqualsSafeSet set1 = HashCodeAndEqualsSafeSet.of(mock1);
        HashCodeAndEqualsSafeSet set2 = HashCodeAndEqualsSafeSet.of(mock1);

        assertThat(set1.hashCode()).isEqualTo(set2.hashCode());
    }

    @Test
    public void should_return_non_empty_string_from_toString() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(mock1);
        assertThat(set.toString()).isNotEmpty();
    }

    @Test
    public void should_remove_element_via_iterator() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(mock1);
        Iterator<Object> iterator = set.iterator();
        iterator.next();
        iterator.remove();

        assertThat(set).isEmpty();
    }

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