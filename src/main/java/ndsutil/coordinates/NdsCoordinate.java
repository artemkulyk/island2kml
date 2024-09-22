package ndsutil.coordinates;

import ndsutil.utils.CoordinateUtils;

/**
 * Represents an NDS (Navigation Data Standard) coordinate with lon, lat and elevation values.
 */
public record NdsCoordinate(long lon, long lat, long elevation) {

    /**
     * Factory method to create an NdsCoordinate from a WGS84 Coordinate.
     */
    public static NdsCoordinate fromCoordinate(WGS84Coordinate coordinate) {
        long x = CoordinateUtils.longitudeWGS84ToNds(coordinate.lon());
        long y = CoordinateUtils.latitudeWGS84ToNds(coordinate.lat());
        return new NdsCoordinate(x, y, Math.round(coordinate.elevation()));
    }

    /**
     * Converts this NDS coordinate back to a WGS84 coordinate.
     */
    public WGS84Coordinate toWGS84Coordinate() {
        double longitude = CoordinateUtils.longitudeNdsToWGS84(lon);
        double latitude = CoordinateUtils.latitudeNdsToWGS84(lat);
        return WGS84Coordinate.of(longitude, latitude, elevation);
    }

    @Override
    public String toString() {
        return "NdsCoordinate [lon=" + lon + ", lat=" + lat + ", elevation=" + elevation + "]";
    }
}