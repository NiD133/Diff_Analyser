package com.google.gson;

import org.junit.Test;
import java.lang.reflect.Field;

// The following imports are retained from the original test structure
// to ensure compatibility with the existing test execution environment.
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * This test class contains a refactored test case for the FieldNamingPolicy enum.
 * The original test was auto-generated and has been improved for clarity and maintainability.
 */
// The class name and scaffolding inheritance are kept to match the original structure.
public class FieldNamingPolicy_ESTestTest13 extends FieldNamingPolicy_ESTest_scaffolding {

    /**
     * Verifies that the translateName method throws a NullPointerException when the input Field is null.
     *
     * This refactored test uses JUnit 4's 'expected' parameter in the @Test annotation,
     * which is a standard, declarative way to test for exceptions. This makes the test's
     * intent clear from its signature.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void translateName_whenFieldIsNull_throwsNullPointerException() {
        // Arrange:
        // Get an instance of any naming policy. The behavior for a null input
        // should be consistent across all policies, as the null check precedes
        // any policy-specific logic.
        FieldNamingPolicy policy = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

        // Act:
        // Call the method under test with a null argument.
        policy.translateName((Field) null);

        // Assert:
        // The test passes if a NullPointerException is thrown, as specified by the
        // `expected` parameter in the @Test annotation. No explicit assertion is needed.
    }
}