/*
 * Copyright (c) 2015, 2017 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0
 * GNU General Public License version 2
 * GNU Lesser General Public License version 2.1
 */
package org.truffleruby.core.array;

public class EmptyArrayMirror extends BasicArrayMirror {

    public static final ArrayMirror INSTANCE = new EmptyArrayMirror();

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public Object get(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void set(int index, Object value) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public ArrayMirror copyArrayAndMirror() {
        return INSTANCE;
    }

    @Override
    public ArrayMirror copyArrayAndMirror(int newLength) {
        return INSTANCE;
    }

    @Override
    public void copyTo(ArrayMirror destination, int sourceStart, int destinationStart, int count) {
        assert sourceStart == 0 && count == 0;
    }

    @Override
    public void copyTo(Object[] destination, int sourceStart, int destinationStart, int count) {
        assert sourceStart == 0 && count == 0;
    }

    @Override
    public ArrayMirror extractRange(int start, int end) {
        assert start == 0 && end == 0;
        return INSTANCE;
    }

    public void sort(int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getArray() {
        return null;
    }

}
