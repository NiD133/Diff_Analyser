package org.apache.commons.jxpath.ri.axes;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NestedTestBean;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.CollectionPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.TestBeanFactory;
import org.apache.commons.jxpath.ri.model.dynamic.DynamicPointer;
import org.apache.commons.jxpath.ri.model.dynamic.DynamicPropertyPointer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the behavior of the SimplePathInterpreter for paths on property-owning
 * objects (like Beans and Maps) without predicates.
 *
 * <p>
 * These are white-box tests that verify the internal structure of the
 * NodePointer chains created by JXPath for various path expressions, especially
 * in lenient mode where paths to non-existent nodes are expected to produce
 * various forms of NullPointer.
 * </p>
 */
@DisplayName("SimplePathInterpreter: Property Owner Paths")
class SimplePathInterpreterPropertyOwnerPathTest {

    private JXPathContext context;
    private TestBeanWithNode bean;

    @BeforeEach
    void setUp() {
        bean = TestBeanWithNode.createTestBeanWithDOM();
        final HashMap<String, Object> submap = new HashMap<>();
        submap.put("key", new NestedTestBean("Name 9"));
        submap.put("strings", bean.getNestedBean().getStrings());

        bean.getList().add(new int[] { 1, 2 });
        bean.getList().add(bean.getVendor());
        bean.getMap().put("Key3", new Object[] { new NestedTestBean("some"), 2, bean.getVendor(), submap });
        bean.getMap().put("Key4", bean.getVendor());
        bean.getMap().put("Key5", submap);
        bean.getMap().put("Key6", new Object[0]);

        context = JXPathContext.newContext(null, bean);
        context.setLenient(true);
        context.setFactory(new TestBeanFactory());
    }

