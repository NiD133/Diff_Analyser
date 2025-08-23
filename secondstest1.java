package org.joda.time;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    @Test
    public void constants_shouldHoldCorrectValues() {
        // Verifies that the public constants in the Seconds class have the expected integer values.
        assertThat(Seconds.ZERO.getSeconds()).isEqualTo(0);
        assertThat(Seconds.ONE.getSeconds()).isEqualTo(1);
        assertThat(Seconds.TWO.getSeconds()).isEqualTo(2);
        assertThat(Seconds.THREE.getSeconds()).isEqualTo(3);
        assertThat(Seconds.MAX_VALUE.getSeconds()).isEqualTo(Integer.MAX_VALUE);
        assertThat(Seconds.MIN_VALUE.getSeconds()).isEqualTo(Integer.MIN_VALUE);
    }
}