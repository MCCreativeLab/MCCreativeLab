# Fork maintenance references

- Read [entity behaviour dispatch](docs/guides/ENTITY_BEHAVIOUR.md) before changing entity interception or superclass calls. Keep vanilla superclass delegation inside the ordinary native implementation.
- [Login advancement events](docs/update-26.3/LOGIN_EVENT_FIX.md) records the required event handler lists and async login dispatch regression.
- Preserve fixes in persistent feature/file patches; generated Minecraft source edits alone do not survive patch application. Verify the applied native tree and relevant tests.
- Use Java 25. [Update and build notes](docs/update-26.3/UPDATE.md) document the toolchain. Native behavioural integration is covered by the sibling mcc-util opt-in server fixture.
- Preserve existing user build/credential changes. Do not equate a successful build or an empty test world with a real player-session verification.
