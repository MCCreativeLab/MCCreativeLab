# MCCreativeLab Update Guide #

Sometimes minecraft releases a new version. For every major version we also want to update our code base since most of our code relies on native minecraft server code.
In order to have a smooth updating process we want to follow this strict protocol. Steps that share the same base number can be done simultaneously.

### Step 1.1 - Update MCCreativeLab Server Software ###
- In the gradle.properties change the version and mcversion tags
- Change the paperRef to the newest commit hash of papermc. They can be found in the download section of the papermc.io website. Every new build has a blue commit hash. Click on the shortened hash and copy it from the URL in your browser. For example: "https://github.com/PaperMC/Paper/commit/c467df95a29e6c3d42b7e6f49513e924f9ba2ece". Extract the hash "c467df95a29e6c3d42b7e6f49513e924f9ba2ece" and put it into the gradle.properties as "paperRef"
- Now use the paperweight gradle tasks to apply all patches (applyMinecraftPatches, applyServerPatches). If this task worked then you are done updating the fork (this won't happen :D). Normally there will be merge conflict. Congrats you are now in an am session. For every patch that fails, resolve the merge conflict and type and use 'git am continue' to proceed to the next patch.
- Repeat this sequence until all merge conflicts of all patches are resolved.
- Now you have to commit your changes and rebuild all patches.
- In the end create a commit with all updated patches and the updated gradle.properties
- Now publish all packages (api, dev-bundle) to the mcc repo.

### Step 1.2 - Update mcc-wrapper ###
Updating the mcc-wrapper will take most of the time. 
First we need to regenerate all collector classes by using the class-generator.
Then we need to check for some new additions that need a manual implementation:
- Item Components
- Enchantment Components
- This list will be updated when more api is included!

Also check if there are compilation errors that result from mojang updating their internals :)
Please also search for the @MCCReflective annotation as it is always used when we need to use reflection to make certain things work. Check if the fields are still valid!
In the end please publish <3 :)

### Step 2 - Update mcc-util ###
(This step depends on an updated version of: mccreativelab)
The mcc-util is a rather small project. We just need to update the versions in the gradle.properties. We now download the mcc-dev-bundle. Now you just have to build the code and resolve all conflicts. Then publish to verdox repo

### Step 3 - Update mcc-pack-generator ###
(This step depends on an updated version of: mcc-wrapper, mcc-util)
When updating mcc-pack-generator keep the changes in mind that were done in this new minecraft version. Some features of the pack generator might need an update.
If you are done please publish to repo aswell :)

### Step 4 - Update mcc-pack-generator ###
(This step depends on an updated version of: mcc-wrapper, mcc-util, mcc-pack-generator)
The plugin extension is our last project that needs to be updated. Please keep in mind that all functionality still works. Thanks!
In the end publish.
