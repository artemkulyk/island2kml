package ndsutil.utils;

/**
 * Contains constants used throughout the mapget project.
 */
public final class GeoConstants {
    public static final double EARTH_EQUATOR_RADIUS = 6_371_000.0; // in meters
    public static final double MIN_LONGITUDE = -180.0;
    public static final double MAX_LONGITUDE = 180.0;
    public static final double MIN_LATITUDE = -90.0;
    public static final double MAX_LATITUDE = 90.0;
    public static final double LON_EXTENT = MAX_LONGITUDE - MIN_LONGITUDE;
    public static final double LAT_EXTENT = MAX_LATITUDE - MIN_LATITUDE;

    private GeoConstants() {
        // Prevent instantiation
    }
}