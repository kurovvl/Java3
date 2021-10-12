package GU.Lesson04;

public class Main {
    public static void main(String[] args) {

        new SequenceThread('B', 'C').start();
        new SequenceThread('A', 'B').start();
        new SequenceThread('C', 'A').start();

    }
}
