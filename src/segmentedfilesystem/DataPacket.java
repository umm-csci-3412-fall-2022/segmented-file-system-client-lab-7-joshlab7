package segmentedfilesystem;

public class DataPacket extends Packet {
    public byte[] body;
    public int positionNumber;

    public DataPacket() {}
  
    public int getPositionNumber() {
        return positionNumber;
    }

    public byte[] getBody() {
        return body;
    }

    public void appointBody(byte[] body) {
        this.body = body;
    }

    public void appointPositionNumber(int number) {
        positionNumber = number;
    }

}