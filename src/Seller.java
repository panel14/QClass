public class Seller implements Runnable {
    private CQueue queue;
    private int value;
    private int curValue;

    public Seller(CQueue queue) {
        this.queue = queue;
        value = 1;
        curValue = value;
        //Запуск из конструктора
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            //Поместить товар в очередь
            queue.put(value);
            curValue++;
            System.out.println("Доставлен товар: " + value);
            //Если длина не равна количеству произведенного товара -> товар взяли, нужно увеличить значение value
            if (curValue != queue.getLength()) {
                value = value + 1;
                curValue = queue.getLength();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
