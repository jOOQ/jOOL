/**
 * Copyright (c) 2014, Data Geekery GmbH, contact@datageekery.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.lambda.generator

import java.io.{File, PrintWriter}

object Generator {
  def main(args: Array[String]) {
    val max = 8;
    val copyright = """/**
 * Copyright (c) 2014, Data Geekery GmbH, contact@datageekery.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
"""
    write(
      "src/main/java/org/jooq/lambda/tuple/Tuple.java",
      s"""$copyright
package org.jooq.lambda.tuple;

import java.util.List;

/**
 * A tuple.
 *
 * @author Lukas Eder
 */
public interface Tuple extends Iterable<Object> {
${(for (degree <- (1 to max)) yield s"""
    /**
     * Construct a tuple of degree $degree.
     */
    public static <${TN(degree)}> Tuple$degree<${TN(degree)}> tuple(${TN_vn(degree)}) {
        return new Tuple$degree<>(${vn(degree)});
    }
""").mkString}
    /**
     * Get an array representation of this tuple.
     */
    Object[] array();

    /**
     * Get a list representation of this tuple.
     */
    List<?> list();

    /**
     * The degree of this tuple.
     */
    int degree();
}
"""
    )

    for (degree <- 1 to max) {
      write(
        s"src/main/java/org/jooq/lambda/tuple/Tuple$degree.java",
        s"""$copyright
package org.jooq.lambda.tuple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jooq.lambda.function.Function$degree;

/**
 * A tuple of degree $degree.
 *
 * @author Lukas Eder
 */
public final class Tuple$degree<${TN(degree)}> implements Tuple, Comparable<Tuple$degree<${TN(degree)}>>, Serializable {
    ${(for (d <- (1 to degree)) yield s"""
    public final T$d v$d;""").mkString}

    public Tuple$degree(${TN_vn(degree)}) {${(for (d <- (1 to degree)) yield s"""
        this.v$d = v$d;""").mkString}
    }
    ${if (degree == 2) s"""
    public Tuple2<T2, T1> swap() {
        return new Tuple2<>(v2, v1);
    }
    """ else ""}
    public <R> R map(Function$degree<R, ${TN(degree)}> function) {
        return function.apply(this);
    }

    @Override
    public Object[] array() {
        return new Object[] { ${(for (d <- 1 to degree) yield s"v$d").mkString(", ")} };
    }

    @Override
    public List<?> list() {
        return Arrays.asList(array());
    }

    @Override
    public int degree() {
        return $degree;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<Object> iterator() {
        return (Iterator<Object>) list().iterator();
    }

    @Override
    public int compareTo(Tuple$degree<${TN(degree)}> other) {
        int result = 0;
        ${(for (d <- 1 to degree) yield s"""
        result = ((Comparable) v$d).compareTo((Comparable) other.v$d); if (result != 0) return result;""").mkString}

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;

        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Tuple$degree<${TN(degree)}> that = (Tuple$degree) o;
        ${(for (d <- 1 to degree) yield s"""
        if (v$d != that.v$d) {
            if (v$d == null ^ that.v$d == null)
                return false;

            if (!v$d.equals(that.v$d))
                return false;
        }
        """).mkString}
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        ${(for (d <- 1 to degree) yield s"""
        result = prime * result + ((v$d == null) ? 0 : v$d.hashCode());""").mkString}

        return result;
    }

    @Override
    public String toString() {
        return "("${(for (d <- (1 to degree)) yield s"""
             + ${if (d > 1) """", " + """ else """       """}v$d""").mkString}
             + ")";
    }
}
"""
      )
    }

    for (degree <- 1 to max) {
      write(
        s"src/main/java/org/jooq/lambda/function/Function$degree.java",
        s"""$copyright
package org.jooq.lambda.function;

${if      (degree == 1) "import java.util.function.Function;"
  else if (degree == 2) "import java.util.function.BiFunction;"
  else ""}
import static org.jooq.lambda.tuple.Tuple.tuple;
import org.jooq.lambda.tuple.Tuple$degree;

/**
 * A function with $degree arguments
 *
 * @author Lukas Eder
 */
public interface Function$degree<R, ${TN(degree)}> {

    /**
     * Apply this function to the arguments.
     */
    R apply(Tuple$degree<${TN(degree)}> args);

    /**
     * Apply this function to the arguments.
     */
    default R apply(${TN_vn(degree)}) {
        return apply(tuple(${vn(degree)}));
    }
    ${if (degree == 1) s"""
    /**
     * Convert this function to a {@link java.util.function.Function}
     */
    default Function<T1, R> toFunction() {
        return t -> apply(t);
    }

    /**
     * Convert to this function from a {@link java.util.function.Function}
     */
    static <R, T1> Function1<R, T1> from(Function<T1, R> function) {
        return args -> function.apply(args.v1);
    }
    """ else if (degree == 2) s"""
    /**
     * Convert this function to a {@link java.util.function.BiFunction}
     */
    default BiFunction<T1, T2, R> toBiFunction() {
        return (t1, t2) -> apply(t1, t2);
    }

    /**
     * Convert to this function to a {@link java.util.function.BiFunction}
     */
    static <R, T1, T2> Function2<R, T1, T2> from(BiFunction<T1, T2, R> function) {
        return args -> function.apply(args.v1, args.v2);
    }
    """
        else ""}
}
"""
      )
    }
  }

  def write(file : String, text : String) = {
    println("Writing " + file)
    val w = new PrintWriter(new File(file))

    w.print(text)
    w.flush
    w.close
  }

  def TN   (degree : Int)               : String = xxxn(degree, "T")
  def vn   (degree : Int)               : String = xxxn(degree, "v")
  def xxxn (degree : Int, xxx : String) : String = (1 to degree).map(i => xxx + i).mkString(", ")
  def TN_vn(degree : Int)               : String = (1 to degree).map(i => "T" + i + " v" + i).mkString(", ")
}