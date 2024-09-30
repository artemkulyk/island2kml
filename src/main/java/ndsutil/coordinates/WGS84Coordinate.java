package ndsutil.coordinates;

import ndsutil.utils.GeoConstants;

/**
 * Represents a geographical coordinate in the WGS84 format, with longitude, latitude, and elevation values.
 * <p>
 * WGS84 (World Geodetic System 1984) is a standard used in cartography, geodesy, and navigation,
 * including GPS, to represent geographic locations on the Earth.
 */
public record WGS84Coordinate(double lon, double lat, double elevation) {

    /**
     * Factory method to create a WGS84Coordinate with validation.
     * <p>
     * Ensures that the longitude and latitude are within their respective valid ranges
     * before creating the coordinate.
     *
     * @param lon the longitude in degrees, which must be between -180 and 180
     * @param lat the latitude in degrees, which must be between -90 and 90
     * @param elevation the elevation in meters above sea level
     * @return a validated WGS84Coordinate object
     * @throws IllegalArgumentException if the longitude or latitude are out of the valid ranges
     */
    public static WGS84Coordinate of(double lon, double lat, double elevation) {
        if (lon < GeoConstants.MIN_LONGITUDE || lon > GeoConstants.MAX_LONGITUDE) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees.");
        }
        if (lat < GeoConstants.MIN_LATITUDE || lat > GeoConstants.MAX_LATITUDE) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees.");
        }
        return new WGS84Coordinate(lon, lat, elevation);
    }

    /**
     * Returns a string representation of the WGS84 coordinate.
     * <p>
     * The string contains the longitude, latitude, and elevation values in a readable format.
     *
     * @return a string describing the WGS84 coordinate
     */
    @Override
    public String toString() {
        return "WGS84Coordinate [lon=" + lon + ", lat=" + lat + ", elevation=" + elevation + "]";
    }
}