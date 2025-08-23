package org.threeten.extra;

import org.junit.Test;

/**
 * Contains test cases for the {@link DayOfMonth} class.
 * This suite focuses on ensuring the robustness and correctness of the DayOfMonth API.
 */
public class DayOfMonthTest {

    /**
     * Tests that the query() method throws a NullPointerException when passed a null argument.
     * The Java Time specification requires that a null query object should not be accepted.
     */
    @Test(expected = NullPointerException.class)
    public void query_withNullArgument_shouldThrowNullPointerException() {
        // Given a fixed DayOfMonth instance to ensure the test is deterministic.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // When the query() method is called with a null query,
        // then a NullPointerException is expected.
        dayOfMonth.query(null);
    }
}