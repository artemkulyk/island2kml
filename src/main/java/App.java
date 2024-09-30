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

/**
 * The App class serves as the main entry point for the NDS processing application.
 * It fetches data from an API, deserializes it, and generates a KML file with lane geometry.
 */
public class App {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    private static final String BASE_PATH = "https://api.nds.live/island1";
    private static final String API_KEY = "YOUR_API_KEY_HERE";
    private static final String TILE_ID = "545379780";
    private static final String OUTPUT_KML_FILE = "island1.kml";

    /**
     * Main method that initiates the NDS data processing and KML generation.
     *
     * @param args Command line arguments (not used).
     */
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

    /**
     * Initializes the API client with the base path and API key for authentication.
     *
     * @return Initialized ApiClient object.
     */
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

    /**
     * Fetches NDS data from the API for the specified tile ID.
     *
     * @param apiClient The ApiClient instance used to interact with the API.
     * @return NDSData object containing the fetched lane and geometry layers.
     * @throws ApiException If there is an error during the API call.
     * @throws IOException  If there is an error during deserialization of the data.
     */
    private static NDSData fetchNDSData(ApiClient apiClient) throws ApiException, IOException {
        logger.info("Fetching NDS data from API...");
        DefaultApi apiInstance = new DefaultApi(apiClient);

        // Fetch SmartLayer by tile ID
        File layerByTileIdResult = apiInstance.getLayerByTileId(TILE_ID);
        logger.info("SmartLayer data fetched for tile ID: " + TILE_ID);

        // Deserialize the layers
        nds.smart.tile.SmartLayerTile tile = zserio.runtime.io.SerializeUtil.deserializeFromFile(
                nds.smart.tile.SmartLayerTile.class, layerByTileIdResult);

        logger.info("Deserializing LaneLayer and LaneGeometryLayer...");
        nds.lane.layer.LaneLayer laneLayer = zserio.runtime.io.SerializeUtil.deserializeFromBytes(
                nds.lane.layer.LaneLayer.class, tile.getLayers()[0].getLayer().getData().getBuffer());

        nds.lane.layer.LaneGeometryLayer laneGeometryLayer = zserio.runtime.io.SerializeUtil.deserializeFromBytes(
                nds.lane.layer.LaneGeometryLayer.class, tile.getLayers()[1].getLayer().getData().getBuffer());

        logger.info("Deserialization of LaneLayer and LaneGeometryLayer completed.");
        return new NDSData(laneLayer, laneGeometryLayer);
    }

    /**
     * Generates a KML file based on the provided lane geometry data.
     *
     * @param laneGeometryLayer The LaneGeometryLayer containing the 3D lines data.
     * @throws IOException If there is an error during KML file generation.
     */
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

    /**
     * Creates line styles for center and boundary lines in the KML document.
     *
     * @param doc The KML Document object to which the styles will be added.
     */
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

    /**
     * Generates a KML folder with lines based on the provided Line3D data.
     *
     * @param doc        The KML Document object to which the folder will be added.
     * @param folderName The name of the folder in the KML file.
     * @param lines      Iterable collection of Line3D objects representing the lines.
     * @param styleUrl   The style URL to apply to the lines in this folder.
     */
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

/**
 * NDSData is a record that encapsulates the data for the NDS Lane Layer and Lane Geometry Layer.
 * This data is fetched from the NDS API and used for further processing and visualization.
 *
 * @param laneLayer         The LaneLayer object representing lane-related data.
 * @param laneGeometryLayer The LaneGeometryLayer object representing geometry data for lanes.
 */
record NDSData(nds.lane.layer.LaneLayer laneLayer, nds.lane.layer.LaneGeometryLayer laneGeometryLayer) {
}
