package com.makotomiyamoto.combat.data;

import com.makotomiyamoto.combat.CombatSystem;
import org.bukkit.metadata.MetadataValue;

public final class ArrowMeta<T> implements MetadataValue {

    private CombatSystem system;
    private T meta;

    public ArrowMeta(CombatSystem system, T meta) {
        this.system = system;
        this.meta = meta;
    }

    @Override
    public T value() {
        return meta;
    }

    @Override
    public int asInt() {
        return 0;
    }

    @Override
    public float asFloat() {
        return 0;
    }

    @Override
    public double asDouble() {
        return 0;
    }

    @Override
    public long asLong() {
        return 0;
    }

    @Override
    public short asShort() {
        return 0;
    }

    @Override
    public byte asByte() {
        return 0;
    }

    @Override
    public boolean asBoolean() {
        return false;
    }

    @Override
    public String asString() {
        return null;
    }

    @Override
    public CombatSystem getOwningPlugin() {
        return system;
    }

    @Override
    public void invalidate() {

    }
}
