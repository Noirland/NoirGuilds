package me.zephirenz.noirguilds.enums;

public enum RankPerm {

    INVITE("invite"),
    KICK("kick"),
    ADMINCHAT("adminchat"),
    TP("tp"),
    TPHERE("tphere");

    private final String perm;

     RankPerm(final String perm) {
         this.perm = perm;
     }

    public String getPerm() {
        return perm;
    }
}
