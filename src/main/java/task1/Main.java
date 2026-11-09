package task1;

import geometry2d.Circle;
import geometry2d.Figure;
import geometry2d.Rectangle;
import exceptions.InvalidHeightException;
import exceptions.InvalidRadiusException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {

    private final List<DrawFigure> figures = new ArrayList<>();
    private final Random random = new Random();
    private Canvas canvas;

    // Состояние перетаскивания
    private DrawFigure dragged = null;
    private double dragOffsetX = 0;
    private double dragOffsetY = 0;

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(700, 500);
        clearCanvas();

        // ==== Обработчики мыши ====
        canvas.setOnMousePressed(e -> {
            double mx = e.getX();
            double my = e.getY();

            if (e.getButton() == MouseButton.PRIMARY) {
                // Левая кнопка: "захват" верхней фигуры под курсором
                DrawFigure hit = findTopmostAt(mx, my);
                if (hit != null) {
                    dragged = hit;
                    dragOffsetX = mx - hit.getX();
                    dragOffsetY = my - hit.getY();

                    // Выход на первый план: удаляем из списка и добавляем в конец.
                    // Остальные фигуры сохраняют свой относительный порядок.
                    figures.remove(hit);
                    figures.add(hit);
                    redraw();
                }
            } else if (e.getButton() == MouseButton.SECONDARY) {
                // Правая кнопка: меняем цвет верхней фигуры под курсором
                DrawFigure hit = findTopmostAt(mx, my);
                if (hit != null) {
                    hit.setColor(randomColor());
                    redraw();
                }
            }
        });

        canvas.setOnMouseDragged(e -> {
            if (dragged != null) {
                dragged.moveTo(e.getX() - dragOffsetX, e.getY() - dragOffsetY);
                redraw();
            }
        });

        canvas.setOnMouseReleased(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                dragged = null;
            }
        });

        // ==== Панель кнопок ====
        VBox buttonsBox = new VBox(10);
        buttonsBox.setPadding(new Insets(10));
        buttonsBox.setStyle("-fx-background-color: #f0f0f0;");
        buttonsBox.getChildren().add(new Label("Фигуры:"));

        buttonsBox.getChildren().add(createButton("Круг", () -> {
            try {
                return randomCircle();
            } catch (InvalidRadiusException ex) {
                throw new RuntimeException(ex);
            }
        }));
        buttonsBox.getChildren().add(createButton("Прямоугольник", () -> {
            try {
                return randomRectangle();
            } catch (InvalidHeightException ex) {
                throw new RuntimeException(ex);
            }
        }));

        Button clearBtn = new Button("Очистить");
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setOnAction(e -> {
            figures.clear();
            clearCanvas();
        });
        buttonsBox.getChildren().add(clearBtn);

        BorderPane root = new BorderPane();
        root.setLeft(buttonsBox);
        root.setCenter(canvas);

        stage.setTitle("Geometry2D — JavaFX");
        stage.setScene(new Scene(root, 900, 520));
        stage.show();
    }

    /**
     * Ищем верхнюю фигуру под точкой (px, py).
     * Идём с конца списка — последняя нарисованная лежит поверх всех.
     */
    private DrawFigure findTopmostAt(double px, double py) {
        for (int i = figures.size() - 1; i >= 0; i--) {
            if (figures.get(i).contains(px, py)) {
                return figures.get(i);
            }
        }
        return null;
    }

    private Button createButton(String label, FigureFactory factory) {
        Button btn = new Button(label);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> {
            try {
                Figure f = factory.create();
                DrawFigure df = new DrawFigure(
                        f,
                        randInRange(80, canvas.getWidth() - 80),
                        randInRange(80, canvas.getHeight() - 80),
                        40 + random.nextDouble() * 100,
                        randomColor()
                );
                figures.add(df);
                redraw();
            } catch (InvalidRadiusException | InvalidHeightException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Не удалось создать фигуру");
                alert.setContentText(ex.getMessage());
                alert.show();
            }
        });
        return btn;
    }

    private Figure randomCircle() throws InvalidRadiusException {
        return new Circle(20 + random.nextDouble() * 60);
    }

    private Figure randomRectangle() throws InvalidHeightException {
        return new Rectangle(
                20 + random.nextDouble() * 100,
                20 + random.nextDouble() * 100
        );
    }

    private double randInRange(double min, double max) {
        return min + random.nextDouble() * (max - min);
    }

    private Color randomColor() {
        return Color.color(
                0.3 + random.nextDouble() * 0.7,
                0.3 + random.nextDouble() * 0.7,
                0.3 + random.nextDouble() * 0.7,
                0.9
        );
    }

    private void redraw() {
        clearCanvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (DrawFigure df : figures) {
            df.draw(gc);
        }
    }

    private void clearCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setStroke(Color.LIGHTGRAY);
        for (int x = 0; x < canvas.getWidth(); x += 50) {
            gc.strokeLine(x, 0, x, canvas.getHeight());
        }
        for (int y = 0; y < canvas.getHeight(); y += 50) {
            gc.strokeLine(0, y, canvas.getWidth(), y);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}