package geometry2d;

import exceptions.InvalidHeightException;

public class Rectangle implements Figure {
    private double width;
    private double height;

    public Rectangle(double width, double height) throws InvalidHeightException {
        if (width <= 0 || height <= 0) {
            throw new InvalidHeightException(
                    "Стороны прямоугольника должны быть больше нуля: " + width + " x " + height
            );
        }
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double perimetr() {
        return 2 * (width + height);
    }

    @Override
    public String toString() {
        return "Rectangle{width=" + width + ", height=" + height + "}";
    }
}

