package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the factory method {@link CharSet#getInstance(String...)}.
 * This class focuses on verifying the caching of common, pre-defined instances.
 */
// Renamed from CharSetTestTest10 for clarity and to follow standard conventions.
public class CharSetTest extends AbstractLangTest {

    /**
     * Provides arguments for the parameterized test. Each argument consists of a
     * string definition and the expected pre-defined CharSet constant.
     *
     * @return a stream of arguments for the test.
     */
    static Stream<Arguments> commonInstanceProvider() {
        return Stream.of(
            Arguments.of(null, CharSet.EMPTY),
            Arguments.of("", CharSet.EMPTY),
            Arguments.of("a-zA-Z", CharSet.ASCII_ALPHA),
            Arguments.of("A-Za-z", CharSet.ASCII_ALPHA),
            Arguments.of("a-z", CharSet.ASCII_ALPHA_LOWER),
            Arguments.of("A-Z", CharSet.ASCII_ALPHA_UPPER),
            Arguments.of("0-9", CharSet.ASCII_NUMERIC)
        );
    }

    @DisplayName("getInstance() should return cached, pre-defined instances for common set definitions")
    @ParameterizedTest(name = "[{index}] For input \"{0}\", should return CharSet.{1}")
    @MethodSource("commonInstanceProvider")
    void testGetInstanceReturnsCommonInstances(final String setDefinition, final CharSet expectedInstance) {
        // The CharSet.getInstance method is optimized to return pre-defined constants
        // for common character set definitions. This behavior is documented and relies
        // on a map of common instances.

        // We use assertSame to verify that the factory method returns the *exact*
        // same singleton instance, not just an equivalent one. This confirms that
        // the caching mechanism is working as expected.
        // The cast to (String) is necessary to resolve ambiguity with the
        // varargs method signature CharSet.getInstance(String...).
        final CharSet actualInstance = CharSet.getInstance((String) setDefinition);

        assertSame(expectedInstance, actualInstance);
    }
}