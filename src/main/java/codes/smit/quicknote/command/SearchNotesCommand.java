package codes.smit.quicknote.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import codes.smit.quicknote.data.NoteManager;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class SearchNotesCommand {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("searchnotes")
                .then(CommandManager.argument("query", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            ServerCommandSource source = ctx.getSource();
                            UUID uuid = source.getPlayer().getUuid();
                            String query = StringArgumentType.getString(ctx, "query");

                            List<Note> foundNotes = NoteManager.searchNotes(uuid, query);

                            if (foundNotes.isEmpty()) {
                                source.sendFeedback(() -> Text.literal("No notes found containing '" + query + "'."), false);
                            } else {
                                source.sendFeedback(() -> Text.literal("Notes found containing '" + query + "':"), false);
                                for (Note note : foundNotes) {
                                    String timestampStr = "";
                                    if (note.timestamp != 0) {
                                        timestampStr = " (on " + FORMATTER.format(Instant.ofEpochMilli(note.timestamp)) + ")";
                                    }

                                    String coordsStr = (note.x != 0 || note.y != 0 || note.z != 0)
                                            ? String.format(" @ [%.1f, %.1f, %.1f]", note.x, note.y, note.z)
                                            : "";

                                    String tagsStr = "";
                                    if (note.tags != null && !note.tags.isEmpty()) {
                                        tagsStr = " [" + String.join(", ", note.tags.stream().map(tag -> "#" + tag).toList()) + "]";
                                    }

                                    String line = String.format(
                                            "#%d: \"%s\"%s%s%s",
                                            note.id,
                                            note.message,
                                            tagsStr,
                                            coordsStr,
                                            timestampStr
                                    );
                                    source.sendFeedback(() -> Text.literal(line), false);
                                }
                            }
                            return 1;
                        }))
        );
    }
}
