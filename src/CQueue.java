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
    //длина очереди
    private int length;

    //Публичный конструктор
    public CQueue() {
        first = 0; last = 9; length = 0;
        queue = new int[10];

        block = new ReentrantLock();
        getter = block.newCondition(); setter = block.newCondition();
    }

    //Вывести очередь в консоль
    private void printQueue() {
        System.out.println("Состояние очереди:");
        if (first < last) {
            for (int i = first; i <= last; i++)
                System.out.print(queue[i] + " ");
        }
        else {
            for (int i = first; i < 10; i++)
                System.out.print(queue[i] + " ");
            for (int i = 0; i < last; i++)
                System.out.print(queue[i] + " ");
        }
        System.out.println();
    }

    public int getLength() {
        return length;
    }

    @Override
    public void put(int val) {
        //Блокировка метода
        block.lock();
         {
            try {
                //Если очередь полная, потребители ждут
                while (full()) getter.await();
                //Увеличение указателя конца
                last = (last + 1) % 10;
                queue[last] = val;
                //Печать очереди
                printQueue();
                //Длина уменьшилась на 1
                length++;
                //Сигнал о том, что очередь не пустая и можно использовать get()
                setter.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Даже если возникнет прерывание, блокировку нужно снять
            finally {
                block.unlock();
            }
        }
    }

    @Override
    public int get() {
        //Блокировка метода
        block.lock();
        try {
            //Если очередь пуста, то производители ждут
            while (empty()) setter.await();
            //Увеличение указателя начала
            first = (first + 1) % 10;
            printQueue();
            length--;
            //Сигнал о том, что очередь не полная и можно использовать put()
            getter.signalAll();
            return queue[first];
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Даже если возникнет прерывание, блокировку нужно снять
        finally {
            block.unlock();
        }
        return 0;
    }

    @Override
    public boolean full() {
        return ((first + 1) + 1) % 10 == last;
    }

    @Override
    public boolean empty() {
        return (first + 1) % 10 == last;
    }
}
