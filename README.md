# RandomWelcomeRewards
## Brief Description
RandomWelcomeRewards (or RandWelRewards for short) is a spigot plugin every server needs to allow players to welcome new (or returning) players with either funny,
strange, or weird messages. This plugin is based off of Ranulls "Welcome" plugin but with a lot of twists and extra features available, Whenever a player 
either types "welcome" or "wb," the plugin will generate a random message from the list in the "messages.yml" file and make it look like the player 
took the time and effort to write something nice (or bad) to the welcoming player. Each time they welcome a new or returning player, they can earn rewards 
like a diamond, in game currency, or being publicly humilated (the best part). If a player gains enough welcome points, they will get a milestone
everytime they hit a certain threshold configurable in the "milestones.yml" section. Players can keep count on their score through the stats command (see commands
section) and through leaderboards from either in chat or in a hologram (more on this later). Well, what are you waiting for. It's time for players to actually
start welcoming new players in a funnt way while also earning rewards along side it!!
## Plugin Information
### Server Version
The API is built off of 1.18, but should be able to go up towards 1.13. If theres enough requests, I can upload a 1.12 and below version (more than likely up to
1.8). However, it will get limited updates and not much support from me since the native version of this plugin is 1.13 and beyond.
### Dependicies
No Dependices are required for this plugin, just drag and drop into the plugins and restart your server.
### Soft Dependicies
#### Vault
Vault can be used to give in-game currency and usually helps with most chat permission plugins. You can download the latest version of vault through here: 
https://dev.bukkit.org/projects/vault
#### PlaceholderAPI
PlaceholderAPI can be used to project the top welcoming points a player has or their current score on a hologram plugin 
(like HolographicDisplays, GHolos, DecentHolograms, etc..). You can find the latest version of PlaceholderAPI here: https://www.spigotmc.org/resources/placeholderapi.6245/
### Languages
The only language this plugin comes with is English, however, everything is fully translatable in the "messages.yml." Just to remember
to reload the plugin for the changes to take effect.
### Hex Color Support
Not only can use the "&" color placeholder, but you can also use hex colors, the "messages.yml" file will show what you
need to do, otherwise its pretty simple.
## Commands
### /randwelrewards stats
Shows the players new and return welcome points they have.
default: true
### /randwelrewards leaderboards <type>
Displays the current ranking of either type, this can be used if you don't want to use holograms.
default: true
### /randwelrewards stats <playername>
Shows the players new and return welcome points another player has.
default: false
### /randwelrewards setstats <playername> <type> <amount>
Set the player stat for either type in game.
default: false
### /randwelrewards reload
Reloads the plugin.
default: false
## Permissions
### - randomwelcomerewards.reload
The permission for a player to reload the plugin.
### - randomwelcomerewards.stats.others
The permission to allow the player to look at other players stats.
### - randomwelcomerewards.setstats
The permission to allow the player to change the stat of another player.
## PAPI Placeholders
### - %randwelrewards_newwelcome_score%
Returns the players current New Welcome points.
### - %randwelrewards_return_welcome_score%
Returns the players current Return Welcome points.
### - %randwelrewards_top_<newwelcome/returnwelcome>_<name/amount>_<position>%
Returns either the players name or amount based on either newwelcome points or returnwelcome points at that position (Mainly used for leaderboards).
## Final Notes
If anyone has any questions, issues, or problems regarding this plugin. Please let me know and I'll get back to you
as soon as possible, thank you and enjoy seeing those funny messages!!


  
