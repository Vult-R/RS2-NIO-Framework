package main.astraeus.net.protocol.codec.login;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.astraeus.Bootstrap;
import main.astraeus.game.model.World;
import main.astraeus.net.channel.PlayerChannel;
import main.astraeus.net.channel.events.WriteChannelEvent;
import main.astraeus.net.packet.PacketWriter;
import main.astraeus.net.protocol.ProtocolConstants;
import main.astraeus.net.protocol.ProtocolStateDecoder;
import main.astraeus.net.protocol.codec.IsaacRandom;
import main.astraeus.net.protocol.codec.IsaacRandomPair;
import main.astraeus.net.protocol.codec.game.GamePacketDecoder;

public final class LoginPayloadDecoder implements ProtocolStateDecoder {
	
	/**
	 * The single logger for this class.
	 */
	public static final Logger logger = Logger.getLogger(LoginPayloadDecoder.class.getName());

	@Override
	public void decode(PlayerChannel context) throws IOException {

		if (!(context.getBuffer().remaining() < 2)) {

			/*
			 * Denotes the connection status.
			 */
			final int opcode = context.getBuffer().get() & 0xFF;

			/*
			 * The size of the login block.
			 */
			final int loginBlockSize = context.getBuffer().get() & 0xFF;

			/*
			 * The size of the login block after basic encryption.
			 */
			final int encryptedLoginBlockSize = (loginBlockSize - ProtocolConstants.LOGIN_BLOCK_ENCRYPTION_KEY);

			if (opcode != ProtocolConstants.NEW_CONNECTION_OPCODE && opcode != ProtocolConstants.RECONNECTION_OPCODE) {
				logger.log(Level.WARNING, "Invalid connection opcode.");
				return;
			}

			if (encryptedLoginBlockSize < 1) {
				logger.log(Level.WARNING, "Invalid Login-Block size.");
				return;
			}

			if (context.getBuffer().remaining() < loginBlockSize) {
				logger.log(Level.WARNING, "Insufficent buffered memory.");
				return;
			}

			if ((context.getBuffer().get() & 0xFF) != ProtocolConstants.MAGIC_NUMBER_OPCODE) {
				logger.log(Level.WARNING, "Invalid magic number.");
				return;
			}

			if (context.getBuffer().getShort() != ProtocolConstants.PROTOCOL_REVISION) {
				logger.log(Level.WARNING, "Invalid client version.");
				return;
			}

			context.getBuffer().get();

			for (int accumulator = 0; accumulator < ProtocolConstants.RSA_KEY_SKIPPING_AMOUNT; accumulator ++) {
				context.getBuffer().getInt();
			}
			context.getBuffer().get();

			if ((context.getBuffer().get() & 0xFF) == 10) {
				
				/*
				 * The seed generated on the client's end.
				 */
				final long clientSeed = context.getBuffer().getLong();

				/*
				 * The seed generated on the server's end.
				 */
				final long serverSeed = context.getBuffer().getLong();

				/*
				 * The player's identification key.
				 */
				context.getBuffer().getInt();

				/*
				 * The cryptography seeds.
				 */
				final int[] seeds = { (int) (clientSeed >> 32), (int) clientSeed, (int) (serverSeed >> 32), (int) serverSeed };

				/*
				 * The cryptography algorithm for opcode encoding.
				 */
				final IsaacRandom encoder = new IsaacRandom(seeds);

				for (int i = 0; i < seeds.length; i++) {
					seeds[i] += 50;
				}

				/*
				 * The cryptography algorithm for opcode decoding.
				 */
				final IsaacRandom decoder = new IsaacRandom(seeds);

				context.getPlayer().setCryptographyPair(new IsaacRandomPair(encoder, decoder));

				/*
				 * The name of the player's account.
				 */
				final String username = readString(context.getBuffer()).trim();

				/*
				 * The password of the player's account.
				 */
				final String password = readString(context.getBuffer()).trim();

				/*
				 * The local address of the player's comwriteer.
				 */
				final String address = context.getChannel().getRemoteAddress().toString().replaceFirst("/", " ").trim();
				
				context.getPlayer().getDetails().setUsername(username);
				context.getPlayer().getDetails().setPassword(password);
				context.getPlayer().getDetails().setAddress(address);
				
				LoginResponse loginResponse = LoginResponse.SUCCESSFUL_LOGIN;
				
				if (!context.getPlayer().load()) {
					loginResponse = LoginResponse.INVALID_CREDENTIALS;
				}
				
				if (context.getPlayer().getDetails().getUsername().length() > 12) {
					loginResponse = LoginResponse.INVALID_CREDENTIALS;
				}
				
				if (World.isLoggedIn(context.getPlayer().getDetails().getUsername())) {
					loginResponse = LoginResponse.ACCOUNT_IS_ALREADY_LOGGED_IN;
				}
				
				if (!Bootstrap.SERVER_STARTED) {
					loginResponse = LoginResponse.SERVER_UPDATED;					
				}

				context.execute(new WriteChannelEvent(sendResponseCode(context, loginResponse)));
				
				if (loginResponse != LoginResponse.SUCCESSFUL_LOGIN) {
					context.getChannel().close();
					logger.log(Level.INFO, String.format("[LOGIN ATTEMPT FAILED] - User: %s  %s", context.getPlayer().getDetails().getUsername(), loginResponse.name()));
					return;
				}
				
				context.getPlayer().getEventListener().add(context.getPlayer());
				context.setProtocolDecoder(new GamePacketDecoder());			
				
			} else {
				logger.log(Level.WARNING, "Invalid RSA key.");
			}
		} else {
			context.getBuffer().compact();
		}
	}
	
	/**
	 * Writes a response code to the client.
	 * 
	 * @param responseCode
	 * 		The response code to send.
	 * 
	 * @return The encoder of this outgoing packet.
	 */
	public PacketWriter sendResponseCode(PlayerChannel context, LoginResponse responseCode) {
		final PacketWriter response = new PacketWriter(ByteBuffer.allocate(3));		
		response.write(responseCode.getValue());
		response.write(context.getPlayer().getDetails().getRights().getProtocolValue()); // player rights
		response.write(0);
		return response;
	}

	/**
	 * Reads a series of bytes in the form of characters and translates that
	 * sequence into a String.
	 * 
	 * @param buffer The internal buffer.
	 * 
	 * @return The result of the operation.
	 */
	public final String readString(ByteBuffer buffer) {
		final StringBuilder builder = new StringBuilder();

		for (char character = '\0'; buffer.hasRemaining() && character != '\n'; character = (char) (buffer.get() & 0xFF)) {
			/*
			 * Appends the String representation of the character argument to this sequence. 
			 */
			builder.append(character);
		}
		return builder.toString();
	}
}