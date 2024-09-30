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
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    private static final String BASE_PATH = "https://api.nds.live/island1";
    private static final String API_KEY = "YOUR_API_KEY_HERE";
    private static final String TILE_ID = "545379780";
    private static final String OUTPUT_KML_FILE = "island1.kml";

    public static void main(String[] args) {
        try {
            logger.info("Starting processing...");

            // Initialize API client
            ApiClient apiClient = initializeApiClient();
            logger.info("API client initialized successfully.");

            // Fetch data from API
            NDSData ndsData = fetchNDSData(apiClient);
            logger.info("Data fetched successfully from API.");

            // Generate KML file
            generateKMLFile(ndsData.laneGeometryLayer());
            logger.info("KML file generated successfully.");

            System.out.println("Processing finished successfully.");
        } catch (ApiException | IOException e) {
            logger.log(Level.SEVERE, "An error occurred during processing:", e);
        }
    }

    private static ApiClient initializeApiClient() {
        logger.info("Initializing API client...");
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath(BASE_PATH);

        // Configure API key authorization
        ApiKeyAuth apiKeyHeader = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyHeader");
        apiKeyHeader.setApiKey(API_KEY);
        logger.info("API client initialized with base path and API key.");

        return defaultClient;
    }

    private static NDSData fetchNDSData(ApiClient apiClient) throws ApiException, IOException {
        logger.info("Fetching NDS data from API...");
        DefaultApi apiInstance = new DefaultApi(apiClient);

        // Fetch SmartLayer by tile ID
        File layerByTileIdResult = apiInstance.getLayerByTileId(TILE_ID);
        logger.info("SmartLayer data fetched for tile ID: " + TILE_ID);

        nds.smart.tile.SmartLayerTile tile = zserio.runtime.io.SerializeUtil.deserializeFromFile(
                nds.smart.tile.SmartLayerTile.class, layerByTileIdResult);

        // Deserialize LaneLayer and LaneGeometryLayer
        logger.info("Deserializing LaneLayer and LaneGeometryLayer...");
        nds.lane.layer.LaneLayer laneLayer = zserio.runtime.io.SerializeUtil.deserializeFromBytes(
                nds.lane.layer.LaneLayer.class, tile.getLayers()[0].getLayer().getData().getBuffer());

        nds.lane.layer.LaneGeometryLayer laneGeometryLayer = zserio.runtime.io.SerializeUtil.deserializeFromBytes(
                nds.lane.layer.LaneGeometryLayer.class, tile.getLayers()[1].getLayer().getData().getBuffer());

        logger.info("Deserialization of LaneLayer and LaneGeometryLayer completed.");
        return new NDSData(laneLayer, laneGeometryLayer);
    }

    private static void generateKMLFile(nds.lane.layer.LaneGeometryLayer laneGeometryLayer) throws IOException {
        logger.info("Starting KML file generation...");
        Kml kml = new Kml();
        Document doc = kml.createAndSetDocument().withName("NDS.live Island 1").withOpen(true);

        // Define styles for center and boundary lines
        createLineStyles(doc);
        logger.info("Styles created for center and boundary lines.");

        // Generate Center Lines with yellow and arrows
        generateKMLFolder(doc, "Island 1 Center",
                Arrays.asList(laneGeometryLayer.getCenterLineGeometryLayer().getBuffers().getLines3D()), "centerLineStyle");

        // Generate Boundary Lines with dashed white lines
        generateKMLFolder(doc, "Island 1 Boundary",
                Arrays.asList(laneGeometryLayer.getBoundaryGeometryLayer().getBuffers().getLines3D()), "boundaryLineStyle");

        // Write KML to file
        kml.marshal(new File(OUTPUT_KML_FILE));
        logger.info("KML file written to " + OUTPUT_KML_FILE);
    }

    private static void createLineStyles(Document doc) {
        logger.info("Creating line styles...");

        // Style for center lines (yellow)
        Style centerLineStyle = doc.createAndAddStyle().withId("centerLineStyle");
        centerLineStyle.createAndSetLineStyle()
                .withColor("ff00dfef") // Yellow color (AABBGGRR, reverse order)
                .withWidth(4);
        logger.info("Center line style created.");

        // Style for boundary lines (white)
        Style boundaryLineStyle = doc.createAndAddStyle().withId("boundaryLineStyle");
        boundaryLineStyle.createAndSetLineStyle()
                .withColor("ffffffff") // White color (AABBGGRR)
                .withWidth(2);
        logger.info("Boundary line style created.");
    }

    private static void generateKMLFolder(Document doc, String folderName, Iterable<Line3D> lines, String styleUrl) {
        logger.info("Generating KML folder: " + folderName);
        Folder folder = doc.createAndAddFolder().withName(folderName).withOpen(true);

        for (Line3D line : lines) {
            logger.info("Processing line...");

            Placemark placemark = folder.createAndAddPlacemark();
            placemark.setStyleUrl("#" + styleUrl); // Apply the style to the placemark

            LineString lineStr = placemark.createAndSetLineString()
                    .withAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND)
                    .withExtrude(false) // Extruding to the ground to show in 3D
                    .withTessellate(true); // Tesselation to follow the terrain

            for (Position3D pos : line.getPositions()) {
                NdsCoordinate ndsCoord = new NdsCoordinate(pos.getLongitude(), pos.getLatitude(), pos.getElevation());
                WGS84Coordinate wgsCoord = ndsCoord.toWGS84Coordinate();
                logger.info("Adding position: " + ndsCoord + " -> " + wgsCoord);
                lineStr.addToCoordinates(wgsCoord.lon(), wgsCoord.lat(), wgsCoord.elevation() / 100.0);
            }
        }

        logger.info("Folder " + folderName + " generated.");
    }
}

// Additional Classes

record NDSData(nds.lane.layer.LaneLayer laneLayer, nds.lane.layer.LaneGeometryLayer laneGeometryLayer) {
}
