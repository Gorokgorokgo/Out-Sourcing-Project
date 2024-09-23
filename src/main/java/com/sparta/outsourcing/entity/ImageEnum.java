package com.sparta.outsourcing.entity;

public enum ImageEnum {
    REVIEW(Authority.REVIEW),
    STORE(Authority.STORE),
    MENU(Authority.MENU);

    private final String authority;

    ImageEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String REVIEW = "REVIEW";
        public static final String STORE = "STORE";
        public static final String MENU = "MENU";
    }
}