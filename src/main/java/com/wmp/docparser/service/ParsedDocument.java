package com.wmp.docparser.service;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
public class ParsedDocument {
    private String quotient;
    private String remainder;

    @Builder.Default
    private Instant createdDate = Instant.now();
}
