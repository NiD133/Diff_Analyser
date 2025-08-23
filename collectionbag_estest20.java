public class CollectionBag_ESTestTest20 extends CollectionBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        TreeBag<Integer> treeBag0 = new TreeBag<Integer>();
        TreeBag<Object> treeBag1 = new TreeBag<Object>((Collection<?>) treeBag0);
        Transformer<Object, Boolean> transformer0 = InvokerTransformer.invokerTransformer("");
        TransformerPredicate<Object> transformerPredicate0 = new TransformerPredicate<Object>(transformer0);
        NullIsExceptionPredicate<Object> nullIsExceptionPredicate0 = new NullIsExceptionPredicate<Object>(transformerPredicate0);
        PredicatedBag<Object> predicatedBag0 = new PredicatedBag<Object>(treeBag1, nullIsExceptionPredicate0);
        CollectionBag<Object> collectionBag0 = new CollectionBag<Object>(predicatedBag0);
        // Undeclared exception!
        try {
            collectionBag0.add((Object) treeBag1, (-3484));
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // InvokerTransformer: The method '' on 'class org.apache.commons.collections4.bag.TreeBag' does not exist
            //
            verifyException("org.apache.commons.collections4.functors.InvokerTransformer", e);
        }
    }
}