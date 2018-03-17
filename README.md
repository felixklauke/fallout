# fallout
Hassle free and robust minecraft city build engine ready to bring action in your adventure / freebuild server and start the clash of cities and clans. Provides and extensive API and easily integrates within your existing environment with support for many other plugins.

# API Example

## 1. Obtain application instance:
```java
FalloutSpigotPlugin falloutPlugin = Bukkit.getPluginManager().getPlugin("fallout");
FalloutSpigotApplication falloutApplication = falloutPlugin.getFalloutApplication();
```

## 2. Get specific controller:

```java
KingdomController kingdomController = falloutApplication.getKingdomController();
```

## 3. Execute some cool actions.

_Creating a kingdom:_

Note: When you create a kingdom you have to provide the player who creates the kingdom and the location of the
initial holding the kingdom will use as capital. 

```java
UUID kingdomUniqueId = UUID.randomUUID();
UUID playerUniqueId = UUID.randomUUID();
String kingdomName = "CoolKingdom";
String kingdomDescription = "Another cool kingdom";

kingdomController.createKingdom(kingdomUniqueId, playerUniqueId, kingdomName, kingdomDescription, worldName, chunkX, chunkZ, new Consumer<Boolean>() {
    @Override
    public void accept(Boolean result) {
        // callback
    }
});
```