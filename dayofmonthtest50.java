package org.threeten.extra;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the equals() and hashCode() contract of the DayOfMonth class.
 */
public class DayOfMonthTestTest50 {

    private static final int MAX_DAY_OF_MONTH = 31;

    @Test
    @DisplayName("DayOfMonth instances should adhere to the equals and hashCode contract")
    void equalsAndHashCodeShouldFollowContract() {
        EqualsTester equalsTester = new EqualsTester();

        // Add an equality group for each possible day of the month.
        // EqualsTester verifies that:
        // 1. Items within the same group are equal to each other.
        // 2. Items in different groups are not equal to each other.
        // 3. The hashCode() contract is consistent with equals().
        // This comprehensively tests the contract for all possible DayOfMonth values.
        for (int i = 1; i <= MAX_DAY_OF_MONTH; i++) {
            equalsTester.addEqualityGroup(DayOfMonth.of(i));
        }

        equalsTester.testEquals();
    }
}