package com.wmp.docparser.service.scanner;

import com.google.common.collect.ImmutableMap;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * 알바벳 문자만 queue 에 순서대로 저장한다.
 */
public class AlphabetScanner extends DocScanner {

    private static ImmutableMap<Character, Integer> orderMap;

    // 알파벳 정렬을 위한 참고 Map
    static {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0, upper = 'A', lower = 'a'; i < 52; i++) {
            if (i % 2 == 0) {
                map.put((char) upper++, i);
            } else {
                map.put((char) lower++, i);
            }
        }
        orderMap = ImmutableMap.copyOf(map);
    }

    public AlphabetScanner() {
        super(Comparator.comparingInt(a -> orderMap.get(a)));
    }

    @Override
    public void checkAndOffer(char token) {
        if (!isAlphabet(token)) {
            return;
        }
        offer(token);
    }

    private boolean isAlphabet(char token) {
        return (65 <= token && token <= 90)
                || (97 <= token && token <= 122);
    }
}
