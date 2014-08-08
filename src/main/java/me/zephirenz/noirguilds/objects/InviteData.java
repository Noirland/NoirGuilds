package me.zephirenz.noirguilds.objects;

import java.util.UUID;

public class InviteData {

    private final UUID sender;
    private final UUID invitee;
    private final Guild guild;
    public InviteData(UUID sender, UUID invitee, Guild guild) {
        this.sender = sender;
        this.invitee = invitee;
        this.guild = guild;
    }

    public UUID getSender() {
        return sender;
    }
    public UUID getInvitee() {
        return invitee;
    }
    public Guild getGuild() {
        return guild;
    }
}
