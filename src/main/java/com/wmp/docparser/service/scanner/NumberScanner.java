package com.wmp.docparser.service.scanner;

public class NumberScanner extends DocScanner {

    public NumberScanner() {
        super();
    }

    @Override
    public void checkAndOffer(final char token) {
        if (!Character.isDigit(token)) {
            return;
        }
        offer(token);
    }
}
