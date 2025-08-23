public class ObjectGraphIterator_ESTestTest8 extends ObjectGraphIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        linkedList0.add((Integer) null);
        Iterator<Integer> iterator0 = linkedList0.descendingIterator();
        ObjectGraphIterator<Object> objectGraphIterator0 = new ObjectGraphIterator<Object>(iterator0);
        Class<Integer> class0 = Integer.class;
        InstanceofPredicate instanceofPredicate0 = new InstanceofPredicate(class0);
        Transformer<InstanceofPredicate, InstanceofPredicate> transformer0 = InvokerTransformer.invokerTransformer("'#+ Y@U&ZFCL9E$FCS");
        ObjectGraphIterator<InstanceofPredicate> objectGraphIterator1 = new ObjectGraphIterator<InstanceofPredicate>(instanceofPredicate0, transformer0);
        ObjectGraphIterator<InstanceofPredicate> objectGraphIterator2 = new ObjectGraphIterator<InstanceofPredicate>(instanceofPredicate0, transformer0);
        // Undeclared exception!
        try {
            objectGraphIterator1.findNextByIterator(objectGraphIterator2);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // InvokerTransformer: The method ''#+ Y@U&ZFCL9E$FCS' on 'class org.apache.commons.collections4.functors.InstanceofPredicate' does not exist
            //
            verifyException("org.apache.commons.collections4.functors.InvokerTransformer", e);
        }
    }
}