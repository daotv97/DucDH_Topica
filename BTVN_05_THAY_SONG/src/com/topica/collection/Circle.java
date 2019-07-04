package com.topica.collection;

import java.util.Random;

public class Circle {
    private Integer radius;

    public Circle() {
        this.radius = getRandomNumberInRange(Constant.MIN_VALUE, Constant.MAX_VALUE);
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Max value must be greater than min value.");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public Double acreageCircle() {
        return Constant.PI * Math.pow(this.radius, 2);
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }
}
