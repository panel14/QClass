public class Buyer implements Runnable {
    private CQueue queue;

    public Buyer(CQueue queue) {
        this.queue = queue;
        //Запуск из конструктора
        new Thread(this).start();
    }
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(3000);
                //Взять товар из очереди
                System.out.println("Куплен товар: " + queue.get() + ". Покупатель: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
