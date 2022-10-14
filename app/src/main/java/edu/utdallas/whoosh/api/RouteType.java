package edu.utdallas.whoosh.api;

/**
 * An enumeration listing the various types of {@link IRoute}s.
 *
 * Created by sasha on 9/21/15.
 */
public enum RouteType {

    Walking(4.5),
    Crutches(1.5),
    Wheelchair(2.5),
    ;

    private double rateInFeetPerSecond;

    RouteType(double rateInFeetPerSecond) {
        this.rateInFeetPerSecond = rateInFeetPerSecond;
    }

    /**
     * The constant rate (in feet per second) associated with this mode of transit
     */
    public double getRateInFeetPerSecond() {
        return rateInFeetPerSecond;
    }

}