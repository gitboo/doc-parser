package com.wmp.docparser.service.scanner;

import java.util.Comparator;
import java.util.PriorityQueue;


public abstract class DocScanner extends PriorityQueue<Character> {
    public DocScanner(Comparator<Object> comparingInt) {
        super(comparingInt);
    }
    public DocScanner() {}
    public abstract void checkAndOffer(final char token);
}
