# Sp0k HUD+

**Sp0k HUD+** is a lightweight client-side Fabric mod that adds useful player information to the Minecraft HUD without opening the debug screen.

It is designed to keep important gameplay information visible in a clean, minimal way.

![View of the HUD](assets/screenshots/hud_view.png)

## Features

- Displays the player’s current block position
- Displays the biome the player is standing in
- Formats biome names into readable text
    - Example: `old_growth_birch_forest` → `Old Growth Birch Forest`
- Shows the player’s cardinal direction at the top-center of the screen
- Displays equipped armor icons on the top-right of the screen
- Shows armor durability as a percentage
- Uses color-coded durability text:
    - Green for high durability
    - Yellow for medium durability
    - Orange/red for low durability
- Hides the custom HUD when the vanilla F3 debug screen is open
- Moves the armor HUD down when active status effects are visible to avoid overlap

## Requirements

- Minecraft `26.1.2`
- Fabric Loader
- Fabric API
- Java `25`

## Installation

1. Install Fabric Loader for Minecraft `26.1.2`.
2. Install the matching Fabric API version.
3. Download the latest `.jar` release of **Sp0k HUD+**.
4. Place the `.jar` file in your Minecraft `mods` folder.
5. Launch Minecraft with the Fabric profile.

## Development Setup

Clone the repository:

```bash
git clone https://github.com/YOUR_USERNAME/YOUR_REPOSITORY_NAME.git
cd YOUR_REPOSITORY_NAME
```

## License
Distributed under the Creative Common license. See [LICENSE](LICENSE) for more information.