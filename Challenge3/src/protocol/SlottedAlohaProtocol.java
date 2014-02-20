package protocol;

public class SlottedAlohaProtocol implements IMACProtocol {

	public SlottedAlohaProtocol() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public TransmissionInfo TimeslotAvailable(MediumState previousMediumState,
			int controlInformation, int localQueueLength) {
		return new TransmissionInfo(TransmissionType.Data, 0);
	}

}
