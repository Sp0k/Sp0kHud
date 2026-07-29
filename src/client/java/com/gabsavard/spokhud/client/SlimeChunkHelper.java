package com.gabsavard.spokhud.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.Random;

public final class SlimeChunkHelper {
    private static final long SLIME_CHUNK_SALT = 987234911L;

    private static boolean cacheValid;
    private static long cachedWorldSeed;
    private static int cachedChunkX;
    private static int cachedChunkZ;
    private static boolean cachedResult;

    private SlimeChunkHelper() {}

    /**
     * Determines whether the player is currently standing inside a slime chunk.
     *
     * This can automatically obtain the seed in single-player because the client
     * owns the integrated server. A normal multiplayer client does not have the
     * server's world seed, so this returns false on remote servers.
     */
    public static boolean isPlayerInSlimeChunk(Minecraft minecraft) {
        if (minecraft.player == null || minecraft.level == null)
            return false;

        var server = minecraft.getSingleplayerServer();

        // On a remote multiplayer server, the client does not automatically
        // have access to the server's world seed.
        if (server == null)
            return false;

        long worldSeed = server.overworld().getSeed();
        ChunkPos chunkPos = minecraft.player.chunkPosition();

        // Avoid recalculating the result every rendered frame.
        if (!cacheValid
                || cachedWorldSeed != worldSeed
                || cachedChunkX != chunkPos.x()
                || cachedChunkZ != chunkPos.z()) {
            cachedWorldSeed = worldSeed;
            cachedChunkX = chunkPos.x();
            cachedChunkZ = chunkPos.z();
            cachedResult = calculateSlimeChunk(
                    worldSeed,
                    chunkPos.x(),
                    chunkPos.z()
            );

            cacheValid = true;
        }

        return cachedResult;
    }

    /**
     * Reproduces Minecraft Java Edition's slime-chunk calculation.
     */
    static boolean calculateSlimeChunk(long worldSeed, int chunkX, int chunkZ) {
        /*
         * Keep these casts in their current positions.
         *
         * Some of the coordinate multiplications intentionally occur as
         * 32-bit integer operations before being converted to long. Moving
         * the casts inside the expressions can produce incorrect results at
         * large coordinates.
         */
        long slimeRandomSeed = (
                worldSeed
                        + (long) (chunkX * chunkX * 4987142)
                        + (long) (chunkX * 5947611)
                        + (long) (chunkZ * chunkZ) * 4392871L
                        + (long) (chunkZ * 389711)
        ) ^ SLIME_CHUNK_SALT;

        return new Random(slimeRandomSeed).nextInt(10) == 0;
    }
}
