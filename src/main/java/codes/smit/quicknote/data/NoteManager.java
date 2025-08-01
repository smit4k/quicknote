package codes.smit.quicknote.data;

import codes.smit.quicknote.command.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Manages player notes with persistence to disk.
 * Notes are stored in JSON format in the config/quicknote directory.
 * Notes are loaded on mod initialization and saved whenever they are modified.
 */

public class NoteManager {
    private static final Gson GSON = new Gson();
    private static Map<UUID, List<Note>> notes = new HashMap<>();
    private static File file;
    private static Path notesFilePath;

    public static void init(File configDir) {
        // Create the quicknote directory if it doesn't exist
        File quicknoteDir = new File(configDir, "quicknote");
        if (!quicknoteDir.exists()) {
            quicknoteDir.mkdirs();
        }
        
        // Set up the file and path
        file = new File(quicknoteDir, "notes.json");
        notesFilePath = file.toPath();
        
        // Load existing notes
        load();
    }

    private static void load() {
        if (!file.exists()) return;
        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<UUID, List<Note>>>(){}.getType();
            notes = GSON.fromJson(reader, type);
            if (notes == null) notes = new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void save() {
        try {
            // Ensure parent directory exists
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            // Write the notes to the file
            try (FileWriter writer = new FileWriter(file)) {
                GSON.toJson(notes, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Note addNote(UUID uuid, String message, double x, double y, double z, boolean includeTimestamp) {
        List<Note> userNotes = notes.computeIfAbsent(uuid, k -> new ArrayList<>());
        int id = userNotes.size() + 1;
        long timestamp = includeTimestamp ? System.currentTimeMillis() : 0;

        Note note = new Note(id, message, x, y, z, timestamp);
        userNotes.add(note);
        save();
        return note;
    }

    public static void clearNotes(UUID uuid) {
        notes.remove(uuid);
        save();
    }

    public static Path getNotesFilePath() {
        return notesFilePath;
    }



    public static List<Note> getNotes(UUID uuid) {
        return notes.getOrDefault(uuid, Collections.emptyList());
    }
}
