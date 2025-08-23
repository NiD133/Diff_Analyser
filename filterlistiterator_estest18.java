package org.apache.commons.collections4.iterators;

import org.junit.Test;

/**
 * An improved, more understandable test for the {@link FilterListIterator}.
 *
 * <p>This class replaces a confusing, auto-generated test case. The original test
 * was difficult to comprehend due to numerous unused variables, irrelevant method
 * calls, and an incorrect assertion. The goal of this version is to be clear,
 * concise, and to correctly test a specific behavior of the
 * {@code FilterListIterator}.</p>
 */
public class FilterListIteratorUnderstandabilityTest {

    /**
     * Verifies that calling {@code next()} on a {@code FilterListIterator} that was
     * created without an underlying {@code ListIterator} throws a
     * {@code NullPointerException}.
     *
     * <p><b>Improvement Rationale:</b></p>
     * <p>The original test was a single, large method with many unrelated operations.
     * Its only actual assertion was checking for an exception when {@code next()} was
     * called on a default-constructed {@code FilterListIterator}.</p>
     *
     * <p>This improved test focuses solely on that behavior:</p>
     * <ol>
     *   <li><b>Simplicity:</b> All irrelevant setup and method calls have been removed,
     *       leaving only the code essential for the test.</li>
     *   <li><b>Clarity:</b> The test is named to clearly describe the scenario it covers,
     *       following the Arrange-Act-Assert pattern implicitly.</li>
     *   <li><b>Correctness:</b> The original test incorrectly expected a
     *       {@code NoSuchElementException}. The actual behavior is to throw a
     *       {@code NullPointerException} because the internal iterator is {@code null}
     *       and gets dereferenced. This test asserts the correct exception type.
     *       A {@code NoSuchElementException} would only be expected if the underlying
     *       iterator was present but empty.</li>
     *   <li><b>Readability:</b> Uses the standard JUnit 4 {@code @Test(expected=...)}
     *       annotation, which is more concise and readable than a try-catch-fail block
     *       for checking exceptions.</li>
     * </ol>
     */
    @Test(expected = NullPointerException.class)
    public void nextOnUninitializedIteratorShouldThrowNullPointerException() {
        // Arrange: Create a FilterListIterator using the default constructor.
        // This means its internal ListIterator is not set (i.e., it is null).
        final FilterListIterator<Object> uninitializedIterator = new FilterListIterator<>();

        // Act & Assert: Calling next() attempts to access the null internal iterator,
        // which is expected to result in a NullPointerException.
        uninitializedIterator.next();
    }
}