package segmentedfilesystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;

public class FileRetriever {
        HashMap<Byte, FileHash> incomingFiles;
        int port;
        String server;

	public FileRetriever(String server, int port) {
          this.server = server;
          this.port = port;
          incomingFiles = new HashMap<Byte, FileHash>();
	}

	public void downloadFiles() {
        // Do all the heavy lifting here.
        // This should
        //   * Connect to the server
        //   * Download packets in some sort of loop
        //   * Handle the packets as they come in by, e.g.,
        //     handing them to some PacketManager class
        // Your loop will need to be able to ask someone
        // if you've received all the packets, and can thus
        // terminate. You might have a method like
        // PacketManager.allPacketsReceived() that you could
        // call for that, but there are a bunch of possible
        // ways.
                try {
                        // Create a connection
                        DatagramSocket socket = new DatagramSocket();

                        // Send a packet through to initialize the connection
                        DatagramPacket packet0 = new DatagramPacket(new byte[1028], 1028, InetAddress.getByName(server), port);
                        socket.send(packet0);

                        // Create an empty packet format for new packets
                        DatagramPacket packet = new DatagramPacket(new byte[1028], 1028);
                        int numberOfFiles = 0;

                        // While the maximum amount of files have yet to be reached:
                        //      - Receive and create a packet
                        //      - If a packet has been added, increase the amount of files
                        while (numberOfFiles < 3) {
                                socket.receive(packet);
                                Packet currentPacket = createPacket(packet);
                                if (addPacket(currentPacket)) {
                                        numberOfFiles++;
                                }
                        }
                        // Close the connection
                        socket.close();
                } catch (IOException error) {
                        System.out.println("An unexpected error occurred: " + error);
                }
	}

        // downloadFiles() Helper Functions:

        // Create a packet
        public Packet createPacket(DatagramPacket packet) {
                byte[] data = packet.getData();

                // If the first byte is odd, than it is a data packet, else a header packet
                if (isOdd(data[0])) {
                        DataPacket newPacket = new DataPacket();

                        // Set the characteristics of the new Data packet
                        newPacket.appointIfLast(((data[0] % 4) == 3));
                        newPacket.appointPositionNumber((256 * Byte.toUnsignedInt(data[2])) + Byte.toUnsignedInt(data[3]));
                        newPacket.setFileID(data[1]);
                        newPacket.appointBody(Arrays.copyOfRange(data, 4, packet.getLength() - 2));
                        return newPacket;
                } else {
                        // Set the characteristics of the new Header Packet
                        HeaderPacket newPacket = new HeaderPacket();
                        newPacket.appointName(new String(data, 2, packet.getLength() - 2));
                        newPacket.setFileID(data[1]);
                        return newPacket;
                }
        }

        // Tests where a byte is even or odd
        public boolean isOdd(byte status) {
                if (status % 2 == 1) {
                        return true;
                } else {
                        return false;
                }
        }

        // Tests whether the new file has actually been added to the collection of packets
        public boolean addPacket(Packet packet) throws IOException {
                incomingFiles.putIfAbsent(packet.getFileID(), new FileHash());
                return incomingFiles.get(packet.getFileID()).addPacket(packet);
        }

}