package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

/**
 * Tests for the equals() and hashCode() contracts of {@link LazilyParsedNumber}.
 */
// Renamed class to follow standard naming conventions.
public class LazilyParsedNumberTest {

    // Test method name now describes the specific scenario.
    // Using isEqualTo() is more idiomatic and provides better failure messages.
    @Test
    public void equals_twoInstancesWithSameValue_areEqual() {
        LazilyParsedNumber number1 = new LazilyParsedNumber("123");
        LazilyParsedNumber anotherNumberWithSameValue = new LazilyParsedNumber("123");

        // Asserts that number1.equals(anotherNumberWithSameValue) is true.
        assertThat(number1).isEqualTo(anotherNumberWithSameValue);
    }

    // A good test suite for equals() should also test the hashCode() contract.
    @Test
    public void hashCode_twoEqualInstances_haveSameHashCode() {
        LazilyParsedNumber number1 = new LazilyParsedNumber("123");
        LazilyParsedNumber anotherNumberWithSameValue = new LazilyParsedNumber("123");

        // The hashCode() contract: if two objects are equal, their hash codes must be equal.
        assertThat(number1.hashCode()).isEqualTo(anotherNumberWithSameValue.hashCode());
    }

    // Added a test for the inequality case.
    @Test
    public void equals_twoInstancesWithDifferentValues_areNotEqual() {
        LazilyParsedNumber number1 = new LazilyParsedNumber("123");
        LazilyParsedNumber number2 = new LazilyParsedNumber("456");

        assertThat(number1).isNotEqualTo(number2);
    }

    // Added a test to verify behavior with null, which is part of the equals() contract.
    @Test
    public void equals_comparedWithNull_isFalse() {
        LazilyParsedNumber number1 = new LazilyParsedNumber("123");

        // An object should never be equal to null. isNotEqualTo() handles the null check.
        assertThat(number1).isNotEqualTo(null);
    }

    // Added a test to verify behavior with a different object type.
    @Test
    public void equals_comparedWithDifferentType_isFalse() {
        LazilyParsedNumber number1 = new LazilyParsedNumber("123");
        String stringObject = "123";

        // An object should only be equal to other instances of the same class.
        assertThat(number1).isNotEqualTo(stringObject);
    }
}