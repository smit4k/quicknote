# Quicknote
A lightweight and intuitive Fabric mod designed to help you create, manage, and recall notes directly within your Minecraft world using simple chat commands.

## 🌟 Features
- Note creation / deletion
- Note filtering
- Attach coordinates to notes
- Attach timestamps to notes

## 🚀 Installation

### Prerequisites
- [Minecraft Java Edition](https://www.minecraft.net/en-us/download/alternative)
- [Fabric Loader](https://fabricmc.net/use/installer/)
- [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

### Steps
1.  **Install Fabric Loader:** If you haven't already, download and run the Fabric installer for your Minecraft version (any `1.21.X` version).
2.  **Download Quicknote:** Go to the [Releases](https://github.com/smit4k/quicknote/releases) page and download the latest `.jar` file for Quicknote.
3.  **Place in Mods Folder:** Locate your Minecraft `mods` folder.
    *   **Windows:** Press `Win + R`, type `%appdata%\.minecraft\mods`, and press Enter.
    *   **macOS:** In Finder, click `Go > Go to Folder...`, type `~/Library/Application Support/minecraft/mods`, and press Enter.
    *   **Linux:** Typically `~/.minecraft/mods`.
    Place the downloaded Quicknote `.jar` file into this `mods` folder.
4.  **Launch Minecraft:** Start your Minecraft launcher and select the "fabric-loader" profile. Launch the game, and Quicknote will be active!

## 🛠️ Building from Source

If you wish to build Quicknote from its source code:

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/smit4k/quicknote.git
    cd quicknote
    ```
2.  **Run Gradle Tasks:**
    ```bash
    ./gradlew build
    ```
    This will compile the mod and generate the `.jar` files in the `build/libs` directory.
    -   `./gradlew runClient`: Runs a Minecraft client with the mod loaded for testing.
    -   `./gradlew runServer`: Runs a Minecraft server with the mod loaded for testing.

## 🤝 Contributing

Contributions are welcome! If you have suggestions for new features, bug fixes, or improvements, please feel free to:
1.  Fork the repository.
2.  Create a new branch (`git checkout -b feature/YourFeatureName`).
3.  Make your changes.
4.  Commit your changes (`git commit -m 'Add new feature'`).
5.  Push to the branch (`git push origin feature/YourFeatureName`).
6.  Open a Pull Request.

## 📜 License

Quicknote is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.
