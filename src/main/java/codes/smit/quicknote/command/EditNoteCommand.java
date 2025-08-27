package codes.smit.quicknote.command;

import codes.smit.quicknote.data.NoteManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.List;
import java.util.UUID;

public class EditNoteCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("editnote")
                .then(CommandManager.argument("id", IntegerArgumentType.integer(1))
                        .then(CommandManager.argument("message", StringArgumentType.greedyString())
                                .executes(EditNoteCommand::execute))));
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        UUID playerUuid = source.getPlayerOrThrow().getUuid();
        int id = IntegerArgumentType.getInteger(context, "id");
        String newMessage = StringArgumentType.getString(context, "message");

        boolean edited = NoteManager.editNote(playerUuid, id, newMessage);

        if (edited) {
            source.sendFeedback(() -> Text.literal("Note " + id + " updated."), false);
            return 1;
        } else {
            source.sendError(Text.literal("Note " + id + " not found."));
            return 0;
        }
    }
}
