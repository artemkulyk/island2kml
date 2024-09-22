import de.micromata.opengis.kml.v_2_2_0.*;
import nds.core.geometry.Line3D;
import nds.core.geometry.Position3D;
import ndsutil.coordinates.NdsCoordinate;
import ndsutil.coordinates.WGS84Coordinate;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.auth.ApiKeyAuth;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class App {
    private static final String BASE_PATH = "https://api.nds.live/island1";
    private static final String API_KEY = "YOUR_API_KEY_HERE";
    private static final String TILE_ID = "545379780";
    private static final String OUTPUT_KML_FILE = "island1.kml";

    public static void main(String[] args) {
        try {
            // Initialize API client
            ApiClient apiClient = initializeApiClient();

            // Fetch data from API
            NDSData ndsData = fetchNDSData(apiClient);

            // Generate KML file
            generateKMLFile(ndsData.laneGeometryLayer());

            System.out.println("Processing finished successfully.");
        } catch (ApiException | IOException e) {
            System.err.println("An error occurred during processing:");
            e.printStackTrace();
        }
    }

    private static ApiClient initializeApiClient() {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath(BASE_PATH);

        // Configure API key authorization
        ApiKeyAuth apiKeyHeader = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyHeader");
        apiKeyHeader.setApiKey(API_KEY);

        return defaultClient;
    }

    private static NDSData fetchNDSData(ApiClient apiClient) throws ApiException, IOException {
        DefaultApi apiInstance = new DefaultApi(apiClient);

        // Fetch SmartLayer by tile ID
        File layerByTileIdResult = apiInstance.getLayerByTileId(TILE_ID);
        nds.smart.tile.SmartLayerTile tile = zserio.runtime.io.SerializeUtil.deserializeFromFile(
                nds.smart.tile.SmartLayerTile.class, layerByTileIdResult);

        // Deserialize LaneLayer and LaneGeometryLayer
        nds.lane.layer.LaneLayer laneLayer = zserio.runtime.io.SerializeUtil.deserializeFromBytes(
                nds.lane.layer.LaneLayer.class, tile.getLayers()[0].getLayer().getData().getBuffer());

        nds.lane.layer.LaneGeometryLayer laneGeometryLayer = zserio.runtime.io.SerializeUtil.deserializeFromBytes(
                nds.lane.layer.LaneGeometryLayer.class, tile.getLayers()[1].getLayer().getData().getBuffer());

        return new NDSData(laneLayer, laneGeometryLayer);
    }

    private static void generateKMLFile(nds.lane.layer.LaneGeometryLayer laneGeometryLayer) throws IOException {
        Kml kml = new Kml();
        Document doc = kml.createAndSetDocument().withName("NDS.live Island 1").withOpen(true);

        // Generate Center Lines
        generateKMLFolder(doc, "Island 1 Center",
                Arrays.asList(laneGeometryLayer.getCenterLineGeometryLayer().getBuffers().getLines3D()));

        // Generate Boundary Lines
        generateKMLFolder(doc, "Island 1 Boundary",
                Arrays.asList(laneGeometryLayer.getBoundaryGeometryLayer().getBuffers().getLines3D()));

        // Write KML to file
        kml.marshal(new File(OUTPUT_KML_FILE));
    }

    private static void generateKMLFolder(Document doc, String folderName, Iterable<Line3D> lines) {
        Folder folder = doc.createAndAddFolder().withName(folderName).withOpen(true);

        for (Line3D line : lines) {
            Placemark placemark = folder.createAndAddPlacemark();
            LineString lineStr = placemark.createAndSetLineString().withAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND);

            for (Position3D pos : line.getPositions()) {
                NdsCoordinate ndsCoord = new NdsCoordinate(pos.getLongitude(), pos.getLatitude(), pos.getElevation());
                WGS84Coordinate wgsCoord = ndsCoord.toWGS84Coordinate();
                lineStr.addToCoordinates(wgsCoord.lon(), wgsCoord.lat(), wgsCoord.elevation() / 100.0);
            }
        }
    }
}

// Additional Classes

record NDSData(nds.lane.layer.LaneLayer laneLayer, nds.lane.layer.LaneGeometryLayer laneGeometryLayer) {
}
