# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Development Commands

### Prerequisites
- Java 25 JDK installed and configured
- Gradle wrapper included in the project

### Common Commands
```bash
# Make gradlew executable (first time only)
chmod +x ./gradlew

# Build the mod
./gradlew build

# Run tests
./gradlew test

# Clean build artifacts
./gradlew clean

# Build specific task
./gradlew build check --no-daemon

# Run in development environment
./gradlew runClient
```

### GitHub Actions Integration
The project includes a CI workflow that:
- Runs on push to main/master branches and pull requests
- Uses Ubuntu runner with JDK 25
- Executes `./gradlew build check --no-daemon`
- Uploads build artifacts

## Code Architecture Overview

### Project Structure
```
src/main/java/com/miniradar/
├── MiniRadar.java          # Main mod class - NeoForge mod entry point
├── RadarManager.java     # Core radar logic - entity detection and positioning
├── RadarRenderer.java    # Rendering logic - GUI overlay for radar display
└── ConfigManager.java    # Configuration management - JSON-based settings

resources/
└── META-INF/neoforge.mods.toml  # Mod metadata and dependencies
```

### Key Components

#### MiniRadar (Main Class)
- **Purpose**: NeoForge mod entry point
- **Key Features**:
  - Registers event listeners for client setup and GUI layer registration
  - Manages singleton instances of core components
  - Uses `@Mod` annotation with Dist.CLIENT for client-side only functionality

#### RadarManager
- **Purpose**: Handles entity detection and coordinate transformation
- **Core Methods**:
  - `update()`: Scans entities within detection radius (configurable, default 64 blocks)
  - `getEntitiesInRadarRange()`: Filters entities based on player orientation and position
  - `rotateCoordinates()`: Transforms world coordinates to relative radar coordinates
- **Update Frequency**: 50ms intervals to prevent performance issues

#### RadarRenderer
- **Purpose**: Renders radar overlay on screen
- **Rendering Details**:
  - 100x100 pixel radar display positioned at top-left of screen
  - Player position shown as white square at center
  - Entities displayed as colored dots:
    - Players: Cyan (0xFF00FFFF)
    - Items: Yellow (0xFFFFFF00)
    - Hostile mobs: Red (0xFFFF0000)
    - Other entities: Green (0xFF00FF00)
  - Uses NeoForge 26.1+ `GuiGraphicsExtractor` API for rendering

#### ConfigManager
- **Purpose**: Manages mod configuration via JSON file
- **Configuration Options**:
  - `detectionRadius`: Entity detection range (16-128 blocks, default 64)
  - Configuration saved to `~/.minecraft/config/miniradar.json`
  - Automatic validation and bounds checking

### Dependencies
- **NeoForge 26.1.2.43-beta**: Minecraft modding framework
- **Minecraft 26.1**: Target game version
- **Gson**: JSON serialization/deserialization for configuration

### NeoForge 26.1 Migration Notes
This mod has been updated for NeoForge 26.1 compatibility:
- Replaced deprecated `GuiGraphics` with new `GuiGraphicsExtractor` API
- Updated event system usage
- Modified entity type checking to work with updated Minecraft 26.1 APIs
- Maintained backward compatibility where possible

### Event System
- Uses NeoForge event bus for client-side events
- Registers handlers for:
  - `FMLClientSetupEvent`: Client initialization
  - `RegisterGuiLayersEvent`: GUI layer registration

### Configuration File Location
- Default path: `~/.minecraft/config/miniradar.json`
- Contains: `{ "detectionRadius": 64 }`

### Performance Considerations
- Entity scanning occurs every 50ms to balance responsiveness and performance
- Coordinate transformations use efficient trigonometric calculations
- Renderer only processes entities visible within radar bounds
- Null checks throughout to prevent crashes

## Development Guidelines

### Adding New Features
1. Follow existing code patterns and naming conventions
2. Add configuration options through ConfigManager if user-facing
3. Use proper NeoForge event system for game integration
4. Include comprehensive error handling
5. Test with various entity types and edge cases

### Debugging Tips
- Check `~/.minecraft/logs/latest.log` for mod-specific errors
- Use `radarManager.update()` calls to verify entity detection
- Monitor FPS impact when adding complex rendering effects
- Verify coordinate transformations with debug overlays if needed

### Testing Strategy
- Test with different detection radius values
- Verify rendering with multiple entity types simultaneously
- Check performance with large numbers of entities
- Validate configuration persistence across game sessions