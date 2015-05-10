/**
 * Copyright (c) 2014-2015, Data Geekery GmbH, contact@datageekery.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jooq.lambda;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jooq.lambda.function.Function0;
import org.jooq.lambda.function.Function1;
import org.jooq.lambda.function.Function2;
import org.jooq.lambda.function.Function3;
import org.jooq.lambda.function.Function4;
import org.jooq.lambda.function.Function5;
import org.jooq.lambda.function.Function6;
import org.jooq.lambda.function.Function7;
import org.jooq.lambda.function.Function8;


/**
 * A single entry-point place to put all jOOL utility methods.
 *
 */
public class JOOL {

    /**
     * A helper method for "lifting" a method into a function.
     * @param func
     * @return
     */
    public static <R> Function0<R> lift(Function0<R> func){
        return func;
    }

    public static <R, T1> Function1<R, T1> lift(Function<R, T1> func){
        return Function1.from(func);
    }

    public static <R, T1, T2> Function2<R, T1, T2> lift(BiFunction<R, T1, T2> func){
        return Function2.from(func);
    }

    public static <R, T1, T2, T3> Function3<R, T1, T2, T3> lift(Function3<R, T1, T2, T3> func){
        return func;
    }

    public static <R, T1, T2, T3, T4> Function4<R, T1, T2, T3, T4> lift(Function4<R, T1, T2, T3, T4> func){
        return func;
    }

    public static <R, T1, T2, T3, T4, T5> Function5<R, T1, T2, T3, T4, T5> lift(Function5<R, T1, T2, T3, T4, T5> func){
        return func;
    }

    public static <R, T1, T2, T3, T4, T5, T6> Function6<R, T1, T2, T3, T4, T5, T6> lift(Function6<R, T1, T2, T3, T4, T5, T6> func){
        return func;
    }

    public static <R, T1, T2, T3, T4, T5, T6, T7> Function7<R, T1, T2, T3, T4, T5, T6, T7> lift(Function7<R, T1, T2, T3, T4, T5, T6, T7> func){
        return func;
    }

    public static <R, T1, T2, T3, T4, T5, T6, T7, T8> Function8<R, T1, T2, T3, T4, T5, T6, T7, T8> lift(Function8<R, T1, T2, T3, T4, T5, T6, T7, T8> func){
        return func;
    }

    public static <R> Function0<R> lift0(Function0<R> func){
        return func;
    }

    public static <R, T1> Function1<R, T1> lift1(Function<R, T1> func){
        return Function1.from(func);
    }

    public static <R, T1, T2> Function2<R, T1, T2> lift2(BiFunction<R, T1, T2> func){
        return Function2.from(func);
    }

    public static <R, T1, T2, T3> Function3<R, T1, T2, T3> lift3(Function3<R, T1, T2, T3> func){
        return func;
    }

    public static <R, T1, T2, T3, T4> Function4<R, T1, T2, T3, T4> lift4(Function4<R, T1, T2, T3, T4> func){
        return func;
    }

    public static <R, T1, T2, T3, T4, T5> Function5<R, T1, T2, T3, T4, T5> lift5(Function5<R, T1, T2, T3, T4, T5> func){
        return func;
    }

    public static <R, T1, T2, T3, T4, T5, T6> Function6<R, T1, T2, T3, T4, T5, T6> lift6(Function6<R, T1, T2, T3, T4, T5, T6> func){
        return func;
    }

    public static <R, T1, T2, T3, T4, T5, T6, T7> Function7<R, T1, T2, T3, T4, T5, T6, T7> lift7(Function7<R, T1, T2, T3, T4, T5, T6, T7> func){
        return func;
    }

    public static <R, T1, T2, T3, T4, T5, T6, T7, T8> Function8<R, T1, T2, T3, T4, T5, T6, T7, T8> lift8(Function8<R, T1, T2, T3, T4, T5, T6, T7, T8> func){
        return func;
    }

    public static <T1> Consumer<T1> lift(Consumer<T1> func){
        return func;
    }

    public static <T1, T2> BiConsumer<T1, T2> lift(BiConsumer<T1, T2> func){
        return func;
    }

    public static <T1> Consumer<T1> lift1(Consumer<T1> func){
        return func;
    }

    public static <T1, T2> BiConsumer<T1, T2> lift2(BiConsumer<T1, T2> func){
        return func;
    }

    public static Runnable lift(Runnable func){
        return func;
    }

    /**
     * A helper method for "lifting" a method into a Predicate.
     * Can also be used to convert a Function that returns Boolean into a Predicate.
     * @param func
     * @return
     */
    public static <T> Predicate<T> liftPredicate(Function<T, Boolean> func){
        return func::apply;
    }






}
