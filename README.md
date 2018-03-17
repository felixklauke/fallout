# fallout
Hassle free and robust minecraft city build engine ready to bring action in your adventure / freebuild server and start the clash of cities and clans. Provides and extensive API and easily integrates within your existing environment with support for many other plugins.

# API Example

1. Obtain application instance:
```java
FalloutSpigotPlugin falloutPlugin = Bukkit.getPluginManager().getPlugin("fallout");
FalloutSpigotApplication falloutApplication = falloutPlugin.getFalloutApplication();
```

2. Get specific controller:
1. Obtain application instance:
```java
KingdomController kingdomController = falloutApplication.getKingdomController();
kingdomController.createKingdom(UUID.randomUUID(), "NiceKingdom", "Nice Kingdom", new Consumer<Boolean>() {
        @Override
        public void accept(Boolean success) {
            if (success) {
                // Created!
                return;
            }

            // Not created ;(
        }
    });
```