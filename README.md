#Island 2 KML

<img width="1440" alt="image" src="https://github.com/user-attachments/assets/d63d358a-a2f3-4615-8b4b-9aa7b2f718b0">


## Project Description

**Island2KML** is a Java application that interacts with the NDS.live API to retrieve road infrastructure data for an island and converts this data into KML format for visualization in applications like Google Earth.

The application performs the following functions:

- Connects to the NDS.live API using a provided API key.
- Retrieves SmartLayer data by tile ID.
- Deserializes the obtained data into `LaneLayer` and `LaneGeometryLayer` objects.
- Processes lane groups and outputs information about them.
- Generates a KML file containing the center lines and boundary lines of roads for visualization.

## Requirements

- Java Development Kit (JDK) 8 or higher.
- Maven for dependency management and project build.
- Internet access to connect to the NDS.live API.

## Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yourusername/ndsisland12kml.git
   ```

2. **Navigate to the Project Directory**

   ```bash
   cd island2kml
   ```

3. **Set Up Dependencies**

   The project uses Maven for dependency management. Ensure Maven is installed and properly configured.

## Configuration

Before running the application, you need to configure some parameters:

1. **API Key**

   Update the `API_KEY` value in the `App` class to your valid API key:

   ```java
   private static final String API_KEY = "YOUR_API_KEY_HERE";
   ```

2. **API Base Path**

   If you are using a different API base path, update `BASE_PATH`:

   ```java
   private static final String BASE_PATH = "https://api.nds.live/island1";
   ```

3. **Tile ID**

   Change `TILE_ID` to the desired tile ID if necessary:

   ```java
   private static final String TILE_ID = "545379780";
   ```

4. **Output File Name**

   By default, the KML file will be saved as `island1.kml`. You can change this by updating `OUTPUT_KML_FILE`:

   ```java
   private static final String OUTPUT_KML_FILE = "island1.kml";
   ```

## Build and Run

1. **Build the Project**

   In the project's root directory, execute:

   ```bash
   mvn clean install
   ```

   This will compile the project and install all necessary dependencies.

2. **Run the Application**

   Execute the following command to run the application:

   ```bash
   mvn exec:java -Dexec.mainClass="App"
   ```

   Upon successful execution, you will see the message:

   ```
   Processing finished successfully.
   ```

   And the file `island1.kml` will appear in the project directory.

## Usage

Open the generated `island1.kml` file in a KML-compatible application like Google Earth to visualize the island's road lines.

## Project Structure

- `App.java` - The main application class containing the `main` entry point and methods for performing core functions.
- `NDSData.java` - A helper class for storing `LaneLayer` and `LaneGeometryLayer` objects.
- `pom.xml` - The Maven configuration file containing project information and dependencies.

## Dependencies

The project uses the following main dependencies:

- **OpenAPI Client** - For interacting with the NDS.live API.
- **Zserio Runtime** - For deserializing binary data from the API.
- **JAK (Java API for KML)** - For creating and manipulating KML files.

A complete list of dependencies is available in the `pom.xml` file.
