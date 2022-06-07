public class Seller implements Runnable {
    private CQueue queue;
    private int value;

    public Seller(CQueue queue) {
        this.queue = queue;
        value = 1;
        //Запуск из конструктора
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            //Поместить товар в очередь
            try {
                queue.put(value);
                System.out.println("Доставлен товар: " + value);
                //Если длина не равна количеству произведенного товара -> товар взяли, нужно увеличить значение value

                if (queue.isGot()) {
                    value++;
                }

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
