class NumberPrinter {
    public static void printZero() {
        System.out.print("0");
    }

    public static void printEven(int num) {
        System.out.print(num);
    }

    public static void printOdd(int num) {
        System.out.print(num);
    }
}

public class ThreadController {
    private final int n;
    private int nextNumber = 1;
    private boolean isZeroTurn = true;
    private final Object lock = new Object();

    public ThreadController(int n) {
        this.n = n;
    }

    public void zero() throws InterruptedException {
        synchronized (lock) {
            while (nextNumber <= n) {
                if (!isZeroTurn) {
                    lock.wait();
                    continue;
                }
                NumberPrinter.printZero();
                isZeroTurn = false;
                lock.notifyAll();
            }
        }
    }

    public void even() throws InterruptedException {
        synchronized (lock) {
            while (nextNumber <= n) {
                if (isZeroTurn || nextNumber % 2 != 0) {
                    lock.wait();
                    continue;
                }
                NumberPrinter.printEven(nextNumber);
                nextNumber++;
                isZeroTurn = true;
                lock.notifyAll();
            }
        }
    }

    public void odd() throws InterruptedException {
        synchronized (lock) {
            while (nextNumber <= n) {
                if (isZeroTurn || nextNumber % 2 == 0) {
                    lock.wait();
                    continue;
                }
                NumberPrinter.printOdd(nextNumber);
                nextNumber++;
                isZeroTurn = true;
                lock.notifyAll();
            }
        }
    }
}
class Main {
    public static void main(String[] args) {
        int n = 20;
        ThreadController controller = new ThreadController(n);

        Thread zeroThread = new Thread(() -> {
            try {
                controller.zero();
            } catch (InterruptedException e) {
            }
        });

        Thread evenThread = new Thread(() -> {
            try {
                controller.even();
            } catch (InterruptedException e) {
            }
        });

        Thread oddThread = new Thread(() -> {
            try {
                controller.odd();
            } catch (InterruptedException e) {
            }
        });

        zeroThread.start();
        oddThread.start();
        evenThread.start();

        try {
            zeroThread.join();
            oddThread.join();
            evenThread.join();
        } catch (InterruptedException e) {
        }
    }
}
