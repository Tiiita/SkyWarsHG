package de.tiiita.skywarshg.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Box {
    private double xMin, xMax;
    private double yMin, yMax;
    private double zMin, zMax;

    private boolean isBetween(double arg, double lower, double upper) {
        double _lower = Math.min(lower,upper);
        double _upper = Math.max(lower,upper);

        return (_lower <= arg && arg <= _upper);
    }

    public boolean isInside(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return isBetween(x, xMin, xMax) && isBetween(y, yMin, yMax) && isBetween(z, zMin, zMax);
    }

    public boolean isInside(Vector location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return isBetween(x, xMin, xMax) && isBetween(y, yMin, yMax) && isBetween(z, zMin, zMax);
    }

    public Box(Vector location1, Vector location2) {
        xMin = Math.min(location1.getX(), location2.getX());
        yMin = Math.min(location1.getY(), location2.getY());
        zMin = Math.min(location1.getZ(), location2.getZ());

        xMax = Math.max(location1.getX(), location2.getX());
        yMax = Math.max(location1.getY(), location2.getY());
        zMax = Math.max(location1.getZ(), location2.getZ());

        //System.out.println("Box: " + xMin + "," + yMin+ "," + zMin + " / " + xMax+ "," + yMax+ "," + zMax);
    }
}