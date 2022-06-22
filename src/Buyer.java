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
                //Случайное число итераций (сколько товара возьмем)
                int iterCount = (int)(Math.random() * (10 - 1)) + 1;
                for (int i = 0; i < iterCount; i++)
                    System.out.println("Куплен товар: " + queue.get() + ". Покупатель: " + Thread.currentThread().getName());
                Thread.sleep(100);
                //Взять товар из очереди
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
