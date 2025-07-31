package codes.smit.quicknote.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import codes.smit.quicknote.data.NoteManager;

import java.util.UUID;

public class ClearNotesCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("clearnotes")
                .executes(ctx -> {
                    ServerCommandSource source = ctx.getSource();
                    UUID uuid = source.getPlayer().getUuid();

                    NoteManager.clearNotes(uuid);

                    source.sendFeedback(() -> Text.literal("All notes have been cleared"), false);
                    return 1;
                })
        );
    }
}
