package com.middleware.interfaces;
import java.util.ArrayList;

public interface IWhale extends IMammal {
    void swim();
    void addChild(IWhale child);
    ArrayList<Object> getChildren();
    Object getFirstChild();
    String getSpecies();
}
