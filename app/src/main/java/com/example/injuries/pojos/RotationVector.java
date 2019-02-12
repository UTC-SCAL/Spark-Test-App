package com.example.injuries.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class RotationVector implements Parcelable {
    double x;
    double y;
    double z;

    public RotationVector() {
        x = 0;
        y = 0;
        z = 0;
        theta = 0;
    }

    protected RotationVector(Parcel in) {
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
        theta = in.readDouble();
    }

    public static final Creator<RotationVector> CREATOR = new Creator<RotationVector>() {
        @Override
        public RotationVector createFromParcel(Parcel in) {
            return new RotationVector(in);
        }

        @Override
        public RotationVector[] newArray(int size) {
            return new RotationVector[size];
        }
    };

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

    public void update(float position_update_rate, double new_x, double new_y, double new_z, double new_angle) {
        this.x = position_update_rate * this.x + (1 - position_update_rate) * new_x;
        this.y = position_update_rate * this.y + (1 - position_update_rate) * new_y;
        this.z = position_update_rate * this.z + (1 - position_update_rate) * new_z;
        this.theta = position_update_rate * this.theta + (1 - position_update_rate) * new_angle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(x);
        dest.writeDouble(y);
        dest.writeDouble(z);
        dest.writeDouble(theta);
    }
}