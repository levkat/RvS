package programmieraufgaben;

import java.net.InetAddress;

/**
 * Hier sollen die Nutzereingaben sowie die Resultate gespeichert werden.
 * Die Struktur der Klasse und die Variablen können frei gewählt werden.
 */
public class DataPackage {
    //maximale Datenteil-Länge
    private int dataPackageLength;
    private int version;
    private String senderAddress;
    private String reciverAddress;
    private String message;
    private int seqNum;
    /**
     * Erzeugt ein DataPackage Objekt und speichert beim erzeugen die maximale Datenteil-Länge
     * @param dataPackageLength returns the maximumg package length TODO check in Aufgabestellung
     */
    public DataPackage(int dataPackageLength) {
        this.dataPackageLength = dataPackageLength;
    }
    public void setVersion(int version){
        this.version = version;
    }
    public void setSenderAddress(String senderAddress){
        this.senderAddress = senderAddress;
    }
    public void setReciverAddress(String reciverAddress){
        this.reciverAddress = reciverAddress;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public void setSeqNum(int seqNum){
        this.seqNum = seqNum;
    }
    /**
     * Gibt die maximale Datenteil-Länge zurück
     * @return maximale Datenteil-Länge
     */
    public int getDataPackageLength() {
        return dataPackageLength;
    }

    public String getReciverAddress() {
        return reciverAddress;
    }

    public int getVersion() {
        return version;
    }

    public String getsenderAddress() {
        return senderAddress;
    }

    public String getMessage() {
        return message;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void printAll(){
        System.out.println("Die maximale Datenteil-Länge ist " + dataPackageLength
                + System.lineSeparator()
                + "Version:" + version
                + System.lineSeparator()
                + "Absender:" + senderAddress
                + System.lineSeparator()
                + "Empfänger:" + reciverAddress
                + System.lineSeparator()
                + "Nachricht:" + message);
    }

    /**
     * Setzt die maximale Datenteil-Länge
     * @param dataPackageLength Setzt die maximale Datenteil-Länge
     */
    public void setDataPackageLength(int dataPackageLength) {
        this.dataPackageLength = dataPackageLength;
    }
}
