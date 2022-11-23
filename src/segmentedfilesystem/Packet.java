package segmentedfilesystem;

public class Packet{

	public byte fileID;

	public byte getFileID(){
		return fileID;
	}

    public void setFileID(byte ID) {
        fileID = ID;
    }

}