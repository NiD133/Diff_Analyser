/**
 * Tests for ComparableComparator.
 */
@SuppressWarnings("boxing")
class ComparableComparatorTest extends AbstractComparatorTest<Integer> {

    // Define a clear and concise description of the test data structure
    private static final List<Integer> COMPARABLE_OBJECTS = List.of(1, 2, 3, 4, 5);

    /**
     * Returns a list of comparable objects in the correct order.
     *
     * @return a list of integers in ascending order
     */
    @Override
    public List<Integer> getComparableObjectsOrdered() {
        return COMPARABLE_OBJECTS;
    }

    /**
     * Returns the compatibility version of the test.
     *
     * @return the compatibility version as a string
     */
    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    /**
     * Creates a new instance of the ComparableComparator.
     *
     * @return a new ComparableComparator instance
     */
    @Override
    public Comparator<Integer> makeObject() {
        // Encourage the use of the singleton instance for performance reasons
        return ComparableComparator.INSTANCE;
    }

    // Commented out for now, but can be used for testing serialization
    // void testCreate() throws Exception {
    //     writeExternalFormToDisk((java.io.Serializable) makeObject(), "src/test/resources/data/test/ComparableComparator.version4.obj");
    // }
}