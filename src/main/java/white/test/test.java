package white.test;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class test {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Date> queue = new ArrayBlockingQueue<>(10);
        producer producer1 = new producer(queue);
        producer producer2 = new producer(queue);
        producer producer3 = new producer(queue);
        Consumer consumer1 = new Consumer(queue);
        Consumer consumer2 = new Consumer(queue);
        Consumer consumer3 = new Consumer(queue);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(producer1);
        executorService.execute(producer2);
        executorService.execute(producer3);
        executorService.execute(consumer1);
        executorService.execute(consumer2);
        executorService.execute(consumer3);
        Thread.sleep(3000);
        producer1.stop();
        producer2.stop();
        producer3.stop();
        Thread.sleep(1000);
        executorService.shutdown();

    }
    @Data
    @AllArgsConstructor
    static
    class Date{
        int num;
        int id;
    }
    static class producer implements Runnable{
        private BlockingQueue<Date> queue;
        private volatile boolean isRunning = true;
        private  AtomicInteger count  = new AtomicInteger();
        private Random random = new Random();
        public producer(BlockingQueue<Date> queue){
            this.queue = queue;
        }

        @Override
        public void run() {
            try{
                while (isRunning){
                    Thread.sleep(random.nextInt(1000));
                    int num = count.incrementAndGet();
                    Date date = new Date(num,num);
                    System.out.println("当前>>注水管:"+Thread.currentThread().getName() + "注水容量" + num);
                    if (!queue.offer(date,2, TimeUnit.SECONDS)){
                        System.out.println("fall");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        public void stop (){
            isRunning  = false;
        }
    }
    static class Consumer implements Runnable{

        private BlockingQueue<Date> queue ;

        private  Random random = new Random();

        public Consumer(BlockingQueue<Date> queue){
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true){
                try {
                    Date data = queue.take();
                    //模拟抽水耗时
                    Thread.sleep(random.nextInt(1000));
                    if(data != null){
                        System.out.println("当前<<抽水管:"+Thread.currentThread().getName()+",抽取水容量(L):"+data.getNum());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }
}
