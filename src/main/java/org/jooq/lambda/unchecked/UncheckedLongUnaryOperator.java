/**
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
package org.jooq.lambda.unchecked;

import java.util.function.Consumer;
import java.util.function.LongUnaryOperator;
import org.jooq.lambda.Unchecked;
import org.jooq.lambda.fi.util.function.CheckedLongUnaryOperator;

/**
 * Aliases of functions in {@link Unchecked} for static import.
 *
 * @author Ersafan Rend
 */
public final class UncheckedLongUnaryOperator {

    /**
     * Alias of {@link Unchecked#longUnaryOperator(CheckedLongUnaryOperator)} for static import.
     */
    public static LongUnaryOperator unchecked(CheckedLongUnaryOperator operator) {
        return Unchecked.longUnaryOperator(operator);
    }

    /**
     * Alias of {@link Unchecked#longUnaryOperator(CheckedLongUnaryOperator, Consumer)} for static import.
     */
    public static LongUnaryOperator unchecked(CheckedLongUnaryOperator operator, Consumer<Throwable> handler) {
        return Unchecked.longUnaryOperator(operator, handler);
    }

    /** No instances */
    private UncheckedLongUnaryOperator() {}
}
