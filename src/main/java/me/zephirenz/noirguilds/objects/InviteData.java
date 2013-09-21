package me.zephirenz.noirguilds.objects;

public class InviteData {

    String sender;
    String invitee;
    Guild guild;
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
