package ndsutil.utils;

/**
 * Utility class for coordinate conversions and calculations.
 */
public final class CoordinateUtils {
    private CoordinateUtils() {
        // Prevent instantiation
    }

    /**
     * Converts lon in degrees to NDS lon-coordinate.
     */
    public static long longitudeWGS84ToNds(double lon) {
        long x = (long) Math.floor(lon / 90.0 * 0x40000000);
        return x >= 0 ? x : x + 0x100000000L;
    }

    /**
     * Converts lat in degrees to NDS lat-coordinate.
     */
    public static long latitudeWGS84ToNds(double lat) {
        long y = (long) Math.floor(lat / 90.0 * 0x40000000);
        return y >= 0 ? y : y + 0x80000000L;
    }

    /**
     * Converts NDS lon-coordinate to lon in degrees.
     */
    public static double longitudeNdsToWGS84(long lon) {
        if (lon >= 0x80000000L)
            lon -= 0x100000000L;
        return 90.0 * lon / 0x40000000L;
    }

    /**
     * Converts NDS lat-coordinate to lat in degrees.
     */
    public static double latitudeNdsToWGS84(long lat) {
        if (lat >= 0x40000000L)
            lat -= 0x80000000L;
        return 90.0 * lat / 0x40000000L;
    }
}