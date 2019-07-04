package com.topica.collection;

import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CircleProcessorImpl processor = new CircleProcessorImpl();
        Map<Integer, Circle> circleMap = processor.initializeCircle();
        System.out.println("=== Before sort ===");
        processor.showAll(circleMap);

        System.out.println("\n=== After sort ===");
        Map<Integer, Circle> circleMapAfterSort = processor.sortByRadiusAsc(circleMap);
        processor.showAll(circleMapAfterSort);

        System.out.println("\n=== Search ===");
        Integer radius = Integer.parseInt(new Scanner(System.in).nextLine());
        if (radius < 1 && radius > 100) {
            System.out.println("Search not found!");
        } else {
            processor.findCircleByAcreage(radius);
        }

    }
}
