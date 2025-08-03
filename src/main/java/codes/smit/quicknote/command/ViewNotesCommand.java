package codes.smit.quicknote.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import codes.smit.quicknote.data.NoteManager;

import java.util.List;
import java.util.UUID;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class ViewNotesCommand {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("viewnotes")
                .executes(ctx -> {
                    ServerCommandSource source = ctx.getSource();
                    UUID uuid = source.getPlayer().getUuid();

                    List<Note> notes = NoteManager.getNotes(uuid);

                    if (notes.isEmpty()) {
                        source.sendFeedback(() -> Text.literal("You have no saved notes."), false);
                    } else {
                        source.sendFeedback(() -> Text.literal("Your Notes:"), false);

                        for (Note note : notes) {
                            String timestampStr = "";
                            if (note.timestamp != 0) {
                                timestampStr = " (@ " + FORMATTER.format(Instant.ofEpochMilli(note.timestamp)) + ")";
                            }

                            String coordsStr = (note.x != 0 || note.y != 0 || note.z != 0)
                                    ? String.format(" at [%.1f, %.1f, %.1f]", note.x, note.y, note.z)
                                    : "";

                            // Format tags if present
                            String tagsStr = "";
                            if (note.tags != null && !note.tags.isEmpty()) {
                                tagsStr = " [" + String.join(", ", note.tags.stream().map(tag -> "#" + tag).toList()) + "]";
                            }
                            
                            String line = String.format(
                                    "#%d: \"%s\"%s%s%s",
                                    note.id,
                                    note.message,
                                    timestampStr,
                                    coordsStr,
                                    tagsStr
                            );
                            source.sendFeedback(() -> Text.literal(line), false);
                        }
                    }
                    return 1;
                })
        );
    }
}