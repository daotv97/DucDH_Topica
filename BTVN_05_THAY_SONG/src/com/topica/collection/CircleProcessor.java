package com.topica.collection;

import java.util.Map;

public abstract class CircleProcessor {
    public abstract Map<Integer, Circle> initializeCircle();

    public abstract void showAll(Map<Integer, Circle> circleMap);

    public abstract Map<Integer, Circle> sortByRadiusAsc(Map<Integer, Circle> circleMap);

    public abstract void findCircleByAcreage(Integer acreage);
}
