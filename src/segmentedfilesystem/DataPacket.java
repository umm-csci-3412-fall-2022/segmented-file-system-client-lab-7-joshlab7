package segmentedfilesystem;

import java.util.ArrayList;

// Data Packet: contains the data for the chunk, as well as the packet number
public class DataPacket extends Packet {
    public byte[] body;
    public int packetNumber;
    // The boolean 'last' signifies whether the file is the last one to be sent
    public boolean last;

    public DataPacket() {}
  
    public int getPositionNumber() {
        return packetNumber;
    }

    public byte[] getBody() {
        return body;
    }

    public boolean getIfLast() {
        return last;
    }

    public void appointBody(byte[] body) {
        this.body = body;
    }

    public void appointPositionNumber(int number) {
        packetNumber = number;
    }

    public void appointIfLast(boolean last) {
        this.last = last;
    }

    // Gets the body of the chunk as a list, so that it can be outputted
    public ArrayList<Byte> getBodyAsList() {
        ArrayList<Byte> output = new ArrayList<Byte>();
        for (byte b: body) {
          output.add(b);
        }
        return output;
      }

}