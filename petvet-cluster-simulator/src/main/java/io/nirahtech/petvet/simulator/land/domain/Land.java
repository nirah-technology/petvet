package io.nirahtech.petvet.simulator.land.domain;

public record Land(
    Segment[] points,
    Building... buildings
) {
    
}
