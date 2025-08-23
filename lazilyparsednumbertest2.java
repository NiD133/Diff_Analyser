package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import org.junit.Test;

public class LazilyParsedNumberTestTest2 {

    @Test
    public void testEquals() {
        LazilyParsedNumber n1 = new LazilyParsedNumber("1");
        LazilyParsedNumber n1Another = new LazilyParsedNumber("1");
        assertThat(n1.equals(n1Another)).isTrue();
    }
}
