package com.wmp.docparser.service.docfilter;


import org.springframework.stereotype.Component;

@Component
public class HtmlTagFilter implements DocFilter {

    @Override
    public String filter(String source) {
        return source.replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
    }
}
