package com.wmp.docparser.service.merger;

import com.wmp.docparser.service.scanner.DocScanner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;


@Component
public class AlternateMerger implements Merger<DocScanner, Character> {
    /**
     * 두 배열의 값을 벌갈아 가며 하나의 리스트로 합친다.
     * 이 때 인덱스 빠른 것이 먼저 오게 한다.
     */
    public List<Character> merge(DocScanner[] scanners) {
        OptionalInt max = Arrays.stream(scanners).mapToInt(DocScanner::size).max();
        if (!max.isPresent()) {
            return Collections.unmodifiableList(new ArrayList<>());
        }
        List<Character> mergedList = new ArrayList<>();
        for (int i = 0, size = max.getAsInt(); i < size; i++) {
            for (DocScanner each : scanners) {
                if (!each.isEmpty()) {
                    mergedList.add(each.poll());
                }
            }
        }
        return Collections.unmodifiableList(mergedList);
    }
}
