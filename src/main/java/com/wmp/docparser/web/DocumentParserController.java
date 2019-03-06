package com.wmp.docparser.web;


import com.wmp.docparser.service.DocumentService;
import com.wmp.docparser.service.ParsedDocument;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * TODO
 * 1. 모든 문장 입력 가능
 * 2. 영어/ 숫자만 출력
 * 3 order
 * 3.1 영어: A,a,B,b,C,c....
 * 3.2 숫자: 0,1,2,3
 * 4. 교차출력: 영어, 숫자, 영어, 숫자
 * 5. 출력 묶음 단위
 */
@Slf4j
@RestController
@AllArgsConstructor
public class DocumentParserController {

    private DocumentService documentService;

    @CrossOrigin
    @GetMapping(path = "/docs/parsing")
    public ParsedDocument parseDocument(
            @RequestParam(value = "uri", required = true) String uri,
            @RequestParam(value = "htmlTag", defaultValue = "true") Boolean htmlTag,
            @RequestParam(value = "bundles") Integer bundles) {
        checkArgument(bundles > 0, "bundles must be positive");
        log.info("Request uri=[{}], htmlTag=[{}],  bundles=[{}]", uri, htmlTag, bundles);
        return documentService.parseWebDocument(uri, htmlTag, bundles);
    }
}
