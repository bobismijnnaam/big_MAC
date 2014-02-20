package protocol;

import java.util.Random;

public class BigMACProtocol implements IMACProtocol {
	
	enum BigMACState {
		Idle,
		Active,
		Starting
	}

	BigMACState state;
	private Random myGen;
	private int packetCount;
	
	private final int DEFAULT_CHANCE = 4;
	
	private final int MAX_PACKETS = 10;
	
	public BigMACProtocol() {
		state = BigMACState.Idle;
		
		myGen = new Random();
	}

	@Override
	public TransmissionInfo TimeslotAvailable(MediumState previousMediumState,
			int controlInformation, int localQueueLength) {

		int c = controlInformation;
		int q = localQueueLength;
		MediumState p = previousMediumState;

		switch (state) {
			case Idle:
				if (q == 0) {
					System.out.print("*");
					return new TransmissionInfo(TransmissionType.Silent, 0);
				} else {
					if (c == 0 && myGen.nextInt(DEFAULT_CHANCE) == 0) {
						state = BigMACState.Starting;
						packetCount = 1;
						System.out.println("Attempting to get ownership");
						return new TransmissionInfo(TransmissionType.Data, q - 1);
					} else if (c == 0) {
						System.out.println("Can't send because someone else is sending");
						return new TransmissionInfo(TransmissionType.Silent, 0);
					} else {
						System.out.println("Can't send because coin flip failed");
						return new TransmissionInfo(TransmissionType.Silent, 0);
					}
				}
			case Starting:
				if (p == MediumState.Collision) {
					if (myGen.nextInt(DEFAULT_CHANCE) == 0) {
						System.out.println("Reattempting to get ownership");
						packetCount = 1;
						return new TransmissionInfo(TransmissionType.Data, q - 1);
					} else {
						state = BigMACState.Idle;
						System.out.println("Consecutive coin flip failed");
						return new TransmissionInfo(TransmissionType.Silent, 0);
					}
				} else {
					if (q == 0) {
						state = BigMACState.Idle;
						System.out.println("Sent only 1 packet");
						return new TransmissionInfo(TransmissionType.Silent, 0);
					} else {
						state = BigMACState.Active;
						packetCount++;
						System.out.println("Received ownership");
						return new TransmissionInfo(TransmissionType.Data, q - 1);
					}
				}
			case Active:
				if (packetCount >= MAX_PACKETS) {
					state = BigMACState.Idle;
					System.out.println("Packet limit reached");
					return new TransmissionInfo(TransmissionType.Silent, 0);
				}
				
				if (q - 1 == 0) {
					state = BigMACState.Idle;
					System.out.println("Finished sending");
				} else {
					System.out.println("Send packet");
				}
				
				packetCount++;
				return new TransmissionInfo(TransmissionType.Data, q - 1);
		}
		
		return null;
	}

}
