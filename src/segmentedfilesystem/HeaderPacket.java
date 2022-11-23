package segmentedfilesystem;

// Header Packet: contains the name of the file being transferred
public class HeaderPacket extends Packet{
	
	String name;

	public HeaderPacket() {};

    // Gets the name of the header packet
    public String getFileName() {
        return name;
    }

    // Assigns the name of the header packet
	public void appointName(String nameForFile) {
        name = nameForFile;
    }
}