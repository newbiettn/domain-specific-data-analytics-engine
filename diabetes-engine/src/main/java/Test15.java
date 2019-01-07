import java.util.concurrent.*;

public class Test15 {
    public int count() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("count");
        return 0;
    }
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Task task = new Task(400, TimeUnit.MILLISECONDS);
        task.runNotingCompletion();
        task.getResultIfPresent();

        System.out.println("Finished!");
    }
}

class Task extends RunnableWithTimeout{

    public Task(long timeout, TimeUnit unit) {
        super(timeout, unit);
    }

    @Override
    public Integer runNotingCompletion() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
        System.out.println("Completed!!");
        return 3;
    }
    @Override
    public void executeIfNotDoneAfterTimeout() {
        System.out.println("Not done");

    }
}
