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

public class LinkedTreeMapTestTest11 {

    @Test
    public void testPutOverrides() throws Exception {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        assertThat(map.put("d", "donut")).isNull();
        assertThat(map.put("e", "eclair")).isNull();
        assertThat(map.put("f", "froyo")).isNull();
        assertThat(map).hasSize(3);
        assertThat(map.get("d")).isEqualTo("donut");
        assertThat(map.put("d", "done")).isEqualTo("donut");
        assertThat(map).hasSize(3);
    }
}
