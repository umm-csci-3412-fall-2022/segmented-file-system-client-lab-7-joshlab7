package segmentedfilesystem;
import java.util.PriorityQueue;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.Arrays;

public class FileHash {
    String name;
    int currentPacketCount = 0;
    int sizeOfFile = 0;
    PriorityQueue<DataPacket> packets = new PriorityQueue<DataPacket>(1028, (x, y) -> x.getPositionNumber() - y.getPositionNumber());

    public FileHash() {}

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

    private void addHeaderPacket(HeaderPacket packet) {
        name = packet.getFileName();
    }

    private void addDataPacket(DataPacket packet) {
        packets.add(packet);
        currentPacketCount++;
        if (packet.getIfLast()) {
            sizeOfFile = packet.getPositionNumber() + 1;
        }
    }

    private boolean isWritable() {
        //Check to confirm that we've received the last packet, all of the packets up to the last packet, and the
        //file name.
        return (sizeOfFile > 0) && (currentPacketCount == sizeOfFile) && (name != null);
    }

    private byte[] convertListOfBytes(ArrayList<Byte> l) {
        byte[] output = new byte[l.size()];
        for (int i = 0; i < l.size(); i++) {
            output[i] = l.get(i);
        }
        return output;
    }


    private void writeFile() throws IOException {
        FileOutputStream output = new FileOutputStream(name);
        //Extracts all the data from the data packets, and assembles them into the file body
        Collector<DataPacket, ArrayList<Byte>, byte[]> fileDataCollector = Collector.of(
            ArrayList<Byte>::new,
            ((x, y) -> {x.addAll(y.getBodyAsList());}),
            ((x, y) -> {x.addAll(y); return x;}),
            this::convertListOfBytes
        );

        byte[] fileData = packets.stream()
        .sorted((x, y) -> x.getPositionNumber() - y.getPositionNumber())
        .collect(fileDataCollector);

        output.write(fileData);
        output.flush();
    }
}
