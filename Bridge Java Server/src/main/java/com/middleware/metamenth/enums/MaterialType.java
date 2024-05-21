package com.middleware.metamenth.enums;

public enum MaterialType {
    EX_WALL_WOOD("ExWall:Wood"),
    EX_WALL_BRICK("ExWall:Brick"),
    EX_WALL_CONCRETE("ExWall:Concrete"),
    EX_WALL_STEEL("ExWall:Steel"),
    EX_WALL_ALUMINIUM("ExWall:Aluminium"),
    EX_WALL_FIBRE_CEMENT("ExWall:FibreCement"),
    ROOF_STEEL("Roof:Steel"),
    ROOF_ALUMINIUM("Roof:Aluminium"),
    ROOF_COPPER("Roof:Copper"),
    ROOF_ZINC("Roof:Zinc"),
    ROOF_TIN("Roof:Tin"),
    ROOF_STAINLESS_STEEL("Roof:StainlessSteel"),
    ROOF_STONE_COATED_STEEL("Roof:StoneCoatedSteel"),
    ROOF_METAL_SHINGLES("Roof:MetalShingles"),
    ROOF_CONCRETE_TILE("Roof:ConcreteTiles"),
    ROOF_GREEN("Roof:Green"),
    ROOF_EPMD("Roof:EPMD"),
    WIN_DOOR_WOOD("WindowDoor:Wood"),
    WIN_DOOR_VINYL("WindowDoor:Vinyl"),
    WIN_DOOR_FIBRE_GLASS("WindowDoor:FibreGlass"),
    WIN_DOOR_ALUMINIUM("WindowDoor:Aluminium"),
    IN_WALL_FIBRE_GLASS("InWall:FibreGlass"),
    IN_WALL_CELLULOSE("InWall:Cellulose"),
    IN_WALL_SPRAY_FOAM("InWall:SprayFoam"),
    IN_WALL_MINERAL_WOOL("InWall:MineralWool"),
    FLOOR_CONCRETE("Floor:Concrete"),
    FLOOR_WOOD("Floor:Wood"),
    FLOOR_CERAMIC_TILE("Floor:CeramicTile"),
    FLOOR_NATURAL_STONE("Floor:NaturalStone"),
    FLOOR_VINYL("Floor:Vinyl"),
    FLOOR_BAMBOO("Floor:Bamboo"),
    FLOOR_CORK("Floor:Cork"),
    FLOOR_RUBBER("Floor:Rubber"),
    FLOOR_LINOLEUM("Floor:Linoleum");

    private final String material;

    MaterialType(String material) {
        this.material = material;
    }

    public String getValue() {
        return material;
    }
}
