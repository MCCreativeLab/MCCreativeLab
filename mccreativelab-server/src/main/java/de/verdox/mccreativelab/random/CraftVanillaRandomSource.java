package de.verdox.mccreativelab.random;

import net.minecraft.util.RandomSource;

public class CraftVanillaRandomSource implements VanillaRandomSource {
    private final RandomSource randomSource;

    public CraftVanillaRandomSource (RandomSource randomSource){
        this.randomSource = randomSource;
    }
    @Override
    public void setSeed(long seed) {
        randomSource.setSeed(seed);
    }

    @Override
    public int nextInt() {
        return randomSource.nextInt();
    }

    @Override
    public int nextInt(int bound) {
        return randomSource.nextInt(bound);
    }

    @Override
    public int nextIntBetweenInclusive(int min, int max) {
        return randomSource.nextIntBetweenInclusive(min, max);
    }

    @Override
    public long nextLong() {
        return randomSource.nextLong();
    }

    @Override
    public boolean nextBoolean() {
        return randomSource.nextBoolean();
    }

    @Override
    public float nextFloat() {
        return randomSource.nextFloat();
    }

    @Override
    public double nextDouble() {
        return randomSource.nextDouble();
    }

    @Override
    public double nextGaussian() {
        return randomSource.nextGaussian();
    }

    @Override
    public double triangle(double mode, double deviation) {
        return randomSource.triangle(mode, deviation);
    }

    @Override
    public void consumeCount(int count) {
        randomSource.consumeCount(count);
    }

    @Override
    public int nextInt(int min, int max) {
        return randomSource.nextInt(min, max);
    }
}
