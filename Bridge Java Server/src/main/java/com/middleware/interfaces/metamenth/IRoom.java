package com.middleware.interfaces.metamenth;

import com.middleware.enums.RoomType;

public interface IRoom {
    String toString();
    RoomType getRoomType();
    void setRoomType(RoomType roomType);
}
