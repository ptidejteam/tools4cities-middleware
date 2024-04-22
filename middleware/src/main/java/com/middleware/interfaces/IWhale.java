package com.middleware.interfaces;
import java.util.List;

public interface IWhale extends IMammal {
    void swim();
    void addChild(IWhale child);
    List<Object> getChildren();
    IMammal getFirstChild();
    String getSpecies();
}
