package codes.smit.quicknote;

import codes.smit.quicknote.command.*;
import codes.smit.quicknote.data.NoteManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickNote implements ModInitializer {
	public static final String MOD_ID = "quicknote";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Initializing QuickNote mod");
		
		// Initialize the NoteManager with the config directory
		var configDir = FabricLoader.getInstance().getConfigDir().toFile();
		NoteManager.init(configDir);
		LOGGER.info("Notes will be saved to: " + NoteManager.getNotesFilePath());
		
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			NoteCommand.register(dispatcher);
			ViewNoteCommand.register(dispatcher);
			ClearNotesCommand.register(dispatcher);
			ExportNotesCommand.register(dispatcher);
			FilterNotesCommand.register(dispatcher);
		});
	}
}