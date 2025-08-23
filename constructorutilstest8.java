package org.apache.commons.lang3.reflect;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests the varargs constructor invocation features of {@link ConstructorUtils},
 * specifically focusing on scenarios involving type coercion like unboxing.
 */
class ConstructorUtilsInvokeConstructorVarargsTest {

    /**
     * A helper class with a specific varargs constructor used to verify that
     * ConstructorUtils can correctly identify and invoke it even when
     * argument types require unboxing.
     */
    public static class VarargsUnboxingTestBean {

        final String[] processedVarArgs;

        /**
         * This constructor is the target for the test. It takes a fixed Integer
         * and a variable number of primitive ints.
         */
        public VarargsUnboxingTestBean(final Integer first, final int... args) {
            // Convert the varargs to strings to easily verify them later.
            processedVarArgs = new String[args.length];
            for (int i = 0; i < args.length; ++i) {
                processedVarArgs[i] = Integer.toString(args[i]);
            }
        }
    }

    @Test
    @DisplayName("invokeConstructor should select a varargs constructor that requires unboxing for its arguments")
    void invokeConstructor_shouldSelectVarargsConstructor_whenUnboxingIsRequired() throws Exception {
        // Arrange
        // The target constructor is VarargsUnboxingTestBean(Integer, int...).
        // We pass three Integers. The reflection utility should match this by
        // unboxing the second and third Integers to ints for the varargs parameter.
        final Integer firstArg = 1;
        final Integer varArg1 = 2;
        final Integer varArg2 = 3;
        final String[] expectedProcessedVarArgs = {"2", "3"};

        // Act
        final VarargsUnboxingTestBean bean = ConstructorUtils.invokeConstructor(
            VarargsUnboxingTestBean.class, firstArg, varArg1, varArg2);

        // Assert
        assertNotNull(bean, "The constructor should have been invoked successfully.");
        assertArrayEquals(expectedProcessedVarArgs, bean.processedVarArgs,
            "The varargs arguments should be correctly processed after unboxing.");
    }
}