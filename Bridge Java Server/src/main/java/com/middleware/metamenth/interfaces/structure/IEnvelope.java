package com.middleware.metamenth.interfaces.structure;

import java.util.List;

public interface IEnvelope {
    String getUID();
    void addCover(ICover cover);
    ICover getCoverByUID(String uid);
    List<ICover> getCovers();
    String toString();
}
