/*
 * Copyright (c) 2013, 2017 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0
 * GNU General Public License version 2
 * GNU Lesser General Public License version 2.1
 */
package org.truffleruby.core.array;

import com.oracle.truffle.api.object.DynamicObject;
import org.truffleruby.Layouts;
import org.truffleruby.language.RubyGuards;

public class ArrayGuards {

    // Partial Escape Analysis only considers < 32 elements
    public static final int ARRAY_MAX_EXPLODE_SIZE = 16;

    // Enough to handle all array strategies (all types, plus null and Object[] without longs).
    public static final int STORAGE_STRATEGIES = 6;

    // Enough to handle all combinations of two strategies.
    public static final int ARRAY_STRATEGIES = STORAGE_STRATEGIES * STORAGE_STRATEGIES;

    // Storage strategies

    public static boolean isIntArray(DynamicObject array) {
        assert RubyGuards.isRubyArray(array);
        return Layouts.ARRAY.getStore(array) instanceof int[];
    }

    public static boolean isLongArray(DynamicObject array) {
        assert RubyGuards.isRubyArray(array);
        return Layouts.ARRAY.getStore(array) instanceof long[];
    }

    public static boolean isDoubleArray(DynamicObject array) {
        assert RubyGuards.isRubyArray(array);
        return Layouts.ARRAY.getStore(array) instanceof double[];
    }

    public static boolean isObjectArray(DynamicObject array) {
        assert RubyGuards.isRubyArray(array);
        final Object store = Layouts.ARRAY.getStore(array);
        return store != null && store.getClass() == Object[].class;
    }

    // Higher level properties

    public static boolean isEmptyArray(DynamicObject array) {
        assert RubyGuards.isRubyArray(array);
        return Layouts.ARRAY.getSize(array) == 0;
    }

}
