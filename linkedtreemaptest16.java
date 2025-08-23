package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.gson.common.MoreAsserts;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import org.junit.Test;

public class LinkedTreeMapTestTest16 {

    @Test
    public void testJavaSerialization() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        Map<String, Integer> map = new LinkedTreeMap<>();
        map.put("a", 1);
        objOut.writeObject(map);
        objOut.close();
        ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        @SuppressWarnings("unchecked")
        Map<String, Integer> deserialized = (Map<String, Integer>) objIn.readObject();
        assertThat(deserialized).isEqualTo(Collections.singletonMap("a", 1));
    }
}
