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
import java.util.function.DoubleConsumer;
import org.jooq.lambda.Unchecked;
import org.jooq.lambda.fi.util.function.CheckedDoubleConsumer;

/**
 * Aliases of functions in {@link Unchecked} for static import.
 *
 * @author Ersafan Rend
 */
public final class UncheckedDoubleConsumer {

    /**
     * Alias of {@link Unchecked#doubleConsumer(CheckedDoubleConsumer)} for static import.
     */
    public static DoubleConsumer unchecked(CheckedDoubleConsumer consumer) {
        return Unchecked.doubleConsumer(consumer);
    }

    /**
     * Alias of {@link Unchecked#doubleConsumer(CheckedDoubleConsumer, Consumer)} for static import.
     */
    public static DoubleConsumer unchecked(CheckedDoubleConsumer consumer, Consumer<Throwable> handler) {
        return Unchecked.doubleConsumer(consumer, handler);
    }

    /** No instances */
    private UncheckedDoubleConsumer() {}
}
