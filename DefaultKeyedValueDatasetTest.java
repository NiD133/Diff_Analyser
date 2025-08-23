package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Readable, intention-revealing tests for DefaultKeyedValueDataset.
 */
@DisplayName("DefaultKeyedValueDataset")
public class DefaultKeyedValueDatasetTest {

    private static final String KEY_A = "Key A";
    private static final String KEY_B = "Key B";
    private static final double VALUE_1 = 45.5;
    private static final double VALUE_2 = 45.6;

    private static DefaultKeyedValueDataset dataset(String key, double value) {
        return new DefaultKeyedValueDataset(key, value);
    }

    // equals()

    @Test
    @DisplayName("equals(): two datasets with same key and value are equal (symmetry)")
    public void equals_sameState_true_andSymmetric() {
        // arrange
        DefaultKeyedValueDataset d1 = dataset(KEY_A, VALUE_1);
        DefaultKeyedValueDataset d2 = dataset(KEY_A, VALUE_1);

        // assert
        assertAll(
                () -> assertEquals(d1, d2, "Expected equality for same key/value"),
                () -> assertEquals(d2, d1, "Symmetry must hold")
        );
    }

    @Test
    @DisplayName("equals(): different key -> not equal")
    public void equals_differentKey_false() {
        // arrange
        DefaultKeyedValueDataset d1 = dataset(KEY_A, VALUE_1);
        DefaultKeyedValueDataset d2 = dataset(KEY_B, VALUE_1);

        // assert
        assertNotEquals(d1, d2, "Datasets with different keys must not be equal");
    }

    @Test
    @DisplayName("equals(): different value -> not equal")
    public void equals_differentValue_false() {
        // arrange
        DefaultKeyedValueDataset d1 = dataset(KEY_A, VALUE_1);
        DefaultKeyedValueDataset d2 = dataset(KEY_A, VALUE_2);

        // assert
        assertNotEquals(d1, d2, "Datasets with different values must not be equal");
    }

    @Test
    @DisplayName("equals(): null and different type -> not equal")
    public void equals_nullAndDifferentType_false() {
        // arrange
        DefaultKeyedValueDataset d1 = dataset(KEY_A, VALUE_1);

        // assert
        assertAll(
                () -> assertNotEquals(d1, null, "Never equal to null"),
                () -> assertNotEquals(d1, "not a dataset", "Never equal to another type")
        );
    }

    @Test
    @DisplayName("hashCode(): equal objects have equal hash codes")
    public void hashCode_consistentWithEquals() {
        // arrange
        DefaultKeyedValueDataset d1 = dataset(KEY_A, VALUE_1);
        DefaultKeyedValueDataset d2 = dataset(KEY_A, VALUE_1);

        // assert
        assertEquals(d1.hashCode(), d2.hashCode(),
                "Equal objects must produce equal hash codes");
    }

    // clone()

    @Test
    @DisplayName("clone(): produces a distinct but equal copy")
    public void cloning_createsEqualButDistinctInstance() throws CloneNotSupportedException {
        // arrange
        DefaultKeyedValueDataset original = dataset(KEY_A, VALUE_1);

        // act
        DefaultKeyedValueDataset clone = (DefaultKeyedValueDataset) original.clone();

        // assert
        assertAll(
                () -> assertNotSame(original, clone, "Clone must be a different instance"),
                () -> assertSame(original.getClass(), clone.getClass(), "Clone must have same runtime type"),
                () -> assertEquals(original, clone, "Clone must be equal to the original")
        );
    }

    @Test
    @DisplayName("clone(): modifying the clone does not affect the original")
    public void clone_isIndependentFromOriginal() throws CloneNotSupportedException {
        // arrange
        DefaultKeyedValueDataset original = dataset("Key", 10.0);

        // act
        DefaultKeyedValueDataset clone = CloneUtils.clone(original);

        // assert precondition
        assertEquals(original, clone, "Sanity check: clone should start equal to original");

        // mutate clone only
        clone.updateValue(99.9);

        // assert independence
        assertNotEquals(original, clone, "Changing the clone must not affect the original");

        // revert and confirm equality again
        clone.updateValue(10.0);
        assertEquals(original, clone, "After reverting, objects should be equal again");
    }

    // serialization

    @Test
    @DisplayName("serialization: round-trip preserves equality")
    public void serialization_roundTrip_preservesEquality() {
        // arrange
        DefaultKeyedValueDataset original = dataset("Test", 25.3);

        // act
        DefaultKeyedValueDataset deserialized = TestUtils.serialised(original);

        // assert
        assertEquals(original, deserialized, "Serialized-then-deserialized object should be equal");
    }
}