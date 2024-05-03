package com.middleware.interfaces.metamenth.structure;

import com.middleware.enums.RoomType;

public interface IRoom extends IAbstractFloorSpace {
    String toString();
    String getRoomType();
    void setRoomType(RoomType roomType);
}
