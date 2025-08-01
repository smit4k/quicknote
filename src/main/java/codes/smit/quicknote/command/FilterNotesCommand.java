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
                    
    // Filter flags
    private static class FilterFlags {
        boolean hasCoordinates = false;
        boolean noCoordinates = false;
        boolean hasTimestamp = false;
        boolean noTimestamp = false;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("filternotes")
                .then(CommandManager.literal("-c")
                    .then(CommandManager.literal("-t")
                        .executes(ctx -> {
                            FilterFlags flags = new FilterFlags();
                            flags.hasCoordinates = true;
                            flags.hasTimestamp = true;
                            return executeFilter(ctx.getSource(), flags);
                        }))
                    .then(CommandManager.literal("-nt")
                        .executes(ctx -> {
                            FilterFlags flags = new FilterFlags();
                            flags.hasCoordinates = true;
                            flags.noTimestamp = true;
                            return executeFilter(ctx.getSource(), flags);
                        }))
                    .executes(ctx -> {
                        FilterFlags flags = new FilterFlags();
                        flags.hasCoordinates = true;
                        return executeFilter(ctx.getSource(), flags);
                    }))
                .then(CommandManager.literal("-nc")
                    .then(CommandManager.literal("-t")
                        .executes(ctx -> {
                            FilterFlags flags = new FilterFlags();
                            flags.noCoordinates = true;
                            flags.hasTimestamp = true;
                            return executeFilter(ctx.getSource(), flags);
                        }))
                    .then(CommandManager.literal("-nt")
                        .executes(ctx -> {
                            FilterFlags flags = new FilterFlags();
                            flags.noCoordinates = true;
                            flags.noTimestamp = true;
                            return executeFilter(ctx.getSource(), flags);
                        }))
                    .executes(ctx -> {
                        FilterFlags flags = new FilterFlags();
                        flags.noCoordinates = true;
                        return executeFilter(ctx.getSource(), flags);
                    }))
                .then(CommandManager.literal("-t")
                    .then(CommandManager.literal("-c")
                        .executes(ctx -> {
                            FilterFlags flags = new FilterFlags();
                            flags.hasTimestamp = true;
                            flags.hasCoordinates = true;
                            return executeFilter(ctx.getSource(), flags);
                        }))
                    .then(CommandManager.literal("-nc")
                        .executes(ctx -> {
                            FilterFlags flags = new FilterFlags();
                            flags.hasTimestamp = true;
                            flags.noCoordinates = true;
                            return executeFilter(ctx.getSource(), flags);
                        }))
                    .executes(ctx -> {
                        FilterFlags flags = new FilterFlags();
                        flags.hasTimestamp = true;
                        return executeFilter(ctx.getSource(), flags);
                    }))
                .then(CommandManager.literal("-nt")
                    .then(CommandManager.literal("-c")
                        .executes(ctx -> {
                            FilterFlags flags = new FilterFlags();
                            flags.noTimestamp = true;
                            flags.hasCoordinates = true;
                            return executeFilter(ctx.getSource(), flags);
                        }))
                    .then(CommandManager.literal("-nc")
                        .executes(ctx -> {
                            FilterFlags flags = new FilterFlags();
                            flags.noTimestamp = true;
                            flags.noCoordinates = true;
                            return executeFilter(ctx.getSource(), flags);
                        }))
                    .executes(ctx -> {
                        FilterFlags flags = new FilterFlags();
                        flags.noTimestamp = true;
                        return executeFilter(ctx.getSource(), flags);
                    }))
                .executes(ctx -> {
                    ServerCommandSource source = ctx.getSource();
                    source.sendFeedback(() -> Text.literal("Usage: /filternotes [-c | -nc] [-t | -nt]"), false);
                    source.sendFeedback(() -> Text.literal("  -c: Show notes with coordinates"), false);
                    source.sendFeedback(() -> Text.literal("  -nc: Show notes without coordinates"), false);
                    source.sendFeedback(() -> Text.literal("  -t: Show notes with timestamp"), false);
                    source.sendFeedback(() -> Text.literal("  -nt: Show notes without timestamp"), false);
                    source.sendFeedback(() -> Text.literal("  You can combine filters, e.g.: /filternotes -nc -t"), false);
                    return 1;
                })
        );
    }

    private static int executeFilter(ServerCommandSource source, FilterFlags flags) {
        UUID uuid = source.getPlayer().getUuid();
        List<Note> allNotes = NoteManager.getNotes(uuid);

        List<Note> filteredNotes = allNotes.stream()
                .filter(note -> {
                    boolean passes = true;
                    
                    // Apply coordinate filters
                    if (flags.hasCoordinates) {
                        passes = note.x != 0 || note.y != 0 || note.z != 0;
                    }
                    if (flags.noCoordinates) {
                        passes = passes && (note.x == 0 && note.y == 0 && note.z == 0);
                    }
                    
                    // Apply timestamp filters
                    if (flags.hasTimestamp) {
                        passes = passes && note.timestamp != 0;
                    }
                    if (flags.noTimestamp) {
                        passes = passes && note.timestamp == 0;
                    }
                    
                    return passes;
                })
                .toList();

        if (filteredNotes.isEmpty()) {
            StringBuilder message = new StringBuilder("You have no notes");
            if (flags.hasCoordinates) {
                message.append(" with coordinates");
            }
            if (flags.noCoordinates) {
                message.append(" without coordinates");
            }
            if (flags.hasTimestamp) {
                message.append(" with timestamp");
            }
            if (flags.noTimestamp) {
                message.append(" without timestamp");
            }
            message.append(".");
            source.sendFeedback(() -> Text.literal(message.toString()), false);
        } else {
            StringBuilder headerMessage = new StringBuilder("Notes");
            if (flags.hasCoordinates) {
                headerMessage.append(" with coordinates");
            }
            if (flags.noCoordinates) {
                headerMessage.append(" without coordinates");
            }
            if (flags.hasTimestamp) {
                headerMessage.append(" with timestamp");
            }
            if (flags.noTimestamp) {
                headerMessage.append(" without timestamp");
            }
            headerMessage.append(":");
            source.sendFeedback(() -> Text.literal(headerMessage.toString()), false);

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
