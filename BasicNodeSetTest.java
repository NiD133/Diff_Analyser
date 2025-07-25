/**
 * Test BasicNodeSet.
 */
class BasicNodeSetTest extends AbstractJXPathTest {

    // JXPathContext
    protected JXPathContext context;
    // BasicNodeSet
    protected BasicNodeSet nodeSet;

    /**
     * Set up before each test.
     */
    @Override
    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
        context = JXPathContext.newContext(new TestMixedModelBean());
        nodeSet = new BasicNodeSet();
    }

    /**
     * Test adding a list of numbers to the node set.
     */
    @Test
    void testAddNumbers() {
        // Given
        String xpath = "/bean/integers";

        // When
        addPointersToNodeSet(xpath);

        // Then
        assertNodeSetPointers(xpath, 4);
        assertNodeSetValues(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4));
        assertNodeSetNodes(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4));
    }

    /**
     * Test adding a list of contacts to the node set.
     */
    @Test
    void testAddContacts() {
        // Given
        String xpath = "/document/vendor/contact";

        // When
        addPointersToNodeSet(xpath);

        // Then
        assertNodeSetPointers(xpath, 4);
        assertNodeSetValues("John", "Jack", "Jim", "Jack Black");
        assertNodeSetNodesHaveElementName("contact");
        assertNodeSetNodesHaveElementValues("John", "Jack", "Jim", "Jack Black");
    }

    /**
     * Test removing a pointer from the node set.
     */
    @Test
    void testRemovePointer() {
        // Given
        String xpath = "/bean/integers";
        String pointerToRemove = "/bean/integers[4]";

        // When
        addPointersToNodeSet(xpath);
        removePointerFromNodeSet(pointerToRemove);

        // Then
        assertNodeSetPointers(xpath, 3);
        assertNodeSetValues(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
        assertNodeSetNodes(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
    }

    /**
     * Add pointers for the specified xpath to the node set.
     *
     * @param xpath xpath expression
     */
    private void addPointersToNodeSet(String xpath) {
       	addPointers(xpath);
    }

    /**
     * Remove a pointer from the node set.
     *
     * @param xpath xpath expression
     */
    private void removePointerFromNodeSet(String xpath) {
        removePointers(xpath);
    }

    /**
     * Assert that the node set pointers match the expected pointers.
     *
     * @param xpath xpath expression
     * @param expectedSize expected size of the pointers
     */
    private void assertNodeSetPointers(String xpath, int expectedSize) {
        List<String> expectedPointers = new ArrayList<>();
        for (int i = 1; i <= expectedSize; i++) {
            expectedPointers.add(xpath + "[" + i + "]");
        }
        assertEquals(expectedPointers.toString(), nodeSet.getPointers().toString());
    }

    /**
     * Assert that the node set values match the expected values.
     *
     * @param expectedValues expected values
     */
    private void assertNodeSetValues(Object... expectedValues) {
        assertEquals(list(expectedValues), nodeSet.getValues());
    }

    /**
     * Assert that the node set nodes match the expected nodes.
     *
     * @param expectedNodes expected nodes
     */
    private void assertNodeSetNodes(Object... expectedNodes) {
        assertEquals(list(expectedNodes), nodeSet.getNodes());
    }

    /**
     * Assert that the node set nodes have the expected element name.
     */
    private void assertNodeSetNodesHaveElementName(String expectedElementName) {
        assertElementNames(list(expectedElementName), nodeSet.getNodes());
    }

    /**
     * Assert that the node set nodes have the expected element values.
     *
     * @param expectedValues expected values
     */
    private void assertNodeSetNodesHaveElementValues(Object... expectedValues) {
        assertElementValues(list(expectedValues), nodeSet.getNodes());
    }

    /**
     * Add the pointers for the specified xpath to the node set.
     *
     * @param xpath xpath expression
     */
    protected void addPointers(final String xpath) {
        for (final Iterator<Pointer> iter = context.iteratePointers(xpath); iter.hasNext();) {
            nodeSet.add(iter.next());
        }
        nudge();
    }

    /**
     * Remove the pointers for the specified xpath from the node set.
     *
     * @param xpath xpath expression
     */
    protected void removePointers(final String xpath) {
        for (final Iterator<Pointer> iter = context.iteratePointers(xpath); iter.hasNext();) {
            nodeSet.remove(iter.next());
        }
        nudge();
    }

    /**
     * "Nudge" the node set.
     */
    protected void nudge() {
        nodeSet.getPointers();
        nodeSet.getValues();
        nodeSet.getNodes();
    }

    /**
     * Assert that the element names match the expected names.
     *
     * @param expectedNames expected names
     * @param elements elements
     */
    protected void assertElementNames(List expectedNames, List elements) {
        assertEquals(expectedNames.size(), elements.size());
        Iterator expectedNameIter = expectedNames.iterator();
        Iterator elementIter = elements.iterator();
        while (elementIter.hasNext()) {
            assertEquals(expectedNameIter.next(), ((Element) elementIter.next()).getTagName());
        }
    }

    /**
     * Assert that the element values match the expected values.
     *
     * @param expectedValues expected values
     * @param elements elements
     */
    protected void assertElementValues(List expectedValues, List elements) {
        assertEquals(expectedValues.size(), elements.size());
        Iterator expectedValueIter = expectedValues.iterator();
        Iterator elementIter = elements.iterator();
        while (elementIter.hasNext()) {
            assertEquals(expectedValueIter.next(), ((Element) elementIter.next()).getFirstChild().getNodeValue());
        }
    }

    /**
     * Create a list from the given objects.
     *
     * @param objects objects
     * @return list
     */
    private List list(Object... objects) {
        return Arrays.asList(objects);
    }
}