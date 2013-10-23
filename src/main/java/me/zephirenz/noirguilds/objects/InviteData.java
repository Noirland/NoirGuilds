package me.zephirenz.noirguilds.objects;

public class InviteData {

    private final String sender;
    private final String invitee;
    private final Guild guild;
    public InviteData(String sender, String invitee, Guild guild) {
        this.sender = sender;
        this.invitee = invitee;
        this.guild = guild;
    }

    public String getSender() {
        return sender;
    }
    public String getInvitee() {
        return invitee;
    }
    public Guild getGuild() {
        return guild;
    }
}
