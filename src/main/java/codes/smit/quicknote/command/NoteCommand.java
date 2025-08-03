package codes.smit.quicknote.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import codes.smit.quicknote.data.NoteManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoteCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("note")
                .then(CommandManager.argument("message", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            ServerCommandSource source = ctx.getSource();
                            UUID uuid = source.getPlayer().getUuid();
                            String rawMessage = StringArgumentType.getString(ctx, "message");

                            AtomicBoolean includeCoords = new AtomicBoolean(false);
                            AtomicBoolean includeTimeStamp = new AtomicBoolean(false);

                            // Split message into tokens
                            List<String> tokens = new ArrayList<>(Arrays.asList(rawMessage.split("\\s+")));
                            
                            // Extract hashtags
                            List<String> tags = new ArrayList<>();
                            Pattern hashtagPattern = Pattern.compile("#(\\w+)");
                            
                            // Process each token to find hashtags
                            for (int i = 0; i < tokens.size(); i++) {
                                String token = tokens.get(i);
                                Matcher matcher = hashtagPattern.matcher(token);
                                
                                if (matcher.matches()) {
                                    // If the token is just a hashtag, add it to tags and remove from tokens
                                    tags.add(matcher.group(1).toLowerCase());
                                    tokens.remove(i);
                                    i--; // Adjust index after removal
                                } else if (token.contains("#")) {
                                    // If the token contains a hashtag within it
                                    matcher = hashtagPattern.matcher(token);
                                    while (matcher.find()) {
                                        tags.add(matcher.group(1).toLowerCase());
                                    }
                                }
                            }

                            // Remove flags and set booleans
                            tokens.removeIf(token -> {
                                if (token.equalsIgnoreCase("-c")) {
                                    includeCoords.set(true);
                                    return true;
                                } else if (token.equalsIgnoreCase("-t")) {
                                    includeTimeStamp.set(true);
                                    return true;
                                }
                                return false;
                            });

                            // Join tokens back into cleaned message
                            String cleanedMessage = String.join(" ", tokens).trim();

                            // Get player position if needed
                            double x = 0, y = 0, z = 0;
                            if (includeCoords.get()) {
                                Vec3d pos = source.getPosition();
                                x = pos.x;
                                y = pos.y;
                                z = pos.z;
                            }

                            // Add note with optional timestamp and tags
                            Note note = NoteManager.addNote(uuid, cleanedMessage, x, y, z, includeTimeStamp.get(), tags);

                            // Build feedback message
                            String reply = "📌 Note saved: \"" + cleanedMessage + "\"";
                            if (includeCoords.get()) {
                                reply += String.format(" at [%.1f, %.1f, %.1f]", x, y, z);
                            }
                            if (includeTimeStamp.get()) {
                                reply += " (timestamp included)";
                            }
                            if (!tags.isEmpty()) {
                                reply += " with tags: " + String.join(", ", tags.stream().map(tag -> "#" + tag).toList());
                            }

                            String finalReply = reply;
                            source.sendFeedback(() -> Text.literal(finalReply), false);
                            return 1;
                        })
                )
        );
    }
}
