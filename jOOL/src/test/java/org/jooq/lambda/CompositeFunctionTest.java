package org.jooq.lambda;

import org.junit.Test;
import org.jooq.lambda.function.*;

import static junit.framework.TestCase.assertEquals;

public class CompositeFunctionTest {
    @Test
    public void compose() throws Exception {
        //  Test the composite function of the same two functions in different orders
        //  -------------------------------------------------------------------------
        Function1<Integer, Integer> square = t -> t * t;
        Function1<Integer, Integer> addTwo = t -> t + 2;
        assertEquals(11, (int) addTwo.compose(square).apply(3));
        assertEquals(25, (int) square.compose(addTwo).apply(3));

        //  Test the composite function of a function[1] and a function[N]
        //  --------------------------------------------------------------
        Function2<Integer, Integer, Integer> sum = Integer::sum;
        assertEquals(49, (int) square.compose(sum).apply(3, 4));
    }

    @Test
    public void andThen() throws Exception {
        //  Test the composite function of the same two functions in different orders
        //  -------------------------------------------------------------------------
        Function1<Integer, Integer> square = t -> t * t;
        Function1<Integer, Integer> addTwo = t -> t + 2;
        assertEquals(25, (int) addTwo.andThen(square).apply(3));
        assertEquals(11, (int) square.andThen(addTwo).apply(3));

        //  Test the composite function of a function[1] and a function[N]
        //  --------------------------------------------------------------
        Function2<Integer, Integer, Integer> sum = Integer::sum;
        assertEquals(81, (int) sum.andThen(square).apply(4, 5));
    }
}