    @Test
    void getPointer_forExistingScalarProperty_returnsCorrectPointer() {
        assertValueAndPointer("/int", 1, "/int",
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class},
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class, BeanPointer.class});
    }

    @Test
    void getPointer_forSelfAxis_resolvesToCorrectPointer() {
        assertValueAndPointer("/./int", 1, "/int",
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class},
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class, BeanPointer.class});
    }

    @Test
    void getPointer_forMissingProperty_returnsNullPropertyPointer() {
        assertNullPointer("/foo", "/foo",
            new Class<?>[]{BeanPointer.class, NullPropertyPointer.class});
    }

    @Test
    void getPointer_forNestedExistingProperty_returnsCorrectPointer() {
        assertValueAndPointer("/nestedBean/int", 1, "/nestedBean/int",
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class, BeanPointer.class, BeanPropertyPointer.class},
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class, BeanPointer.class, BeanPropertyPointer.class, BeanPointer.class});
    }

    @Test
    void getPointer_forNestedCollectionProperty_returnsCorrectPointer() {
        assertValueAndPointer("/nestedBean/strings", bean.getNestedBean().getStrings(), "/nestedBean/strings",
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class, BeanPointer.class, BeanPropertyPointer.class},
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class, BeanPointer.class, BeanPropertyPointer.class, CollectionPointer.class});
    }

    @Test
    void getPointer_forNestedMissingProperty_returnsNullPropertyPointer() {
        assertNullPointer("/nestedBean/foo", "/nestedBean/foo",
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class, BeanPointer.class, NullPropertyPointer.class});
    }

    @Test
    void getPointer_forMissingPropertyOnMap_returnsDynamicPropertyPointer() {
        // In lenient mode, a pointer to a missing map entry is a DynamicPropertyPointer whose value is null.
        assertNullPointer("/map/foo", "/map[@name='foo']",
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class, DynamicPointer.class, DynamicPropertyPointer.class});
    }

    @Test
    void getPointer_forExistingPropertyInList_isFoundBySearching() {
        // JXPath searches the list for a bean with an 'int' property. The 3rd element is a TestBean.
        assertValueAndPointer("/list/int", 1, "/list[3]/int",
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class, CollectionPointer.class, BeanPointer.class, BeanPropertyPointer.class},
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class, CollectionPointer.class, BeanPointer.class, BeanPropertyPointer.class, BeanPointer.class});
    }

    @Test
    void getPointer_forMissingPropertyInList_returnsNullPointer() {
        // JXPath checks the first element of the list, which does not have a 'foo' property.
        assertNullPointer("/list/foo", "/list[1]/foo",
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class, CollectionPointer.class, BeanPointer.class, NullPropertyPointer.class});
    }

    @Test
    void getPointer_forChainedMissingProperties_returnsNullPointer() {
        assertNullPointer("/nestedBean/foo/bar", "/nestedBean/foo/bar",
            new Class<?>[]{
                BeanPointer.class,         // root bean
                BeanPropertyPointer.class, // 'nestedBean' property
                BeanPointer.class,         // value of 'nestedBean'
                NullPropertyPointer.class, // 'foo' property (missing)
                NullPointer.class,         // value of 'foo' (null)
                NullPropertyPointer.class  // 'bar' property (on null)
            });
    }

    @Test
    void getPointer_forMissingPropertyOnExistingCollectionItem_returnsNullPointer() {
        assertNullPointer("/list/int/bar", "/list[3]/int/bar",
            new Class<?>[]{
                BeanPointer.class, BeanPropertyPointer.class, CollectionPointer.class,
                BeanPointer.class, BeanPropertyPointer.class, BeanPointer.class,
                NullPropertyPointer.class
            });
    }

    @Test
    void getPointer_forChainedMissingPropertyOnCollection_returnsNullPointer() {
        assertNullPointer("/list/foo/bar", "/list[1]/foo/bar",
            new Class<?>[]{
                BeanPointer.class, BeanPropertyPointer.class, CollectionPointer.class,
                BeanPointer.class, NullPropertyPointer.class, NullPointer.class,
                NullPropertyPointer.class
            });
    }

    @Test
    void getPointer_forChainedMissingPropertyOnMap_returnsNullPointer() {
        assertNullPointer("/map/foo/bar", "/map[@name='foo']/bar",
            new Class<?>[]{
                BeanPointer.class, BeanPropertyPointer.class, DynamicPointer.class,
                DynamicPropertyPointer.class, NullPointer.class, NullPropertyPointer.class
            });
    }

    @Test
    void getPointer_forExistingDynamicPropertyOnMap_isFoundCorrectly() {
        assertValueAndPointer("/map/Key1", "Value 1", "/map[@name='Key1']",
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class, DynamicPointer.class, DynamicPropertyPointer.class},
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class, DynamicPointer.class, DynamicPropertyPointer.class, BeanPointer.class});
    }

    @Test
    void getPointer_forCollectionProperty_isFoundCorrectly() {
        assertValueAndPointer("/integers", bean.getIntegers(), "/integers",
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class},
            new Class<?>[]{BeanPointer.class, BeanPropertyPointer.class, CollectionPointer.class});
    }

    //--- Helper Methods ---

    /**
     * Asserts that a path resolves to a specific value and that the pointer chain
     * has the expected structure.
     */
    private void assertValueAndPointer(final String path, final Object expectedValue,
                                       final String expectedPath,
                                       final Class<?>[] expectedPointerTypes,
                                       final Class<?>[] expectedValuePointerTypes) {
        // 1. Check the resolved value
        final Object value = context.getValue(path);
        assertEquals(expectedValue, value, "Value mismatch for path: " + path);

        // 2. Check the pointer for the path
        final NodePointer pointer = (NodePointer) context.getPointer(path);
        assertEquals(expectedPath, pointer.asPath(), "Pointer path mismatch for: " + path);
        assertPointerChain("Pointer", pointer, expectedPointerTypes);

        // 3. Check the pointer for the value
        final NodePointer valuePointer = pointer.getValuePointer();
        assertPointerChain("ValuePointer", valuePointer, expectedValuePointerTypes);
    }

    /**
     * Asserts that a path resolves to a non-existent node (a "null pointer") and
     * that the pointer chain has the expected structure.
     */
    private void assertNullPointer(final String path, final String expectedPath,
                                   final Class<?>[] expectedPointerTypes) {
        // 1. Check the pointer for the path
        final NodePointer pointer = (NodePointer) context.getPointer(path);
        assertNotNull(pointer, "Pointer should exist for path: " + path);
        assertEquals(expectedPath, pointer.asPath(), "Pointer path mismatch for: " + path);
        assertPointerChain("Pointer", pointer, expectedPointerTypes);

        // 2. Check the pointer for the value, which should represent null
        final NodePointer valuePointer = pointer.getValuePointer();
        assertFalse(valuePointer.isActual(), "Value pointer should not be for an actual node for path: " + path);

        // The value of a null-representing pointer is a NullPointer itself.
        // Its chain should be the original pointer's chain plus a final NullPointer.
        final List<Class<?>> expectedValuePointerTypes = new ArrayList<>(Arrays.asList(expectedPointerTypes));
        expectedValuePointerTypes.add(NullPointer.class);

        assertPointerChain("ValuePointer", valuePointer, expectedValuePointerTypes.toArray(new Class[0]));
    }

    /**
     * Asserts that the chain of pointers from the root to the given pointer
     * consists of instances of the expected classes.
     *
     * @param pointerName         A descriptive name for the pointer being checked (e.g., "Pointer", "ValuePointer").
     * @param pointer             The leaf NodePointer of the chain to verify.
     * @param expectedTypesInChain An array of classes representing the expected pointer
     *                            types, starting from the root.
     */
    private void assertPointerChain(final String pointerName, final NodePointer pointer, final Class<?>... expectedTypesInChain) {
        final List<Class<?>> actualTypes = new ArrayList<>();
        for (NodePointer current = pointer; current != null; current = current.getImmediateParentPointer()) {
            actualTypes.add(0, current.getClass());
        }
        assertArrayEquals(expectedTypesInChain, actualTypes.toArray(new Class[0]),
            () -> String.format("%s chain type mismatch for pointer: %s", pointerName, pointer.asPath()));
    }
}