import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//Класс очереди (имплементирует интерфейс)
public class CQueue implements Queue{
    //блокировка
    private ReentrantLock block;
    //два условия блокировки (для производителя и потребителя)
    private Condition getter;
    private Condition setter;
    private int[] queue;
    //указатели на начало и на конец очереди
    private int first;
    private int last;

    private boolean isGot;

    //Публичный конструктор
    public CQueue() {
        first = 0; last = 9;
        queue = new int[10];
        isGot = false;

        block = new ReentrantLock();
        getter = block.newCondition(); setter = block.newCondition();
    }

    //Вывести очередь в консоль
    private void printQueue() {
        block.lock();

        System.out.println("Состояние очереди:");
        int firstPointer = first; int lastPointer = last;

        while (!(getNext(firstPointer) == lastPointer)) {
            System.out.print(queue[getNext(firstPointer)] + " ");
            firstPointer = getNext(firstPointer);
        }
        System.out.println();

        block.unlock();
    }

    private int getNext(int index) {
        return ++index % 10;
    }

    public boolean getGot() {
        return isGot;
    }

    private void changeGot(boolean value) {
        block.lock();
        isGot = value;
        block.unlock();
    }

    @Override
    public void put(int val) throws InterruptedException {
        //Блокировка метода
        block.lock();
         {
            try {
                //Если очередь полная, потребители ждут
                while (full()) getter.await();
                //Увеличение указателя конца
                last = getNext(last);
                queue[last] = val;
                //Печать очереди
                printQueue();
                isGot = false;
                //Сигнал о том, что очередь не пустая и можно использовать get()
                setter.signalAll();
            }
            //Даже если возникнет прерывание, блокировку нужно снять
            finally {
                block.unlock();
            }
        }
    }

    @Override
    public int get() throws InterruptedException {
        //Блокировка метода
        block.lock();
        try {
            //Если очередь пуста, то производители ждут
            while (empty()) setter.await();
            //Увеличение указателя начала
            first = getNext(first);
            printQueue();
            //Сигнал о том, что очередь не полная и можно использовать put()
            isGot = true;
            getter.signalAll();
            return queue[first];
        }
        //Даже если возникнет прерывание, блокировку нужно снять
        finally {
            block.unlock();
        }
    }

    @Override
    public boolean full() {
        return getNext(getNext(last)) == first;
    }

    @Override
    public boolean empty() {
        return getNext(last) == first;
    }
}
