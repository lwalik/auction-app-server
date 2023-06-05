package utills;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class UniqueIdGenerator {
    private final List<Integer> unavailableIds = Arrays.asList();

    public int generateId() {
        int number;
        while (true) {
            number = new Random().nextInt(9000) + 1000;
            if (!unavailableIds.contains(number)) {
                unavailableIds.add(number);
                break;
            }
        };

        return number;
    }
}
