# Login advancement event handler fix (2026-10-28)

A real authenticated connection by YourServerPromo to the local test server was rejected with "Failed to verify username". The console stack trace identified a NullPointerException in PaperEventManager.callEvent: PlayerAdvancementDataLoadEvent.getHandlers returned null. The login routine had already received a non-null authenticated profile; the exception happened inside callPlayerPreLoginEvents -> PlayerDataLoaderHelper.load -> loadAdvancements. Its generic exception handler then emitted the misleading authentication message.

Both PlayerAdvancementDataLoadEvent and PlayerAdvancementDataSaveEvent had this unfinished implementation. Each now owns a separate static HandlerList and exposes the public static getHandlerList method required for listener registration. Instance getHandlers returns that same list. Payload setters, cancellation, UUIDs and synchronous/asynchronous constructor behavior are retained. Online authentication is left enabled.

Two API regressions cover both constructors, non-null/shared instance/static handler identity, separate load/save lists, async flags, load payload replacement and save cancellation. The sibling mcc-util native fixture additionally registers real listeners, dispatches a load event on a worker thread, verifies its replacement payload and separately dispatches/cancels a synchronous save event. These exercise Paper's actual event manager, which was the source of the login exception. They do not simulate an authenticated client session; a real client retry is still needed to establish complete joining/rendering behavior.

Build with JDK 25:

```sh
./gradlew :mccreativelab-api:test --tests io.papermc.paper.event.data.PlayerAdvancementDataEventTest :mccreativelab-server:createPaperclipJar
```

Run the updated sibling mcc-util serverSmokeJar with this paperclip artifact, as described in `mcc-util/docs/update-26.3/INTEGRATION.md`. The extension run now expects 35 checks (the earlier 33 plus the two event-dispatch regressions). The manual `testserver` uses a backup of the prior JAR before replacement and a normal stop/start; its whitelist, online-mode setting and worlds are retained.

Validation: 489 API tests, zero failures/errors and two existing upstream skips; the new tests contribute two of those tests. The actual server fixture passes all 35 assertions. The corrected paperclip is deployed to the manual testserver, which completed startup and serves its pack correctly with online-mode=true. Its previous JAR is under `testserver/backups/`; deployment hashes and verification are in `testserver/artifacts.json` and `login-fix-verification.json`. Full interactive console handling is enabled, and scripted commands clear pending input before sending their command so stray arrow-key sequences cannot corrupt a stop command.
