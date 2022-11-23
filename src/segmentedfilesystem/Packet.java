package segmentedfilesystem;

public class Packet{

	public byte fileID;

	public int getFileID(){
		return fileID;
	}

    public void setFileID(byte ID) {
        fileID = ID;
    }

}