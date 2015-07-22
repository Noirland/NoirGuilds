# NoirGuilds #

Create and manage guilds, with features like ranks, permissions, banks and more.

* Create a guild with a name and 4 character tag
* Create and modify ranks, with their own unique permissions
* Designate a guild HQ, where other guild members can teleport to
* General and Admin chats for guilds, for private group chats
* Guild banks, where members can donate money, which leaders can use (links into [BankOfNoir](//github.com/ZephireNZ/BankOfNoir))
* MOTD text, to be shown on player login
* Configureable guild size limits, which leaders can pay to upgrade using guild balance

## Configuration ##

```
noirguilds:
  initial-member-limit: 10 # Maximum limit for new guilds
  upgrade:
    initial-cost: 10000 # Cost to upgrade from inital to inital + 1 members
    multiplier: 1.3 # Cost multiplier, applied to each consecutive upgrade, 2nd upgrade 13000, 3rd 16900, etc
  kill-money: 10 # Money transferred from one guild to another when killed in a guild PVP arena (see WGNoirFlags)
  db:
    host: localhost
    port: 3306
    username: noirguilds
    password: 'guilds'
    name: noirguilds
    prefix: guild
```

## Commands ##

### Guild Management ###

`/guild create [name] [tag]`

Creates a guild with the specified name and tag (must be unique)

`/guild disband`

Disbands (deletes) your guild

`/guild edit name [name]`

Changes the name of the guild (must be unique)

`/guild edit tag [tag]`

Changes the tag of the guild (must be unique and maximum of 4 characters)

`/guild bank`

Shows the guild's bank and its contents

`/guild kick [player]`

Removes a player from the guild

`/guild leave`

Leave the current guild

`/guild motd [line] [value]`

Edits the guild's MOTD, for the given line number

`/guild pay [guild] [amount]`

Pay another guild from the guild's bank

`/guild upgrade`

Upgrade a guild's size by 1

`/guild invite [player]`

Invites a player to join a guild. The player cannot already be in a guild, and must be online.

`/guild accept`

Accept an invitation if one is active

`/guild deny`

Denies an invitation if one is active

### Rank Management ###

`/grank list`

Lists all ranks in the guild

`/grank create [rank]`

Adds a new rank to the guild, with default white colour

`/grank delete [rank]`

Deletes a rank, and moves all its members to the default rank

`/grank set [player] [rank]`

Changes the given player's rank

`/grank edit [rank] colour [colour]`

Changes the colour of a rank valid colours are:

* AQUA
* BLACK
* BLUE
* BOLD
* DARK_AQUA
* DARK_BLUE
* DARK_GRAY
* DARK_GREEN
* DARK_PURPLE
* DARK_RED
* GOLD
* GRAY
* GREEN
* ITALIC
* LIGHT_PURPLE
* MAGIC
* RED
* STRIKETHROUGH
* UNDERLINE
* WHITE
* YELLOW

`/grank edit [rank] name [name]`

Change the name of a rank

`/grank edit [rank] [perm] [value]`

Changes the value of a guild permission (see guild permisions below)

### Admin Commands ###

`/guild create [name] [tag] [leader]`

Creates a guild whose leader is another player

`/guild bank [guild]`

Shows the given guild's bank, and allows for adding/removing funds

`/guild disband [guild]`

Disbands the given guild

---

`/guild info [name/tag]`

Shows information about a guild

* Name and tag
* Number of members and the maximum number
* Guild leader(s)
* All members
* Bank balance

`/g [message]`

Chat with other guild members

`/ga [message]` 

Guild admin chat, to chat with other "high level" guild members

`/guilds`

Lists all guilds and their tags

`/hq`

Teleports to the guild's set HQ

`/hq set`

Sets the HQ to the current position and facing

Only the leader may run this command



## Guild Permissions ##

All permissions default to false for new ranks. Leaders have all permissions (including exclusive access to some admin commands)

`invite` - Allows a member to invite other players to the guild

`kick` - Allows for removing a member from the guild

`adminchat` - ALlows member to send/recieve messages in admin chat

`hq` - Allows access to guild HQ

`withdraw` - Allows for member to withdraw money from a guilds bank (all members can add money)

`pay` - Allows member to pay another guild from the guild's bank

## Permissions ##

`noirguilds.create` - Allows for creation of a guild

`noirguilds.create.other` - Allows for an admin to create a guild without another player as leader

`noirguilds.disband.other` - Allows for admin to disband another guild

`noirguilds.bank.other` - Allows an admin to see a guild's bank