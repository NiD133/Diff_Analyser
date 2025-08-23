package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link ByteOrderMark#get(int)}.
 */
@DisplayName("ByteOrderMark#get(int)")
class ByteOrderMarkTest {

    private static final ByteOrderMark BOM_1_BYTE = new ByteOrderMark("BOM_1_BYTE", 1);
    private static final ByteOrderMark BOM_2_BYTES = new ByteOrderMark("BOM_2_BYTES", 1, 2);
    private static final ByteOrderMark BOM_3_BYTES = new ByteOrderMark("BOM_3_BYTES", 1, 2, 3);

    static Stream<Arguments> getArguments() {
        return Stream.of(
            // Test with a 1-byte BOM
            Arguments.of("1-byte BOM, index 0", BOM_1_BYTE, 0, 1),
            // Test with a 2-byte BOM
            Arguments.of("2-byte BOM, index 0", BOM_2_BYTES, 0, 1),
            Arguments.of("2-byte BOM, index 1", BOM_2_BYTES, 1, 2),
            // Test with a 3-byte BOM
            Arguments.of("3-byte BOM, index 0", BOM_3_BYTES, 0, 1),
            Arguments.of("3-byte BOM, index 1", BOM_3_BYTES, 1, 2),
            Arguments.of("3-byte BOM, index 2", BOM_3_BYTES, 2, 3)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getArguments")
    void shouldReturnCorrectByteForGivenIndex(final String testName, final ByteOrderMark bom, final int index, final int expectedByte) {
        assertEquals(expectedByte, bom.get(index));
    }
}