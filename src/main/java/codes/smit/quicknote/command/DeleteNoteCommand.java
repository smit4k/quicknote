package codes.smit.quicknote.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import codes.smit.quicknote.data.NoteManager;

import java.util.UUID;

public class DeleteNoteCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("deletenote")
                .then(CommandManager.argument("id", IntegerArgumentType.integer(1))
                        .executes(ctx -> {
                            ServerCommandSource source = ctx.getSource();
                            UUID uuid = source.getPlayer().getUuid();
                            int noteId = IntegerArgumentType.getInteger(ctx, "id");

                            boolean deleted = NoteManager.deleteNote(uuid, noteId);

                            if (deleted) {
                                source.sendFeedback(() -> Text.literal("Note #" + noteId + " has been deleted"), false);
                            } else {
                                source.sendFeedback(() -> Text.literal("Note #" + noteId + " not found"), false);
                            }
                            return 1;
                        })
                )
        );
    }
}