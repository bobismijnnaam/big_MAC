package protocol;

import java.util.Random;

public class TokenPassingProtocol implements IMACProtocol {

	// id to receive token
	private int id = -1;
	// nodes
	private int nodes = 4;
	private int control = 1;
	private int previousControl = -1;
	private int send = -1;
	private boolean hasSend = false;
	private boolean initialize = true;
	public boolean hasToken = false;
	
	int encode(boolean hasData, int clientID) {
		int hasDataInt = hasData ? 1 : 0;
		
		return (clientID << 1) ^ hasDataInt;
	}
	
	int[] decode(int msg) {
		int hasDataInt = msg & 1;
		int id = msg >> 1;
		
		return new int[]{hasDataInt, id};
	}
	
	@Override
	public TransmissionInfo TimeslotAvailable(MediumState previousMediumState,
			int controlInformation, int localQueueLength) {
		
		Random rand = new Random();
		
		if (controlInformation == nodes) {
			initialize = false;
		}
		
		// if not yet initialized
		if (id == -1) {
			if (controlInformation != 0) {
				System.out.println("Someone tries to get" + controlInformation);
				control++;
				if (hasSend) {
					id = controlInformation;
					send = id + 1;
					System.out.println("I've got id: " + id);
					if (id == nodes) {
						send = 1;
						System.out.println("Done initlizing");
						return new TransmissionInfo(TransmissionType.NoData, 1);
					}
				}
			}
			
			if (id == -1) {
				if (rand.nextInt(nodes) == 0) {
					System.out.println("Fighting for id");
					hasSend = true;
					return new TransmissionInfo(TransmissionType.NoData, control);
				} 
			}
			hasSend = false;
		}
		
		// check if has token then try to send
		if (id == controlInformation && !initialize) {
			//System.out.println("wow i received the token");
			hasToken = true;
		}
		
		if (hasToken) {
			if (localQueueLength == 0) {
				// Queue is empty so give token away
				//System.out.println("I've send here's the token! number;" + send);
				hasToken = false;
				return new TransmissionInfo(TransmissionType.NoData, send);
			}
			//System.out.println("I've send here's the token! number;" + send);
			hasToken = false;
			return new TransmissionInfo(TransmissionType.Data, send);
		}
		
		return new TransmissionInfo(TransmissionType.Silent, -1);
	}

}
