package me.zephirenz.noirguilds.enums;

public enum RankPerm {

    INVITE("invite"),
    KICK("kick"),
    ADMINCHAT("adminchat"),
    TP("tp"),
    TPHERE("tphere"),
    HQ("hq");

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
