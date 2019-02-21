package com.example.injuries.utils;

import com.example.injuries.pojos.RotationVector;

import java.util.ArrayList;
import java.util.List;

public class VectorsList extends ArrayList<RotationVector> {

    private int max_elements;

    public VectorsList(int max_elements){
        super();
        this.max_elements = max_elements;
    }
    @Override
    public boolean add(RotationVector rotationVector) {
        if(this.size() == max_elements)
            super.remove(0);
        return super.add(rotationVector);
    }
}
