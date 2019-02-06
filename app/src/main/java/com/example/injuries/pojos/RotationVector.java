package com.example.injuries.pojos;

public class RotationVector {
    double x;
    double y;
    double z;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getTheta() {
        return theta;
    }

    double theta;

    public RotationVector(double x, double y, double z, double theta) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.theta = theta;
    }
}
