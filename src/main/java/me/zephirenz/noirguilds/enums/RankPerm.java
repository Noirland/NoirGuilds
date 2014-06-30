package me.zephirenz.noirguilds.enums;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum RankPerm {

    INVITE("invite"),
    KICK("kick"),
    ADMINCHAT("adminchat"),
    TP("tp"),
    TPHERE("tphere"),
    HQ("hq"),
    BANK_WITHDRAW("withdraw"),
    PAY("pay");

    public static final Map<RankPerm, Boolean> defaults = new ImmutableMap.Builder<RankPerm, Boolean>()
        .put(INVITE, false)
        .put(KICK, false)
        .put(ADMINCHAT, false)
        .put(TP, false)
        .put(TPHERE, false)
        .put(HQ, false)
        .put(BANK_WITHDRAW, false)
        .put(PAY, false)
        .build();

    private final String perm;

    RankPerm(final String perm) {
        this.perm = perm;
    }

    public static RankPerm get(String perm) {
        for(RankPerm rp : values()) {
            if(rp.getPerm().equalsIgnoreCase(perm)) {
                return rp;
            }
        }
        throw new IllegalArgumentException("No rank perm with value " + perm);
    }

    public String getPerm() {
        return perm;
    }
}
