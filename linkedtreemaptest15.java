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

public class LinkedTreeMapTestTest15 {

    @Test
    public void testEqualsAndHashCode() throws Exception {
        LinkedTreeMap<String, Integer> map1 = new LinkedTreeMap<>();
        map1.put("A", 1);
        map1.put("B", 2);
        map1.put("C", 3);
        map1.put("D", 4);
        LinkedTreeMap<String, Integer> map2 = new LinkedTreeMap<>();
        map2.put("C", 3);
        map2.put("B", 2);
        map2.put("D", 4);
        map2.put("A", 1);
        MoreAsserts.assertEqualsAndHashCode(map1, map2);
    }
}
