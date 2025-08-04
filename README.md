# Quicknote
A lightweight and intuitive Fabric mod designed to help you create, manage, and recall notes directly within your Minecraft world using simple chat commands. Never forget an important location, a crafting recipe, or a task again!

## 🌟 Features
- **Effortless Note Creation:** Quickly add new notes using a simple chat command.
- **Tags:** Organize your notes with tags!
- **Coordinates:** Add coordinates to your notes!
- **Timestamps:** Add timestamps to your notes!
- **Easy Retrieval:** View all your saved notes with a single command.
- **Seamless Deletion:** Remove old or irrelevant notes with ease.
- **Persistent Storage:** Notes are saved locally and persist across game sessions.

## 🚀 Installation

### Prerequisites
- [Minecraft Java Edition](https://www.minecraft.net/en-us/download/alternative)
- [Fabric Loader](https://fabricmc.net/use/installer/) (version `>=0.16.14`)
- [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) (version `0.128.1+1.21.5` or compatible)

### Steps
1.  **Install Fabric Loader:** If you haven't already, download and run the Fabric installer for your Minecraft version (`1.21.5`).
2.  **Download Quicknote:** Go to the [Releases](https://github.com/smit4k/quicknote/releases) page and download the latest `.jar` file for Quicknote.
3.  **Place in Mods Folder:** Locate your Minecraft `mods` folder.
    *   **Windows:** Press `Win + R`, type `%appdata%\.minecraft\mods`, and press Enter.
    *   **macOS:** In Finder, click `Go > Go to Folder...`, type `~/Library/Application Support/minecraft/mods`, and press Enter.
    *   **Linux:** Typically `~/.minecraft/mods`.
    Place the downloaded Quicknote `.jar` file into this `mods` folder.
4.  **Launch Minecraft:** Start your Minecraft launcher and select the "fabric-loader" profile. Launch the game, and Quicknote will be active!

## 🎮 Usage

All commands are executed through the Minecraft chat. Press `T` to open the chat.

-   **Add a Note:** `/note <your note content>`
    *   Example: `/note Remember to mine diamonds here -c`
-   **View All Notes:** `/viewnotes`
-   **Filter Notes:** `/filternotes -c` (shows notes with coordinates, can also do -t, -nc, -nt)
    *   Example: `/note filter diamond` (shows notes containing "diamond")
-   **Delete a Note:** `/deletenote <note ID>`
    *   Find the note ID by viewing all notes (`/viewnotes`).
    *   Example: `/deletenote 5` (deletes the note with ID 5)
-   **Clear All Notes:** `/clearnotes`
-   **Export Notes:** `/exportnotes`

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
