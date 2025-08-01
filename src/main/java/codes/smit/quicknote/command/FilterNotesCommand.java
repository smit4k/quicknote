package codes.smit.quicknote.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import codes.smit.quicknote.data.NoteManager;

public class FilterNotesCommand {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("filternotes")
                .then(CommandManager.literal("-c")
                    .executes(ctx -> executeFilter(ctx.getSource(), true)))
                .then(CommandManager.literal("-nc")
                    .executes(ctx -> executeFilter(ctx.getSource(), false)))
                .executes(ctx -> {
                    ServerCommandSource source = ctx.getSource();
                    source.sendFeedback(() -> Text.literal("Usage: /filternotes [-c | -nc]"), false);
                    source.sendFeedback(() -> Text.literal("  -c: Show notes with coordinates"), false);
                    source.sendFeedback(() -> Text.literal("  -nc: Show notes without coordinates"), false);
                    return 1;
                })
        );
    }

    private static int executeFilter(ServerCommandSource source, boolean hasCoordinates) {
        UUID uuid = source.getPlayer().getUuid();
        List<Note> allNotes = NoteManager.getNotes(uuid);

        List<Note> filteredNotes = allNotes.stream()
                .filter(note -> hasCoordinates ?
                        (note.x != 0 || note.y != 0 || note.z != 0) :
                        (note.x == 0 && note.y == 0 && note.z == 0))
                .toList();

        if (filteredNotes.isEmpty()) {
            String message = hasCoordinates ?
                    "You have no notes with coordinates." :
                    "You have no notes without coordinates.";
            source.sendFeedback(() -> Text.literal(message), false);
        } else {
            String headerMessage = hasCoordinates ?
                    "Notes with coordinates:" :
                    "Notes without coordinates:";
            source.sendFeedback(() -> Text.literal(headerMessage), false);

            for (Note note : filteredNotes) {
                String timestampStr = "";
                if (note.timestamp != 0) {
                    timestampStr = " (@ " + FORMATTER.format(Instant.ofEpochMilli(note.timestamp)) + ")";
                }

                String coordsStr = (note.x != 0 || note.y != 0 || note.z != 0) ?
                        String.format(" at [%.1f, %.1f, %.1f]", note.x, note.y, note.z) :
                        "";

                String line = String.format(
                        "#%d: \"%s\"%s%s",
                        note.id,
                        note.message,
                        timestampStr,
                        coordsStr
                );
                source.sendFeedback(() -> Text.literal(line), false);
            }
        }

        return filteredNotes.size();
    }
}
