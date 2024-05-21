package com.middleware.metamenth.interfaces.structure;

public interface ICover {
    String getUID();
    String getCoverType();
    void setCoverType(String coverType);
    void addLayer(ILayer layer);
    ILayer getLayerByUID(String uid);
    String toString();
}
