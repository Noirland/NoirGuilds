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

    public static RankPerm get(String perm) {
        for(RankPerm rp : values()) {
            if(rp.getPerm().equals(perm)) {
                return rp;
            }
        }
        return null;
    }

}
