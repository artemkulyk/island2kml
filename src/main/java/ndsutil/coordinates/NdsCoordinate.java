package ndsutil.coordinates;

import ndsutil.utils.CoordinateUtils;

/**
 * Represents a coordinate in the NDS (Navigation Data Standard) format, with longitude, latitude, and elevation values.
 * <p>
 * NDS coordinates are often used in navigation systems to represent geographic points in a standardized format.
 */
public record NdsCoordinate(long lon, long lat, long elevation) {

    /**
     * Factory method to create an NdsCoordinate from a WGS84 coordinate.
     * The conversion uses a utility method to transform the WGS84 longitude and latitude into NDS format.
     *
     * @param coordinate the WGS84Coordinate to be converted to an NdsCoordinate
     * @return the corresponding NdsCoordinate after conversion
     */
    public static NdsCoordinate fromCoordinate(WGS84Coordinate coordinate) {
        long x = CoordinateUtils.longitudeWGS84ToNds(coordinate.lon());
        long y = CoordinateUtils.latitudeWGS84ToNds(coordinate.lat());
        return new NdsCoordinate(x, y, Math.round(coordinate.elevation()));
    }

    /**
     * Converts this NDS coordinate back to a WGS84 coordinate.
     * The conversion uses utility methods to transform the NDS longitude and latitude into WGS84 format.
     *
     * @return the corresponding WGS84Coordinate after conversion
     */
    public WGS84Coordinate toWGS84Coordinate() {
        double longitude = CoordinateUtils.longitudeNdsToWGS84(lon);
        double latitude = CoordinateUtils.latitudeNdsToWGS84(lat);
        return WGS84Coordinate.of(longitude, latitude, elevation);
    }

    /**
     * Returns a string representation of the NDS coordinate.
     * The string contains the longitude, latitude, and elevation values in a readable format.
     *
     * @return a string describing the NDS coordinate
     */
    @Override
    public String toString() {
        return "NdsCoordinate [lon=" + lon + ", lat=" + lat + ", elevation=" + elevation + "]";
    }
}