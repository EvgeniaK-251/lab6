package task1;

import geometry2d.Circle;
import geometry2d.Figure;
import geometry2d.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Обёртка над Figure из пакета geometry2d.
 * Хранит саму фигуру + её визуальные параметры (позиция, размер, цвет)
 * и умеет рисовать себя на холсте, проверять попадание точки
 * и менять положение/цвет.
 */
public class DrawFigure {

    private final Figure figure;
    private double x;       // центр фигуры по X
    private double y;       // центр фигуры по Y
    private final double scale;
    private Color color;    // цвет (меняется по правой кнопке)

    public DrawFigure(Figure figure, double x, double y, double scale, Color color) {
        this.figure = figure;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.color = color;
    }

    public Figure getFigure() { return figure; }

    public double getX() { return x; }
    public double getY() { return y; }

    public void moveTo(double newX, double newY) {
        this.x = newX;
        this.y = newY;
    }

    public void moveBy(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public void setColor(Color c) { this.color = c; }

    public Color getColor() { return color; }

    /**
     * Проверка, попадает ли точка (px, py) внутрь фигуры.
     * Для круга — расстояние до центра, для прямоугольника — границы.
     */
    public boolean contains(double px, double py) {
        if (figure instanceof Circle) {
            double r = scale / 2;
            double dx = px - x;
            double dy = py - y;
            return dx * dx + dy * dy <= r * r;
        } else if (figure instanceof Rectangle) {
            double w = scale;
            double h = scale * 0.7;
            return px >= x - w / 2 && px <= x + w / 2
                    && py >= y - h / 2 && py <= y + h / 2;
        } else {
            // fallback — как круг
            double r = scale / 2;
            double dx = px - x;
            double dy = py - y;
            return dx * dx + dy * dy <= r * r;
        }
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        if (figure instanceof Circle) {
            double r = scale / 2;
            gc.fillOval(x - r, y - r, r * 2, r * 2);
            gc.strokeOval(x - r, y - r, r * 2, r * 2);
        } else if (figure instanceof Rectangle) {
            double w = scale;
            double h = scale * 0.7;
            gc.fillRect(x - w / 2, y - h / 2, w, h);
            gc.strokeRect(x - w / 2, y - h / 2, w, h);
        } else {
            double r = scale / 2;
            gc.fillOval(x - r, y - r, r * 2, r * 2);
            gc.strokeOval(x - r, y - r, r * 2, r * 2);
        }
    }

    @Override
    public String toString() {
        return "DrawFigure{" + figure + ", x=" + x + ", y=" + y + ", scale=" + scale + "}";
    }
}