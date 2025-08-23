package com.google.common.primitives;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.annotations.GwtCompatible;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link SignedBytes} static methods.
 */
@GwtCompatible
@RunWith(JUnit4.class)
public class SignedBytesTest {

    private static final byte MIN_BYTE = Byte.MIN_VALUE;
    private static final byte MAX_BYTE = Byte.MAX_VALUE;

    private static final byte[] TYPICAL_VALUES = {MIN_BYTE, -1, 0, 1, MAX_BYTE};

    @Test
    public void saturatedCast_withValueInRange_returnsSameValue() {
        for (byte value : TYPICAL_VALUES) {
            assertThat(SignedBytes.saturatedCast((long) value))
                .isEqualTo(value);
        }
    }

    @Test
    public void saturatedCast_withValueAboveMax_saturatesToMaxValue() {
        assertThat(SignedBytes.saturatedCast(MAX_BYTE + 1L))
            .isEqualTo(MAX_BYTE);
        assertThat(SignedBytes.saturatedCast(Long.MAX_VALUE))
            .isEqualTo(MAX_BYTE);
    }

    @Test
    public void saturatedCast_withValueBelowMin_saturatesToMinValue() {
        assertThat(SignedBytes.saturatedCast(MIN_BYTE - 1L))
            .isEqualTo(MIN_BYTE);
        assertThat(SignedBytes.saturatedCast(Long.MIN_VALUE))
            .isEqualTo(MIN_BYTE);
    }
}