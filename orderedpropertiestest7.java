package org.apache.commons.collections4.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

public class OrderedPropertiesTestTest7 {

    private void assertAscendingOrder(final OrderedProperties orderedProperties) {
        final int first = 1;
        final int last = 11;
        final Enumeration<Object> enumObjects = orderedProperties.keys();
        for (int i = first; i <= last; i++) {
            assertEquals("key" + i, enumObjects.nextElement());
        }
        final Iterator<Object> iterSet = orderedProperties.keySet().iterator();
        for (int i = first; i <= last; i++) {
            assertEquals("key" + i, iterSet.next());
        }
        final Iterator<Entry<Object, Object>> iterEntrySet = orderedProperties.entrySet().iterator();
        for (int i = first; i <= last; i++) {
            final Entry<Object, Object> next = iterEntrySet.next();
            assertEquals("key" + i, next.getKey());
            assertEquals("value" + i, next.getValue());
        }
        final Enumeration<?> propertyNames = orderedProperties.propertyNames();
        for (int i = first; i <= last; i++) {
            assertEquals("key" + i, propertyNames.nextElement());
        }
    }

    private OrderedProperties assertDescendingOrder(final OrderedProperties orderedProperties) {
        final int first = 11;
        final int last = 1;
        final Enumeration<Object> enumObjects = orderedProperties.keys();
        for (int i = first; i <= last; i--) {
            assertEquals("key" + i, enumObjects.nextElement());
        }
        final Iterator<Object> iterSet = orderedProperties.keySet().iterator();
        for (int i = first; i <= last; i--) {
            assertEquals("key" + i, iterSet.next());
        }
        final Iterator<Entry<Object, Object>> iterEntrySet = orderedProperties.entrySet().iterator();
        for (int i = first; i <= last; i--) {
            final Entry<Object, Object> next = iterEntrySet.next();
            assertEquals("key" + i, next.getKey());
            assertEquals("value" + i, next.getValue());
        }
        final Enumeration<?> propertyNames = orderedProperties.propertyNames();
        for (int i = first; i <= last; i--) {
            assertEquals("key" + i, propertyNames.nextElement());
        }
        return orderedProperties;
    }

    private OrderedProperties loadOrderedKeysReverse() throws FileNotFoundException, IOException {
        final OrderedProperties orderedProperties = new OrderedProperties();
        try (FileReader reader = new FileReader("src/test/resources/org/apache/commons/collections4/properties/test-reverse.properties")) {
            orderedProperties.load(reader);
        }
        return assertDescendingOrder(orderedProperties);
    }

    @Test
    void testLoadOrderedKeysReverse() throws IOException {
        loadOrderedKeysReverse();
    }
}
