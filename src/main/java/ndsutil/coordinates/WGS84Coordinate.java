package ndsutil.coordinates;

import ndsutil.utils.GeoConstants;

/**
 * Represents a geographical coordinate in WGS84 format with lon, lat and elevation.
 */
public record WGS84Coordinate(double lon, double lat, double elevation) {

    /**
     * Factory method to create a Coordinate with validation.
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

    @Override
    public String toString() {
        return "WGS84Coordinate [lon=" + lon + ", lat=" + lat + ", elevation=" + elevation + "]";
    }
}