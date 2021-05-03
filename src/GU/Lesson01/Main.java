package GU.Lesson01;

import GU.Lesson01.Fruits.Apple;
import GU.Lesson01.Fruits.Box;
import GU.Lesson01.Fruits.Orange;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

public class Main {

    private static HWPoints hwPoints = new HWPoints();


    public static void main(String[] args) {
        // 1. Написать метод, который меняет два элемента массива местами.(массив может быть любого ссылочного типа);
        pointOne();

        // 2. Написать метод, который преобразует массив в ArrayList;
        pointTwo();

        // 3. Большая задача:
        pointThree();

    }

    public static void pointOne() {

        String[] strArr = new String[]{
                "AAA", "BBB", "CCC", "DDD", "EEE"
        };
        Integer[] intArr = new Integer[]{
                1, 2, 3, 4, 5
        };


        System.out.println(Arrays.toString(hwPoints.changeArrayPlacement(strArr)));
        System.out.println(Arrays.toString(hwPoints.changeArrayPlacement(intArr)));
    }

    public static void pointTwo() {

        String[] strArr = new String[]{
                "AAA", "BBB", "CCC", "DDD", "EEE"
        };
        Integer[] intArr = new Integer[]{
                1, 2, 3, 4, 5
        };
        System.out.println(hwPoints.arrayToListArray(strArr));
        System.out.println(hwPoints.arrayToListArray(intArr));

    }

    public static void pointThree(){
        Function<Object,Integer> rnd  = o ->  new Random().nextInt(10) + 2;
        Box<Apple> boxOfApples = new Box();
        Box<Orange> boxOfOranges = new Box();
        Box<Apple> newAppleBox = new Box<>();

        for (int i = 0; i < new Random().nextInt(10) + 2; i++) {
            boxOfApples.putInto(new Apple());

        }
        for (int i = 0; i < new Random().nextInt(10) + 2; i++) {
            boxOfOranges.putInto(new Orange());
        }


        System.out.println(boxOfApples.getWeight());
        System.out.println(boxOfOranges.getWeight());
        System.out.println(boxOfApples.boxWeightEqualsOf(boxOfOranges));

        boxOfApples.relocateContentTo(newAppleBox);

        System.out.println(boxOfApples.getWeight());
        System.out.println(newAppleBox.getWeight());
    }
}
