package com.purplebeen.springblog.utills;

import java.util.UUID;

public class UUIDUtill {
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }
}
