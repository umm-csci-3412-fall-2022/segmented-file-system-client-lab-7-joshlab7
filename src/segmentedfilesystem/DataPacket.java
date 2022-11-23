package segmentedfilesystem;

import java.util.ArrayList;

public class DataPacket extends Packet {
    public byte[] body;
    public int positionNumber;
    public boolean last;

    public DataPacket() {}
  
    public int getPositionNumber() {
        return positionNumber;
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
        positionNumber = number;
    }

    public void appointIfLast(boolean last) {
        this.last = last;
    }

    public ArrayList<Byte> getBodyAsList() {
        ArrayList<Byte> output = new ArrayList<Byte>();
        for (byte b: body) {
          output.add(b);
        }
        return output;
      }

}