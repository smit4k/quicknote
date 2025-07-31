package codes.smit.quicknote.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.nio.file.Path;

import codes.smit.quicknote.data.NoteManager;

public class ExportNotesCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("exportnotes")
                .executes(ctx -> {
                    ServerCommandSource source = ctx.getSource();
                    Path notesFile = NoteManager.getNotesFilePath();

                    source.sendFeedback(() -> Text.literal("Your notes are stored at: " + notesFile.toString()), false);
                    return 1;
                })
        );
    }
}
