package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import org.junit.Test;

public class LazilyParsedNumberTestTest3 {

    @Test
    public void testJavaSerialization() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(new LazilyParsedNumber("123"));
        objOut.close();
        ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        Number deserialized = (Number) objIn.readObject();
        assertThat(deserialized).isEqualTo(new BigDecimal("123"));
    }
}
