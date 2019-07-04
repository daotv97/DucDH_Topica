package com.topica.collection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CircleProcessorImpl extends CircleProcessor {

    @Override
    public Map<Integer, Circle> initializeCircle() {
        Map<Integer, Circle> circles = new HashMap<>();
        for (int index = 0; index < 100; index++) circles.put(index, new Circle());
        return circles;
    }

    @Override
    public void showAll(Map<Integer, Circle> circleMap) {
        circleMap.forEach(this::accept);
    }

    @Override
    public Map<Integer, Circle> sortByRadiusAsc(Map<Integer, Circle> circleMap) {
        return circleMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue((e1, e2) -> e2.getRadius().compareTo(e1.getRadius())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public void findCircleByAcreage(Integer acreage) {

    }

    private void accept(Integer index, Circle circle) {
        System.out.println(new StringBuilder()
                .append("circle index: ")
                .append(index)
                .append("-- radius: ")
                .append(circle.getRadius())
                .append("-- acreage: ")
                .append(circle.acreageCircle())
                .toString()
        );
    }

//    private Boolean isApproximate() {
//
//    }
}