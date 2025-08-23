package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link CharSet} class, focusing on specific behaviors
 * related to its construction and modification.
 */
public class CharSetTest {

    /**
     * Tests a subtle behavior: the protected {@code add} method can modify the
     * shared, static {@code CharSet.EMPTY} instance.
     *
     * <p>The {@code CharSet.getInstance()} factory is documented to return immutable
     * instances. For inputs like {@code null}, it returns a shared singleton,
     * {@code CharSet.EMPTY}.</p>
     *
     * <p>However, the {@code add} method is {@code protected}, allowing code within the
     * same package (like this test) to call it. This test verifies that such a
     * call mutates the state of the shared singleton. This can lead to unexpected
     * side effects in other parts of an application that rely on the immutability
     * of {@code CharSet.EMPTY}.</p>
     *
     * <p><b>Note:</b> This test has side effects on a static final field. It should
     * ideally be run in an isolated environment to avoid impacting other tests.</p>
     */
    @Test
    public void testProtectedAddMethodCanModifySharedEmptyInstance() {
        // Arrange:
        // Get the shared instance returned for a null definition.
        // We will add a character that is definitely not in an empty set.
        final CharSet sharedEmptySet = CharSet.getInstance((String) null);
        final char charToTest = 'a';
        final String definitionToAdd = "a-c";

        // Verify the precondition: the set should be empty.
        // This also helps detect if a previous test has already modified the static state.
        assertFalse("Precondition: The shared EMPTY set should not contain the test character initially.",
                    sharedEmptySet.contains(charToTest));

        // Act:
        // Call the protected add() method. This is possible because this test
        // resides in the same package (org.apache.commons.lang3). This call
        // mutates the internal state of the supposedly immutable, shared object.
        sharedEmptySet.add(definitionToAdd);

        // Assert:
        // 1. The original instance reference now reflects the modification.
        assertTrue("The set should contain the character after calling add().",
                   sharedEmptySet.contains(charToTest));

        // 2. Verify that retrieving the instance again from the factory or the public
        //    static field returns the *same, modified* instance.
        CharSet instanceFromFactory = CharSet.getInstance((String) null);
        assertSame("getInstance(null) should always return the same singleton instance.",
                   sharedEmptySet, instanceFromFactory);
        assertTrue("The instance retrieved again from the factory should reflect the modification.",
                   instanceFromFactory.contains(charToTest));

        assertTrue("The public static CharSet.EMPTY field should also reflect the modification.",
                   CharSet.EMPTY.contains(charToTest));
    }
}