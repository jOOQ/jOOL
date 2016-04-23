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
import java.util.function.Supplier;
import org.jooq.lambda.Unchecked;
import org.jooq.lambda.fi.util.function.CheckedSupplier;

/**
 * Aliases of functions in {@link Unchecked} for static import.
 *
 * @author Ersafan Rend
 */
public final class UncheckedSupplier {

    /**
     * Alias of {@link Unchecked#supplier(CheckedSupplier)} for static import.
     */
    public static <T> Supplier<T> unchecked(CheckedSupplier<T> supplier) {
        return Unchecked.supplier(supplier);
    }

    /**
     * Alias of {@link Unchecked#supplier(CheckedSupplier, Consumer)} for static import.
     */
    public static <T> Supplier<T> unchecked(CheckedSupplier<T> supplier, Consumer<Throwable> handler) {
        return Unchecked.supplier(supplier, handler);
    }

    /** No instances */
    private UncheckedSupplier() {}
}
