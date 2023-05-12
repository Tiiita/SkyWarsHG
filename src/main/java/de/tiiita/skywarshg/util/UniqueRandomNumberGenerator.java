package de.tiiita.skywarshg.util;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UniqueRandomNumberGenerator {
    private List<Integer> numbers;
    private int currentIndex;
    private Random random;

    public UniqueRandomNumberGenerator(int a, int b) {
        if (a > b) {
            throw new IllegalArgumentException("Invalid range: a must be less than or equal to b");
        }

        numbers = new ArrayList<>();
        for (int i = a; i <= b; i++) {
            numbers.add(i);
        }

        currentIndex = numbers.size() - 1;
        random = new Random();
    }

    public int getRandomNumber() {
        if (currentIndex < 0) {
            throw new IllegalStateException("No more unique numbers available");
        }

        int randomIndex = random.nextInt(currentIndex + 1);
        int randomNumber = numbers.get(randomIndex);

        // Swap the chosen number with the last number in the list
        int lastNumber = numbers.get(currentIndex);
        numbers.set(randomIndex, lastNumber);
        numbers.set(currentIndex, randomNumber);

        currentIndex--;

        return randomNumber;
    }
}
