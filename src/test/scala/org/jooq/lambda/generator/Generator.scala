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

/**
 * A tuple.
 *
 * @author Lukas Eder
 */
public interface Tuple {
${(for (degree <- (1 to max)) yield s"""
    /**
     * Construct a tuple of degree $degree.
     */
    public static <${TN(degree)}> Tuple$degree<${TN(degree)}> tuple(${TN_vn(degree)}) {
        return new Tuple$degree<>(${vn(degree)});
    }
""").mkString("")}
}
"""
    )

    for (degree <- 1 to max) {
      write(
        s"src/main/java/org/jooq/lambda/tuple/Tuple$degree.java",
        s"""$copyright
package org.jooq.lambda.tuple;

/**
 * A tuple of degree $degree.
 *
 * @author Lukas Eder
 */
public final class Tuple$degree<${TN(degree)}> implements Tuple {
    ${(for (d <- (1 to degree)) yield s"""
    public final T$d v$d;""").mkString("")}

    public Tuple$degree(${TN_vn(degree)}) {${(for (d <- (1 to degree)) yield s"""
        this.v$d = v$d;""").mkString("")}
    }
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

  def TN   (degree : Int) : String = (1 to degree).map(i => "T" + i).mkString(", ")
  def vn   (degree : Int) : String = (1 to degree).map(i => "v" + i).mkString(", ")
  def TN_vn(degree : Int) : String = (1 to degree).map(i => "T" + i + " v" + i).mkString(", ")
}