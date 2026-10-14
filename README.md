# Lumen — Procedural World Explorer

**A Java desktop application for generating, navigating, and illuminating interactive 2D worlds.**

Lumen turns a numeric seed into an explorable tile map of rooms and corridors. Players can navigate with the keyboard, preview a shortest route with the mouse, follow it automatically, and toggle room lighting. Save slots preserve the world seed, player position, and light states between sessions.

Developed as a team project for UC Berkeley's CS 61BL Build Your Own World assignment (Summer 2026).

## Features

- **Seeded world generation:** Reproducible room layouts, connecting corridors, and surrounding walls on an 80 × 40 tile grid.
- **Interactive navigation:** WASD movement with collision checks against walkable tiles.
- **Shortest-path planning:** Breadth-first search over four-directional tile neighbors, with a highlighted route and timed automatic traversal.
- **Room lighting:** Toggle lights and render distance-based illumination across nearby floor tiles.
- **Persistent sessions:** Save and restore the seed, avatar coordinates, and lighting configuration using local text files, including indexed save slots.
- **Contextual interface:** A keyboard-driven start menu and a hover HUD that identifies tiles under the cursor.

## Engineering highlights

### Generation and world modeling

A seeded `Random` instance drives room dimensions, placement, and light locations. Room placement checks occupied regions, a priority queue orders rooms by horizontal position, and L-shaped corridors connect consecutive room centers. A neighboring-floor pass constructs the surrounding walls.

### Graph search and interaction

The pathfinder treats walkable tiles as vertices and generates adjacent neighbors on demand. A queue, visited set, and predecessor map support breadth-first search and route reconstruction. The game loop coordinates keyboard input, mouse input, route previews, and automatic movement at 150 ms intervals.

### State persistence

Save files store a compact representation of a session: the seed, player coordinates, and per-light on/off values. Loading regenerates the map from its seed and restores the saved state instead of storing the full tile grid.

## Technology

Java · Princeton `algs4` / `StdDraw` · Java AWT · Tile-based rendering · Breadth-first search · File I/O

## Run locally

### Requirements

- JDK 17 or newer.
- Princeton's `algs4` library, which provides `StdDraw`, `In`, and `Out`. The original IntelliJ configuration references the CS 61BL `library-su26` directory; these external dependencies are not bundled in this repository.
- A graphical desktop session.

### IntelliJ IDEA

1. Clone this repository and open its root folder in IntelliJ IDEA.
2. Set the project SDK to JDK 17 or newer and mark `src` as the Sources Root if necessary.
3. Add the `algs4` JAR to the module dependencies, or reconnect the original `library-su26` library.
4. Run `core.Main` with the repository root as the working directory so the included save files are available.

### Command line (macOS / Linux)

With the `algs4` JAR available locally, replace `/path/to/algs4.jar` below with its actual location:

```sh
mkdir -p build
javac -cp /path/to/algs4.jar -d build $(find src -name '*.java')
java -cp build:/path/to/algs4.jar core.Main
```

## Controls

| Context | Input | Action |
| --- | --- | --- |
| Main menu | `N` | Start a new world; enter a positive numeric seed, then `S` |
| Main menu | `L` | Load the default save (`save0.txt`) |
| Main menu | `K` | Enter a saved slot number, then `S` or Enter |
| Main menu | `Q` | Quit |
| In world | `W` / `A` / `S` / `D` | Move up / left / down / right |
| In world | Click a reachable floor tile | Preview a route from the avatar |
| In world | Click again | Begin following the previewed route |
| In world | WASD while following a route | Cancel automatic movement and move manually |
| In world | `L` | Toggle the light associated with the current room |
| In world | `:Q` | Save to the default slot and quit |
| In world | `:K`, then one digit | Save to that slot and quit |
| In world | Hover over a tile | Show its description in the HUD |

For a first session, try `N`, enter `12345`, then press `S`.

## Project structure

```text
src/
├── core/
│   ├── Main.java          # Menus, input loop, movement, and save/load
│   ├── World.java         # Map generation, tile state, and lighting
│   ├── Room.java          # Room placement and bounds
│   ├── Pathfinder.java    # BFS route search and reconstruction
│   └── Light.java         # Light position and on/off state
├── tileengine/            # Tile definitions and rendering
├── utils/                 # Randomization and file helpers
└── demo/                  # Rendering, input, and persistence examples
```

The repository also preserves the original IntelliJ configuration, compiled output, and sample save files from the project folder.

## Project context

Lumen is an academic team project combining procedural generation, graph algorithms, desktop interaction, and persistent application state. The repository includes course-provided support code and demos alongside project implementation. The Lumen name describes this portfolio presentation; the original in-app menu still uses the course's BYOW title.
