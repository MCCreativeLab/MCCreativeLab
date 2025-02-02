# 🌏 MCCreativeLab - A Paper fork, opening new ways of customization

> [!WARNING]
> This project is still work in progress. You have to compile it by yourself.

## ⛏️ Building
```shell
git clone https://github.com/derverdox/MCCreativeLab.git
cd MCCreativeLab
./gradlew applyPatches # apply all patches of this fork to the code 
./gradlew createMojmapPaperclipJar # create a runnable Paperclip jar file
# Your output will be located in './build/libs'.
```

## 🚀 Modules

### 📦 mccreativelab-api
The API including [Bukkit](https://github.com/Bukkit/Bukkit), [Spigot](https://github.com/SpigotMC), [PaperMC API](https://github.com/PaperMC/paper) and the MCCreativeLab API.

### 🍯 plugin-extension
The `plugin-extension` of MCCreative Lab containing a huge customization toolset for HUDs, GUIs and much more.

### ⚙️ mccreativelab-server
The server internals containing the Minecraft server (aka NMS).

### 👷‍♂️ mcc-util
A module containing useful utilities for MCCreativeLab containing registries.

### 🎛️ mcc-pack-generator
The pack-generator primarily used in the `plugin-extension`.

### 🧪 mcc-class-generator
The class generator used for platform independence (WIP).

## 📋 Tasks
This project uses paperweight

| Task Name                  | Description                                      |
|----------------------------|--------------------------------------------------|
| applyApiPatches            |                                                  |
| applyPatches               |                                                  |
| applyServerPatches         |                                                  |
| cleanCache                 | Delete the project setup cache and task outputs. |
| createMojmapBundlerJar     | Build a runnable bundler jar                     |
| createMojmapPaperclipJar   | Build a runnable paperclip jar                   |
| createReobfBundlerJar      | Build a runnable bundler jar                     |
| createReobfPaperclipJar    | Build a runnable paperclip jar                   |
| generateDevelopmentBundle  |                                                  |
| rebuildApiPatches          |                                                  |
| rebuildPatches             |                                                  |
| rebuildServerPatches       |                                                  |
| reobfJar                   | Re-obfuscate the built jar to obf mappings       |
| runDev                     | Spin up a non-relocated Mojang-mapped test server|
| runReobf                   | Spin up a test server from the reobfJar output jar|
| runShadow                  | Spin up a test server from the shadowJar archiveFile|
