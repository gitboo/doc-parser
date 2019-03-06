package com.wmp.docparser.service;

import com.wmp.docparser.error.ExceededDocSizeException;
import com.wmp.docparser.error.InvalidUriException;
import com.wmp.docparser.error.NetworkErrorException;
import com.wmp.docparser.service.docfilter.DocFilter;
import com.wmp.docparser.service.merger.Merger;
import com.wmp.docparser.service.scanner.AlphabetScanner;
import com.wmp.docparser.service.scanner.DocScanner;
import com.wmp.docparser.service.scanner.NumberScanner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;


@Slf4j
@Service
@AllArgsConstructor
public class DocumentService {

    private RestTemplate restTemplate;

    private DocFilter htmlTagFilter;

    private Merger<DocScanner, Character> alternateMerger;

    public ParsedDocument parseWebDocument(@NotNull final String url, @NotNull final Boolean htmlTagged, @NotNull final Integer bundleCnt) {
        checkArgument(bundleCnt > 0, "bundleCnt must be positive");

        //> Get Document by URI
        String doc = lookup(url);

        //> html tag 포함 여부
        if (!htmlTagged) {
            doc = htmlTagFilter.filter(doc);
        }

        //> 영어,숫자 스캐닝 & 정렬
        char[] tokens = doc.toCharArray();
        DocScanner[] scanners = new DocScanner[2];
        scanners[0] = new AlphabetScanner();
        scanners[1] = new NumberScanner();
        for (char token : tokens) {
            for (DocScanner each : scanners) {
                each.checkAndOffer(token);
            }
        }
        //> 교차 합병
        List<Character> parsedDoc = alternateMerger.merge(scanners);

        //> 반환값 묶음
        int size = parsedDoc.size();
        int remainderNum = size % bundleCnt;
        return ParsedDocument
                .builder()
                .quotient(subListToString(parsedDoc, 0, size - remainderNum))
                .remainder(subListToString(parsedDoc, size - remainderNum, size))
                .build();
    }

    private String subListToString(@NotNull List<Character> parsedDoc, int from, int to) {
        return parsedDoc
                .subList(from, to)
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    private String lookup(@NotNull String url) {
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (ResourceAccessException e) {
            log.error("Invalid Uri {}", e);
            throw new InvalidUriException("Invalid Uri");
        } catch (OutOfMemoryError e) {
            log.error("Exceeded Document Size Error {}", e);
            throw new ExceededDocSizeException("Exceeded Document Size Error");
        } catch (HttpClientErrorException e) {
            log.error("Network Error {}", e);
            throw new NetworkErrorException("Network Error or Invalid Uri");
        }
    }
}
