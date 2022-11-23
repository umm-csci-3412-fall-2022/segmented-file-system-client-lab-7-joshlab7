package segmentedfilesystem;

public class HeaderPacket extends Packet{
	
	String name;

	public HeaderPacket() {};

    public String getFileName() {
        return name;
    }

	public void appointName(String nameForFile) {
        name = nameForFile;
    }
}