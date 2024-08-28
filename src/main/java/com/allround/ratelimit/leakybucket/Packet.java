package com.allround.ratelimit.leakybucket;
class Packet {
    private final String data;

    public Packet(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
