package protocol;

import java.util.Random;

/**
 * A fairly trivial Medium Access Control scheme.
 * @author Jaco ter Braak, Twente University
 * @version 05-12-2013
 */
public class SlottedAlohaSimplified implements IMACProtocol {

	@Override
	public TransmissionInfo TimeslotAvailable(MediumState previousMediumState,
			int controlInformation, int localQueueLength) {
		// No data to send, just be quiet
		if (localQueueLength == 0) {
			System.out.println("I am silent.");
			return new TransmissionInfo(TransmissionType.Silent, 0);
		}

		// Randomly transmit
		if (new Random().nextInt(2) == 0) {
			System.out.println("I will send.");
			return new TransmissionInfo(TransmissionType.Data, 0);
		} else {
			System.out.println("I am silent.");
			return new TransmissionInfo(TransmissionType.Silent, 0);
		}

	}

}
