package code.blockchain;



public class Block {
    long timestamp;
    String nonce;
    String proofOfWork;
    String data;
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
    public void setProofOfWork(String proofOfWork) {
        this.proofOfWork = proofOfWork;
    }
    public void setData(String data){
        String s = "";
        s+= timestamp + data + nonce + proofOfWork;
        this.data= s;

    }
    @Override
    public String toString() {
        String s ="";
        s += timestamp+"|"+data+"|"+nonce+"|"+proofOfWork;
        return s;
    }

}

