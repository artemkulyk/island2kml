package ndsutil.utils;

/**
 * Utility class for coordinate conversions and calculations between WGS84 and NDS formats.
 * <p>
 * The NDS (Navigation Data Standard) uses a specific coordinate system to represent geographic points,
 * and this utility class helps convert between NDS coordinates and the more commonly used WGS84 format.
 */
public final class CoordinateUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * As this class only contains static methods, it is not meant to be instantiated.
     */
    private CoordinateUtils() {
        // Prevent instantiation
    }

    /**
     * Converts a longitude value in degrees (WGS84) to an NDS longitude coordinate.
     * <p>
     * The NDS system uses a specific format where the longitude is represented as a long value.
     *
     * @param lon the longitude in degrees, must be between -180 and 180
     * @return the corresponding NDS longitude coordinate as a long value
     */
    public static long longitudeWGS84ToNds(double lon) {
        long x = (long) Math.floor(lon / 90.0 * 0x40000000);
        return x >= 0 ? x : x + 0x100000000L;
    }

    /**
     * Converts a latitude value in degrees (WGS84) to an NDS latitude coordinate.
     * <p>
     * The NDS system uses a specific format where the latitude is represented as a long value.
     *
     * @param lat the latitude in degrees, must be between -90 and 90
     * @return the corresponding NDS latitude coordinate as a long value
     */
    public static long latitudeWGS84ToNds(double lat) {
        long y = (long) Math.floor(lat / 90.0 * 0x40000000);
        return y >= 0 ? y : y + 0x80000000L;
    }

    /**
     * Converts an NDS longitude coordinate back to a longitude value in degrees (WGS84).
     * <p>
     * This method reverses the conversion done by {@link #longitudeWGS84ToNds(double)}.
     *
     * @param lon the NDS longitude coordinate as a long value
     * @return the corresponding longitude in degrees, between -180 and 180
     */
    public static double longitudeNdsToWGS84(long lon) {
        if (lon >= 0x80000000L)
            lon -= 0x100000000L;
        return 90.0 * lon / 0x40000000L;
    }

    /**
     * Converts an NDS latitude coordinate back to a latitude value in degrees (WGS84).
     * <p>
     * This method reverses the conversion done by {@link #latitudeWGS84ToNds(double)}.
     *
     * @param lat the NDS latitude coordinate as a long value
     * @return the corresponding latitude in degrees, between -90 and 90
     */
    public static double latitudeNdsToWGS84(long lat) {
        if (lat >= 0x40000000L)
            lat -= 0x80000000L;
        return 90.0 * lat / 0x40000000L;
    }
}