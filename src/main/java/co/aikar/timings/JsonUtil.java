/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Daniel Ennis <http://aikar.co>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.aikar.timings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.*;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class JsonUtil {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static JsonArray toArray(Object... objects) {
        List array = new ArrayList();
        Collections.addAll(array, objects);
        return GSON.toJsonTree(array).getAsJsonArray();
    }

    public static JsonObject toObject(Object object) {
        return GSON.toJsonTree(object).getAsJsonObject();
    }

    public static <E> JsonObject mapToObject(Iterable<E> collection, Function<E, JSONPair> mapper) {
        Map object = new LinkedHashMap();
        for (E e : collection) {
            JSONPair pair = mapper.apply(e);
            if (pair != null) {
                object.put(pair.key, pair.value);
            }
        }
        return GSON.toJsonTree(object).getAsJsonObject();
    }

    public static <E> JsonArray mapToArray(E[] elements, Function<E, Object> mapper) {
        ArrayList array = new ArrayList();
        Collections.addAll(array, elements);
        return mapToArray(array, mapper);
    }

    public static <E> JsonArray mapToArray(Iterable<E> collection, Function<E, Object> mapper) {
        List array = new ArrayList();
        for (E e : collection) {
            Object obj = mapper.apply(e);
            if (obj != null) {
                array.add(obj);
            }
        }
        return GSON.toJsonTree(array).getAsJsonArray();
    }

    public static class JSONPair {
        public final String key;
        public final Object value;

        public JSONPair(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public JSONPair(int key, Object value) {
            this.key = String.valueOf(key);
            this.value = value;
        }
    }
}
