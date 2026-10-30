# Entity behaviour dispatch and vanilla superclass calls

MCC dispatchers such as `mcc$canAttack`, `mcc$fireImmune` and `mcc$ate` select an optional plugin behaviour and otherwise invoke the entity's ordinary virtual vanilla method. External vanilla call sites use the MCC dispatcher so custom behaviour can replace or retain vanilla logic.

Within the ordinary vanilla method, retain direct superclass delegation:

```java
@Override
public void ate() {
    // Existing SheepRegrowWoolEvent and sheep-specific logic stay in this method.
    super.ate();
    // Continue with wool regrowth and age handling.
}
```

Calling `super.mcc$ate()` there selects the dispatcher inherited by the same sheep. Its fallback calls `asMob().ate()`, which dynamically selects Sheep.ate again. The same loop affects predicates, for example ItemEntity.fireImmune and TamableAnimal.canAttack. These loops occur even without a custom behaviour installed. `super` on an interface dispatcher does not make its later virtual fallback select the native parent method.

The permanent feature patch `0017-Preserve-vanilla-entity-super-calls.patch` restores all 14 affected superclass delegations across 13 entity classes. It retains the external interception points and subtype conditions. Keep dispatcher and vanilla methods distinct when adding new hooks; audit `super.mcc$` calls in generated Minecraft sources after applying patches.

The sibling mcc-util actual-server fixture covers all affected subtype paths: tameable/illager/dolphin attacks, dropped-item/creaking fire immunity, sheep eating, zombie/wither-skeleton item holding, zombie/piglin-brute pickup, raider despawning, wind-charge collisions, and cat/sniffer mating. Additional checks verify that replacements run exactly once, default sheep behaviour still regrows wool and a replacement can suppress regrowth. These checks use actual native entities and Paper dispatch on the isolated server.

Apply and build from the fork root with JDK 25:

```sh
./gradlew :mccreativelab-server:applyMinecraftFeaturePatches
./gradlew :mccreativelab-server:test :mccreativelab-server:createPaperclipJar
```

Use the sibling mcc-util integration guide to run its updated `serverSmokeJar`. The additional entity assertions are exercised only by the opt-in fixture; no temporary mobs or test plugins are added to the manually operated testserver.

## Verification on 2026-10-28

The feature patch reapplied successfully and left the generated native source tree clean. The complete server test task reports 10,178 tests, zero errors/failures and 86 existing upstream skips. The actual extension smoke run passes all 53 assertions, including 18 entity regressions; evidence is in `../update-26.3/entity-super-smoke-result.json`. The rebuilt paperclip is installed in the manual testserver with the prior JAR backed up. A player had successfully joined before this update and exposed the entity errors; a new client session after the update remains a separate observation.
