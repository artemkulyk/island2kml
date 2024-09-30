package ndsutil.utils;

/**
 * Contains constants used throughout the mapget project related to geographical calculations.
 * <p>
 * These constants define various geographical boundaries, such as the minimum and maximum
 * latitude and longitude, and also provide the Earth's equatorial radius in meters.
 * They are essential for ensuring that geographic coordinates are handled correctly in the project.
 */
public final class GeoConstants {

    /**
     * The Earth's equatorial radius in meters.
     * <p>
     * This value is used in various calculations that involve the Earth's shape and size, such as
     * determining distances between points on the surface of the Earth.
     */
    public static final double EARTH_EQUATOR_RADIUS = 6_371_000.0; // in meters

    /**
     * The minimum valid longitude value, representing the westernmost point on the globe (-180°).
     */
    public static final double MIN_LONGITUDE = -180.0;

    /**
     * The maximum valid longitude value, representing the easternmost point on the globe (180°).
     */
    public static final double MAX_LONGITUDE = 180.0;

    /**
     * The minimum valid latitude value, representing the southernmost point on the globe (-90°).
     */
    public static final double MIN_LATITUDE = -90.0;

    /**
     * The maximum valid latitude value, representing the northernmost point on the globe (90°).
     */
    public static final double MAX_LATITUDE = 90.0;

    /**
     * The total extent of longitude, representing the full range of valid longitude values (360°).
     */
    public static final double LON_EXTENT = MAX_LONGITUDE - MIN_LONGITUDE;

    /**
     * The total extent of latitude, representing the full range of valid latitude values (180°).
     */
    public static final double LAT_EXTENT = MAX_LATITUDE - MIN_LATITUDE;

    /**
     * Private constructor to prevent instantiation of this utility class.
     * This class is not meant to be instantiated because it only provides static constants.
     */
    private GeoConstants() {
        // Prevent instantiation
    }
}