package GU.Lesson04;

public class SequenceThread extends Thread{
    private static int repeatCnt = 5;
    private static char lastChar = 'C';
    private static Object sequenceMonitor = new Object();

    private char startChar;
    private char nextChar;


    public SequenceThread(char startChar, char nextChar) {
        this.startChar = startChar;
        this.nextChar = nextChar;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < repeatCnt; i++) {
                synchronized (sequenceMonitor) {
                    while (lastChar != startChar) {
                        sequenceMonitor.wait();
                    }
                    System.out.print(nextChar);
                    lastChar = nextChar;
                    sequenceMonitor.notifyAll();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
