package com.google.common.primitives;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.testing.SerializableTester;
import java.util.Comparator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for the serialization behavior of {@link SignedBytes#lexicographicalComparator()}.
 */
@GwtIncompatible // SerializableTester is not GWT-compatible.
@RunWith(JUnit4.class)
public class SignedBytesLexicographicalComparatorTest {

    @Test
    @J2ktIncompatible // SerializableTester is not J2KT-compatible.
    public void lexicographicalComparator_serialization_preservesSingletonInstance() {
        // GIVEN the lexicographical comparator, which is implemented as a singleton.
        Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();

        // WHEN the comparator is serialized and then deserialized.
        Comparator<byte[]> reserializedComparator = SerializableTester.reserialize(comparator);

        // THEN the deserialized instance is the same canonical instance as the original.
        // This is an expected and important property of using an enum for the singleton pattern.
        assertThat(reserializedComparator).isSameInstanceAs(comparator);
    }
}