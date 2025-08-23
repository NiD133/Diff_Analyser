package org.apache.commons.lang3.reflect;

import org.junit.Test;
import java.lang.reflect.Constructor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for {@link ConstructorUtils}.
 */
public class ConstructorUtilsTest {

    /**
     * Tests that {@code getMatchingAccessibleConstructor} can find a constructor
     * when a {@code null} is provided in the parameter types array.
     * <p>
     * The {@code null} acts as a wildcard for any reference type. For the {@code Integer}
     * class, which has constructors {@code Integer(int)} and {@code Integer(String)},
     * this test verifies that the {@code Integer(String)} constructor is matched, as
     * {@code String} is a reference type.
     */
    @Test
    public void testGetMatchingAccessibleConstructorWithNullParameterType() {
        // Arrange
        // A null in the parameter types array is treated as a wildcard for any reference type.
        final Class<?>[] parameterTypes = { null };

        // Act
        final Constructor<Integer> foundConstructor = ConstructorUtils.getMatchingAccessibleConstructor(Integer.class, parameterTypes);

        // Assert
        assertNotNull("A matching constructor should have been found.", foundConstructor);

        // Verify that the specific constructor Integer(String) was returned.
        final Class<?>[] actualParameterTypes = foundConstructor.getParameterTypes();
        assertEquals("The constructor should have exactly one parameter.", 1, actualParameterTypes.length);
        assertEquals("The parameter type should be String.", String.class, actualParameterTypes[0]);
    }
}