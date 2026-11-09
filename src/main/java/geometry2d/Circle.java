package geometry2d;

import exceptions.InvalidRadiusException;

public class Circle implements Figure {
    private double radius;

    // Конструктор
    public Circle(double radius) throws InvalidRadiusException {
        if (radius <= 0) {
            throw new InvalidRadiusException("Радиус должен быть больше нуля: " + radius);
        }
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public double perimetr() {
        return 2 * Math.PI * radius;
    }

    @Override
    public String toString() {
        return "Circle{radius=" + radius + "}";
    }
}
