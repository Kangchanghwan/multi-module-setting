package com.example.apiuser.config.document.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface DocumentLinkGenerator {

    static String generateLinkCode(DocUrl docUrl) {
        return String.format("link:common/%s.html[%s %s,role=\"popup\"]", docUrl.pageId, docUrl.text, "코드");
    }

    static String generateText(DocUrl docUrl) {
        return String.format("%s %s", docUrl.text, "코드명");
    }

    // 필요한 url 쭉 명시
    @RequiredArgsConstructor
    enum DocUrl {
        SOCIAL_TYPE("social-type","구분")
        ;

        private final String pageId;
        @Getter
        private final String text;
    }
}
