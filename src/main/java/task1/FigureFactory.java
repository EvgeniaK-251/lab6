package task1;

import geometry2d.Figure;
import exceptions.InvalidHeightException;
import exceptions.InvalidRadiusException;

@FunctionalInterface
public interface FigureFactory {
    Figure create() throws InvalidRadiusException, InvalidHeightException;
}