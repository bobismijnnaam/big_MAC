package protocol;

import java.util.Random;

public class SlottedAlohaProtocol implements IMACProtocol {

	public SlottedAlohaProtocol() {
		// TODO Auto-generated constructor stub
		myGen = new Random();
		
		id = myGen.nextInt(1000000);
	}
	
	private Random myGen;
	private int p = 5;
	private int id;

	@Override
	public TransmissionInfo TimeslotAvailable(MediumState previousMediumState,
			int controlInformation, int localQueueLength) {
		
		// No data to send, just be quiet
		if (localQueueLength == 0) {
			return new TransmissionInfo(TransmissionType.Silent, 0);
		}
		
		if (previousMediumState == MediumState.Collision) {
			// Random
			if (myGen.nextInt(p) == 0) {
				return new TransmissionInfo(TransmissionType.Data, id);
			} else {
				return new TransmissionInfo(TransmissionType.Silent, 0);
			}
		} else {
			return new TransmissionInfo(TransmissionType.Data, 0);
		}
	}

}
