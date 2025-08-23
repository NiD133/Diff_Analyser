package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.mock;

/**
 * This is an improved version of an auto-generated test case.
 * The original test was functionally correct but lacked clarity.
 *
 * Improvements made:
 * - **Descriptive Naming:** The test method and variables now have names that clearly state their purpose.
 * - **Clearer Assertions:** Replaced the original try-catch block and a proprietary assertion helper
 *   with JUnit's `ExpectedException` rule, making the expected outcome declarative and easier to read.
 * - **Arrange-Act-Assert Pattern:** The test is structured to clearly separate the setup, execution, and verification steps.
 * - **Reduced Noise:** Removed unused variables, imports, and arbitrary "magic" values from the original test.
 */
public class ParallelScatterZipCreator_ESTestTest9 extends ParallelScatterZipCreator_ESTest_scaffolding {

    // A JUnit Rule to assert that a specific exception is thrown. This is a standard way
    // to test for exceptions in JUnit 4, allowing for message verification.
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that createCallable() throws an IllegalArgumentException
     * when the provided ZipArchiveEntry does not have a compression method set.
     * A newly created ZipArchiveEntry has its method as -1 (unset) by default.
     */
    @Test(timeout = 4000)
    public void createCallableShouldThrowExceptionForEntryWithUnsetMethod() {
        // Arrange
        // An executor service is required by the constructor.
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator(executorService);

        final String entryName = "entry-with-unset-method.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(entryName);
        // A new ZipArchiveEntry defaults to method = -1, which is the condition under test.

        InputStreamSupplier mockInputStreamSupplier = mock(InputStreamSupplier.class);

        // Assert: Define the expected exception and its message.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method must be set on zipArchiveEntry: " + entryName);

        // Act: Call the method that is expected to throw the exception.
        creator.createCallable(entry, mockInputStreamSupplier);

        // The test will pass if the expected exception is thrown.
        // The creator is responsible for shutting down the executor service when its work is done (e.g., via writeTo()),
        // so no explicit shutdown is needed in this specific test case.
    }
}