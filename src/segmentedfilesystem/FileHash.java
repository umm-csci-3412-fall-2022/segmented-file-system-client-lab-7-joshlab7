package segmentedfilesystem;
import java.util.PriorityQueue;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.stream.Collector;

public class FileHash {
    String name;
    int currentPacketCount = 0;
    int sizeOfFile = 0;
    // Assigning the packets to a priority queue makes an easy way to add new packets to the system and to write the 
    // packets out as output at the end of the process
    PriorityQueue<DataPacket> packets = new PriorityQueue<DataPacket>(1028, (x, y) -> x.getPositionNumber() - y.getPositionNumber());

    public FileHash() {}

    // Adds a packet to the collection of packets and writes it out
    public boolean addPacket(Packet packet) throws IOException {
        if (packet instanceof HeaderPacket) {
            HeaderPacket headerPacket = (HeaderPacket) packet;
            addHeaderPacket(headerPacket);
        } else if (packet instanceof DataPacket) {
            DataPacket dataPacket = (DataPacket) packet;
            addDataPacket(dataPacket);
        }
        if (isWritable()) {
            writeFile();
            return true;
        }
            return false;
        }

    // addPacket Helper Functions: 
    // Adds a header packet/assign the name
    private void addHeaderPacket(HeaderPacket packet) {
        name = packet.getFileName();
    }

    // Adds a data packet to the collection
    private void addDataPacket(DataPacket packet) {
        packets.add(packet);
        currentPacketCount++;
        if (packet.getIfLast()) {
            sizeOfFile = packet.getPositionNumber() + 1;
        }
    }

    // Makes sure that the collection of packets is writable
    private boolean isWritable() {
        return (sizeOfFile > 0) && (currentPacketCount == sizeOfFile) && (name != null);
    }

    // 
    private void writeFile() throws IOException {
        FileOutputStream output = new FileOutputStream(name);
        // Takes data from data packets, and rediirects them into the body of the file
        Collector<DataPacket, ArrayList<Byte>, byte[]> fileDataCollector = Collector.of(
            ArrayList<Byte>::new,
            ((x, y) -> {x.addAll(y.getBodyAsList());}),
            ((x, y) -> {x.addAll(y); return x;}),
            this::convertListOfBytes
        );

        byte[] fileData = packets.stream().sorted((x, y) -> x.getPositionNumber() - y.getPositionNumber()).collect(fileDataCollector);

        // Outputs the file data and cleans the output
        output.write(fileData);
        output.flush();
    }

    // writeFile() Helper Function:
    // Converts an array list of Bytes into a fixed-size list of bytes
    private byte[] convertListOfBytes(ArrayList<Byte> l) {
        byte[] output = new byte[l.size()];
        for (int i = 0; i < l.size(); i++) {
            output[i] = l.get(i);
        }
        return output;
    }

}
