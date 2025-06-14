/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kindbgen.javarules.utils;

import java.util.Iterator;

public final class Comparables {

    private Comparables() {
        // intentionally empty
    }

    public static <T extends Comparable<? super T>> boolean isOrdered(Iterable<T> list) {
        Iterator<T> i = list.iterator();
        if (i.hasNext()) {
            T previous = i.next();
            while (i.hasNext()) {
                T current = i.next();
                if (previous.compareTo(current) > 0) {
                    return false;
                }
                previous = current;
            }
        }
        return true;
    }
}
