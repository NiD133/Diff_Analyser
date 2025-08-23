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

public class LinkedTreeMapTestTest9 {

    @Test
    public void testContainsNonComparableKeyReturnsFalse() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        map.put("a", "android");
        assertThat(map).doesNotContainKey(new Object());
    }
}
