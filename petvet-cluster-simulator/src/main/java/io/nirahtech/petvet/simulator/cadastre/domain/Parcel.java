package io.nirahtech.petvet.simulator.cadastre.domain;

public record Parcel(
    int identifier,
    Land land
) implements Surface, BuiltSurface {

    @Override
    public double calculateArea() {
        return this.land.calculateArea();
    }

    @Override
    public double calculatePerimeter() {
        return this.land.calculatePerimeter();
    }

    @Override
    public double calculateBuiltSurface() {
        return this.land.calculateBuiltSurface();
    }

    @Override
    public double calculateBuiltPerimeter() {
        return this.land.calculateBuiltPerimeter();
    }
    
}
