public class Main {

    public static void main(String[] args) {
        CQueue queue = new CQueue();
        Seller seller = new Seller(queue);
        Buyer firstBuyer = new Buyer(queue);
        Buyer secondBuyer = new Buyer(queue);
    }
}
