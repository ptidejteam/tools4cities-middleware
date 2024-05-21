package com.middleware.metamenth.interfaces.structure;

public interface IRoom extends IAbstractFloorSpace {
    String toString();
    String getRoomType();
    void setRoomType(String roomType);
}
