package programmieraufgaben;


/**
 * Hier sollen die Nutzereingaben sowie die Resultate gespeichert werden.
 * Die Struktur der Klasse und die Variablen können frei gewählt werden.
 */

public class DataPackage {
    private int dataPackageLength;
    private int version;
    private String senderAddress;
    private String receiverAddress;
    private String message;
    private int seqNum;
    /**
     * Erzeugt ein DataPackage Objekt und speichert beim erzeugen die maximale Datenteil-Länge
     * @param dataPackageLength returns the maximum package length
    */
    public DataPackage(int dataPackageLength) {
        this.dataPackageLength = dataPackageLength;
    }

    /**
     * Erzeugt ein DataPackage Objekt mit den folgenden einegegebenen Parametern
     * @param dataPackageLength
     * @param version
     * @param senderAddress
     * @param receiverAddress
     * @param message die im Paket enthaltene Nachricht
     * @param seqNum Paketlaufnummer
     */
    public DataPackage(int dataPackageLength, int version, String senderAddress, String receiverAddress, String message, int seqNum){
        this.dataPackageLength = dataPackageLength;
        this.version = version;
        this.senderAddress = senderAddress;
        this.receiverAddress = receiverAddress;
        this.message = message;
        this.seqNum = seqNum;
    }

    public void setVersion(int version){
        this.version = version;
    }

    public void setSenderAddress(String senderAddress){
        this.senderAddress = senderAddress;
    }
    public void setReceiverAddress(String receiverAddress){
        this.receiverAddress = receiverAddress;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public void setSeqNum(int seqNum){
        this.seqNum = seqNum;
    }
    public void setDataPackageLength(int dataPackageLength) {
        this.dataPackageLength = dataPackageLength;
    }

    public int getDataPackageLength() {
        return dataPackageLength;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public int getVersion() {
        return version;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public String getMessage() { return message; }

    public int getSeqNum() {
        return seqNum;
    }

    /**
     * Gibt die vom User eingegebenen Informationen gebündelt und übersichtlich aus
     */
    public void printAll(){
        System.out.println("Die maximale Datenteil-Länge ist " + dataPackageLength + "."
                + System.lineSeparator()
                + System.lineSeparator()
                + "Version: " + version
                + System.lineSeparator()
                + "Absender: " + senderAddress
                + System.lineSeparator()
                + "Empfänger: " + receiverAddress
                + System.lineSeparator()
                + "Nachricht: "
                + System.lineSeparator()
                + message.replace("\\n","\n")
                + System.lineSeparator()
                + ".");
    }

    /**
     * Gibt die einzelnen Pakete mit sämtlichen Informationen aus
     */
    public void printMessage(){
        System.out.println( "Version: " + version
                + System.lineSeparator()
                + "Absender: " + senderAddress
                + System.lineSeparator()
                + "Empfänger: " + receiverAddress
                + System.lineSeparator()
                + "Paketlaufnummer: " + seqNum
                + System.lineSeparator()
                + "Datenteil-Länge: " + message.length()
                + System.lineSeparator()
                + "Datenteil: " + message + "\n");
    }


}
