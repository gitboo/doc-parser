package com.wmp.docparser;

import com.wmp.docparser.service.DocumentService;
import com.wmp.docparser.service.ParsedDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DocParserApplicationTests {

    @Autowired
    DocumentService documentService;

    @Test
    public void documentServiceTest() {
        ParsedDocument pd = documentService.parseWebDocument("https://kr.vuejs.org", true, 3);
        assertThat(pd).isNotNull();
    }
}

