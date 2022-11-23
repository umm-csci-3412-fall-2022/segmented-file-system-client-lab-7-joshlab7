package segmentedfilesystem;

import java.io.FileNotFoundException;
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
                        DatagramSocket socket = new DatagramSocket();
                        DatagramPacket packet0 = new DatagramPacket(new byte[1028], 1028, InetAddress.getByName(server), port);
                        socket.send(packet0);
                        DatagramPacket packet = new DatagramPacket(new byte[1028], 1028);
                        int numberOfFiles = 0;
                        while (numberOfFiles < 3) {
                                socket.receive(packet);
                                Packet currentPacket = createPacket(packet);
                                if (addPacket(currentPacket)) {
                                        numberOfFiles++;
                                }
                        }
                } catch (IOException error) {
                        System.out.println("An unexpected error occurred: " + error);
                }
	}

        public Packet createPacket(DatagramPacket packet) {
                byte[] data = packet.getData();

                if (isOdd(data[0])) {
                        DataPacket newPacket = new DataPacket();

                        newPacket.appointIfLast(((data[0] % 4) == 3));
                        newPacket.appointPositionNumber((256 * Byte.toUnsignedInt(data[2])) + Byte.toUnsignedInt(data[3]));
                        newPacket.setFileID(data[1]);
                        newPacket.appointBody(Arrays.copyOfRange(data, 4, packet.getLength() - 2));
                        return newPacket;
                } else {
                        HeaderPacket newPacket = new HeaderPacket();
                        newPacket.appointName(new String(data, 2, packet.getLength() - 2));
                        newPacket.setFileID(data[1]);
                        return newPacket;
                }
        }

        public boolean isOdd(byte status) {
                if (status % 2 == 1) {
                        return true;
                } else {
                        return false;
                }
        }

        public boolean addPacket(Packet packet) throws IOException {
                incomingFiles.putIfAbsent(packet.getFileID(), new FileHash());
                return incomingFiles.get(packet.getFileID()).addPacket(packet);
        }

}