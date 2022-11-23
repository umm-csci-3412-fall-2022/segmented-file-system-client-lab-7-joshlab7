package segmentedfilesystem;

// Create the base packet structure
public class Packet{

	// Each Packet has an ID
	public byte fileID;

	// Gets the ID of the packet
	public byte getFileID(){
		return fileID;
	}

	// Sets the ID of a packet
    public void setFileID(byte ID) {
        fileID = ID;
    }

}