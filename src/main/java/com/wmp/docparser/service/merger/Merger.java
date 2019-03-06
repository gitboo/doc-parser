package com.wmp.docparser.service.merger;

import java.util.List;

public interface Merger<E, T> {
    List<T> merge(E[] e);
}
