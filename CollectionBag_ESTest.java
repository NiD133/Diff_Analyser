import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.CollectionBag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bag.UnmodifiableBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test suite for {@link CollectionBag}, focusing on its adherence to the
 * {@link java.util.Collection} contract.
 */
@DisplayName("CollectionBag Test")
class CollectionBagTest {

    private Bag<String> baseBag;
    private Bag<String> collectionBag;

    @BeforeEach
    void setUp() {
        // Arrange: Use a simple HashBag as the decorated bag for most tests.
        baseBag = new HashBag<>(Arrays.asList("one", "two", "two", "three", "three", "three"));
        collectionBag = CollectionBag.collectionBag(baseBag);
    }

    @Nested
    @DisplayName("Factory and Constructor")
    class FactoryAndConstructor {

        @Test
        @DisplayName("Factory method should throw NullPointerException for null bag")
        void factory_withNullBag_throwsNullPointerException() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> CollectionBag.collectionBag(null));
        }

        @Test
        @DisplayName("Constructor should throw NullPointerException for null bag")
        void constructor_withNullBag_throwsNullPointerException() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> new CollectionBag<>(null));
        }
    }

    @Nested
    @DisplayName("add() method")
    class Add {

        @Test
        @DisplayName("Should always return true and add one instance of the element")
        void add_shouldAlwaysReturnTrueAndAddOneInstance() {
            // Arrange
            int initialCount = collectionBag.getCount("new");
            int initialSize = collectionBag.size();

            // Act
            boolean result = collectionBag.add("new");

            // Assert
            assertTrue(result, "add(e) should always return true as per Collection contract");
            assertAll(
                () -> assertEquals(initialCount + 1, collectionBag.getCount("new")),
                () -> assertEquals(initialSize + 1, collectionBag.size())
            );
        }

        @Test
        @DisplayName("add(e, count) should always return true and add multiple instances")
        void addWithCount_shouldAlwaysReturnTrueAndAddInstances() {
            // Arrange
            int initialCount = collectionBag.getCount("new");
            int initialSize = collectionBag.size();
            int countToAdd = 5;

            // Act
            boolean result = collectionBag.add("new", countToAdd);

            // Assert
            assertTrue(result, "add(e, count) should always return true");
            assertAll(
                () -> assertEquals(initialCount + countToAdd, collectionBag.getCount("new")),
                () -> assertEquals(initialSize + countToAdd, collectionBag.size())
            );
        }
    }

    @Nested
    @DisplayName("remove() method")
    class Remove {

        @Test
        @DisplayName("Should remove only one instance of an existing element")
        void remove_whenElementExists_shouldRemoveOneInstance() {
            // Arrange
            int initialCount = collectionBag.getCount("two"); // Initially 2
            int initialSize = collectionBag.size();

            // Act
            boolean result = collectionBag.remove("two");

            // Assert
            assertTrue(result, "Should return true when an element is removed");
            assertAll(
                () -> assertEquals(initialCount - 1, collectionBag.getCount("two")),
                () -> assertEquals(initialSize - 1, collectionBag.size())
            );
        }

        @Test
        @DisplayName("Should return false if the element does not exist")
        void remove_whenElementDoesNotExist_shouldReturnFalse() {
            // Act
            boolean result = collectionBag.remove("nonexistent");

            // Assert
            assertFalse(result, "Should return false for a non-existent element");
            assertEquals(6, collectionBag.size());
        }
    }



    @Nested
    @DisplayName("containsAll() method")
    class ContainsAll {

        @Test
        @DisplayName("Should not respect cardinality and return true if all unique elements are present")
        void containsAll_shouldNotRespectCardinality() {
            // Arrange: collectionBag has {"one":1, "two":2}. We check for {"one":1, "two":1}.
            Bag<String> subset = new HashBag<>(Arrays.asList("one", "two"));

            // Act & Assert
            assertTrue(collectionBag.containsAll(subset),
                "Should return true as all unique elements in the subset exist in the bag");
        }

        @Test
        @DisplayName("Should return false if any element is missing")
        void containsAll_shouldReturnFalseIfElementMissing() {
            // Arrange
            Bag<String> superset = new HashBag<>(Arrays.asList("one", "four"));

            // Act & Assert
            assertFalse(collectionBag.containsAll(superset),
                "Should return false as 'four' is not in the bag");
        }

        @Test
        @DisplayName("Should return true for an empty collection")
        void containsAll_withEmptyCollection_shouldReturnTrue() {
            // Act & Assert
            assertTrue(collectionBag.containsAll(Collections.emptyList()));
        }
    }

    @Nested
    @DisplayName("removeAll() method")
    class RemoveAll {

        @Test
        @DisplayName("Should not respect cardinality and remove all instances of specified elements")
        void removeAll_shouldNotRespectCardinality() {
            // Arrange: collectionBag has {"two":2, "three":3}. We remove {"two", "three"}.
            Bag<String> toRemove = new HashBag<>(Arrays.asList("two", "three"));

            // Act
            boolean result = collectionBag.removeAll(toRemove);

            // Assert
            assertTrue(result, "Should return true as the collection was modified");
            assertAll(
                () -> assertEquals(0, collectionBag.getCount("two")),
                () -> assertEquals(0, collectionBag.getCount("three")),
                () -> assertEquals(1, collectionBag.getCount("one")), // "one" should remain
                () -> assertEquals(1, collectionBag.size())
            );
        }

        @Test
        @DisplayName("Should return false if no elements are removed")
        void removeAll_withDisjointCollection_shouldReturnFalse() {
            // Arrange
            Bag<String> toRemove = new HashBag<>(Arrays.asList("four", "five"));

            // Act
            boolean result = collectionBag.removeAll(toRemove);

            // Assert
            assertFalse(result, "Should return false as no elements were removed");
            assertEquals(6, collectionBag.size());
        }
    }

    @Nested
    @DisplayName("retainAll() method")
    class RetainAll {

        @Test
        @DisplayName("Should not respect cardinality and retain all instances of specified elements")
        void retainAll_shouldNotRespectCardinality() {
            // Arrange: collectionBag has {"one":1, "two":2, "three":3}. We retain {"one", "three"}.
            Bag<String> toRetain = new HashBag<>(Arrays.asList("one", "three"));

            // Act
            boolean result = collectionBag.retainAll(toRetain);

            // Assert
            assertTrue(result, "Should return true as the collection was modified");
            assertAll(
                () -> assertEquals(1, collectionBag.getCount("one")),
                () -> assertEquals(3, collectionBag.getCount("three")),
                () -> assertEquals(0, collectionBag.getCount("two")), // "two" should be removed
                () -> assertEquals(4, collectionBag.size())
            );
        }

        @Test
        @DisplayName("Should return false if the collection is not modified")
        void retainAll_whenNoChange_shouldReturnFalse() {
            // Arrange
            Bag<String> toRetain = new HashBag<>(Arrays.asList("one", "two", "three", "four"));

            // Act
            boolean result = collectionBag.retainAll(toRetain);

            // Assert
            assertFalse(result, "Should return false as no elements were removed");
            assertEquals(6, collectionBag.size());
        }
    }

    @Nested
    @DisplayName("Delegation Behavior")
    class Delegation {

        @Test
        @DisplayName("Should delegate calls to the wrapped bag, propagating exceptions")
        void modificationOnUnmodifiableBag_shouldThrowException() {
            // Arrange: Decorate an unmodifiable bag
            Bag<String> unmodifiableBase = UnmodifiableBag.unmodifiableBag(baseBag);
            Bag<String> unmodifiableCollectionBag = CollectionBag.collectionBag(unmodifiableBase);

            // Act & Assert
            assertAll(
                () -> assertThrows(UnsupportedOperationException.class, () -> unmodifiableCollectionBag.add("four")),
                () -> assertThrows(UnsupportedOperationException.class, () -> unmodifiableCollectionBag.remove("one")),
                () -> assertThrows(UnsupportedOperationException.class, () -> unmodifiableCollectionBag.clear())
            );
        }
    }
}