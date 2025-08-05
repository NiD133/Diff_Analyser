package org.mockito.internal.util.collections;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    // --- Test cases for add() and addAll() ---

    @Test
    public void add_shouldReturnTrueAndAddElement_whenElementIsNew() {
        // Given
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        Object element = "new element";

        // When
        boolean wasAdded = set.add(element);

        // Then
        assertThat(wasAdded).isTrue();
        assertThat(set).contains(element);
    }

    @Test
    public void add_shouldReturnFalse_whenElementAlreadyExists() {
        // Given
        Object element = "existing element";
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(element);

        // When
        boolean wasAdded = set.add(element);

        // Then
        assertThat(wasAdded).isFalse();
        assertThat(set).hasSize(1);
    }

    @Test
    public void addAll_shouldReturnTrueAndAddAllElements_whenSomeElementsAreNew() {
        // Given
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of("existing");
        Collection<Object> toAdd = List.of("existing", "new");

        // When
        boolean wasModified = set.addAll(toAdd);

        // Then
        assertThat(wasModified).isTrue();
        assertThat(set).containsExactlyInAnyOrder("existing", "new");
    }

    @Test
    public void addAll_shouldReturnFalse_whenAllElementsAlreadyExist() {
        // Given
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of("one", "two");
        Collection<Object> toAdd = List.of("one", "two");

        // When
        boolean wasModified = set.addAll(toAdd);

        // Then
        assertThat(wasModified).isFalse();
        assertThat(set).hasSize(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addAll_shouldThrowException_whenGivenNullCollection() {
        // Given
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();

        // When
        set.addAll(null); // Then: throws IllegalArgumentException
    }

    // --- Test cases for remove(), removeAll(), removeIf(), retainAll(), clear() ---

    @Test
    public void remove_shouldReturnTrueAndRemoveElement_whenElementExists() {
        // Given
        Object element = "element";
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(element);

        // When
        boolean wasRemoved = set.remove(element);

        // Then
        assertThat(wasRemoved).isTrue();
        assertThat(set).isEmpty();
    }

    @Test
    public void remove_shouldReturnFalse_whenElementDoesNotExist() {
        // Given
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of("some other element");

        // When
        boolean wasRemoved = set.remove("non-existent element");

        // Then
        assertThat(wasRemoved).isFalse();
        assertThat(set).hasSize(1);
    }

    @Test
    public void removeAll_shouldModifySetAndReturnTrue_whenElementsAreRemoved() {
        // Given
        Object elementToKeep = "keep";
        Object elementToRemove = "remove";
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(elementToKeep, elementToRemove);
        Collection<Object> toRemove = List.of(elementToRemove, "another element");

        // When
        boolean wasModified = set.removeAll(toRemove);

        // Then
        assertThat(wasModified).isTrue();
        assertThat(set).containsExactly(elementToKeep);
    }

    @Test
    public void retainAll_shouldModifySetAndReturnTrue_whenElementsAreRemoved() {
        // Given
        Object elementToKeep = "keep";
        Object elementToRemove = "remove";
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(elementToKeep, elementToRemove);
        Collection<Object> toRetain = List.of(elementToKeep);

        // When
        boolean wasModified = set.retainAll(toRetain);

        // Then
        assertThat(wasModified).isTrue();
        assertThat(set).containsExactly(elementToKeep);
    }

    @Test
    public void removeIf_shouldRemoveMatchingElementsAndReturnTrue() {
        // Given
        String toRemove = "remove_me";
        String toKeep = "keep_me";
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(toRemove, toKeep);
        Predicate<Object> filter = (obj) -> obj.equals(toRemove);

        // When
        boolean wasModified = set.removeIf(filter);

        // Then
        assertThat(wasModified).isTrue();
        assertThat(set).containsExactly(toKeep);
    }

    @Test
    public void clear_shouldRemoveAllElements() {
        // Given
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of("one", "two");
        assertThat(set.isEmpty()).isFalse();

        // When
        set.clear();

        // Then
        assertThat(set.isEmpty()).isTrue();
        assertThat(set.size()).isZero();
    }

    // --- Test cases for contains() and containsAll() ---

    @Test
    public void contains_shouldReturnTrue_whenElementExists() {
        // Given
        Object element = "element";
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(element);

        // Then
        assertThat(set.contains(element)).isTrue();
    }

    @Test
    public void contains_shouldReturnFalse_whenElementDoesNotExist() {
        // Given
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of("some other element");

        // Then
        assertThat(set.contains("non-existent element")).isFalse();
    }

    @Test
    public void containsAll_shouldReturnTrue_whenAllElementsExist() {
        // Given
        Object element1 = "one";
        Object element2 = "two";
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(element1, element2, "three");
        Collection<Object> toCheck = List.of(element1, element2);

        // When
        boolean result = set.containsAll(toCheck);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    public void containsAll_shouldReturnFalse_whenSomeElementsAreMissing() {
        // Given
        Object element1 = "one";
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(element1);
        Collection<Object> toCheck = List.of(element1, "missing element");

        // When
        boolean result = set.containsAll(toCheck);

        // Then
        assertThat(result).isFalse();
    }

    // --- Test cases for size() and isEmpty() ---

    @Test
    public void size_shouldReturnZero_forNewSet() {
        // Given
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();

        // Then
        assertThat(set.size()).isZero();
    }

    @Test
    public void size_shouldReturnCorrectCount_afterAddingElements() {
        // Given
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add("element 1");
        set.add("element 2");

        // Then
        assertThat(set.size()).isEqualTo(2);
    }

    @Test
    public void isEmpty_shouldReturnTrue_forNewSet() {
        // Given
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();

        // Then
        assertThat(set.isEmpty()).isTrue();
    }

    @Test
    public void isEmpty_shouldReturnFalse_forNonEmptySet() {
        // Given
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of("element");

        // Then
        assertThat(set.isEmpty()).isFalse();
    }

    // --- Test cases for toArray() ---

    @Test
    public void toArray_shouldReturnArrayContainingAllElements() {
        // Given
        Object element1 = new Object();
        Object element2 = new Object();
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(element1, element2);

        // When
        Object[] result = set.toArray();

        // Then
        assertThat(result).containsExactlyInAnyOrder(element1, element2);
    }

    @Test
    public void toArrayWithArgument_shouldReturnNewArray_whenGivenArrayIsTooSmall() {
        // Given
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of("one", "two");
        String[] smallArray = new String[1];

        // When
        String[] result = set.toArray(smallArray);

        // Then
        assertThat(result).isNotSameAs(smallArray);
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder("one", "two");
    }

    @Test
    public void toArrayWithArgument_shouldUseGivenArray_whenItIsLargeEnough() {
        // Given
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of("one");
        String[] largeArray = new String[5];

        // When
        String[] result = set.toArray(largeArray);

        // Then
        assertThat(result).isSameAs(largeArray);
        assertThat(result[0]).isEqualTo("one");
        assertThat(result[1]).isNull(); // Should be null-terminated
    }

    @Test(expected = ArrayStoreException.class)
    public void toArrayWithArgument_shouldThrowArrayStoreException_forIncorrectArrayType() {
        // Given
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of("a string");
        Integer[] wrongTypeArray = new Integer[1];

        // When
        set.toArray(wrongTypeArray); // Then: throws ArrayStoreException
    }

    // --- Test cases for iterator() ---

    @Test
    public void iterator_shouldAllowIteratingOverElements() {
        // Given
        Object element1 = "one";
        Object element2 = "two";
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(element1, element2);

        // When
        Iterator<Object> iterator = set.iterator();

        // Then
        List<Object> iteratedElements = new ArrayList<>();
        iterator.forEachRemaining(iteratedElements::add);
        assertThat(iteratedElements).containsExactlyInAnyOrder(element1, element2);
    }

    // --- Test cases for equals(), hashCode(), and toString() ---

    @Test
    public void equals_shouldReturnTrue_forTwoSetsWithSameElements() {
        // Given
        Object element1 = new Object();
        Object element2 = new Object();
        HashCodeAndEqualsSafeSet set1 = HashCodeAndEqualsSafeSet.of(element1, element2);
        HashCodeAndEqualsSafeSet set2 = HashCodeAndEqualsSafeSet.of(element2, element1);

        // Then
        assertThat(set1).isEqualTo(set2);
    }

    @Test
    public void hashCode_shouldBeEqual_forEqualSets() {
        // Given
        HashCodeAndEqualsSafeSet set1 = HashCodeAndEqualsSafeSet.of("one", "two");
        HashCodeAndEqualsSafeSet set2 = HashCodeAndEqualsSafeSet.of("two", "one");

        // Then
        assertThat(set1.hashCode()).isEqualTo(set2.hashCode());
    }

    @Test
    public void toString_shouldReturnStringRepresentationOfElements() {
        // Given
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of("one", 2);

        // When
        String result = set.toString();

        // Then
        assertThat(result).startsWith("[").endsWith("]").contains("one", "2", ", ");
    }

    // --- Test cases for static factory of() ---

    @Test
    public void of_shouldCreateSetWithGivenElements() {
        // Given
        Object element1 = new Object();
        Object element2 = new Object();

        // When
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(element1, element2);

        // Then
        assertThat(set).containsExactlyInAnyOrder(element1, element2);
    }

    @Test
    public void ofIterable_shouldCreateSetFromIterable() {
        // Given
        List<Object> source = List.of(new Object(), new Object());

        // When
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(source);

        // Then
        assertThat(set).containsExactlyInAnyOrderElementsOf(source);
    }

    @Test
    public void ofIterable_shouldCreateEmptySet_whenGivenNullIterable() {
        // When
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);

        // Then
        assertThat(set).isNotNull().isEmpty();
    }

    // --- Test cases for exceptional behavior and edge cases ---

    @Test(expected = CloneNotSupportedException.class)
    public void clone_shouldThrowCloneNotSupportedException() throws CloneNotSupportedException {
        // Given
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();

        // When
        set.clone(); // Then: throws CloneNotSupportedException
    }

    @Test(expected = NullPointerException.class)
    public void toString_shouldThrowNullPointerException_whenSetContainsNull() {
        // Given
        // The wrapper's toString calls toString on the wrapped object, which will NPE for null.
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(new Object(), null);

        // When
        set.toString(); // Then: throws NullPointerException
    }

    /**
     * This tests an internal precondition of the class. The implementation seems to assume
     * that collections passed to bulk operations do not contain duplicates, and it enforces
     * this with an assertion.
     */
    @Test
    public void bulkOperation_shouldThrowAssertionError_whenGivenCollectionHasDuplicates() {
        // Given
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        Object duplicateElement = new Object();
        List<Object> listWithDuplicates = List.of(duplicateElement, duplicateElement);

        // Then
        assertThatThrownBy(() -> set.addAll(listWithDuplicates))
            .isInstanceOf(AssertionError.class)
            .withMessage("WRONG");

        assertThatThrownBy(() -> set.removeAll(listWithDuplicates))
            .isInstanceOf(AssertionError.class)
            .withMessage("WRONG");

        assertThatThrownBy(() -> set.retainAll(listWithDuplicates))
            .isInstanceOf(AssertionError.class)
            .withMessage("WRONG");
    }
}