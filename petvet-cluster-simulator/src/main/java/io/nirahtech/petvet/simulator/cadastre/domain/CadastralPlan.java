package io.nirahtech.petvet.simulator.cadastre.domain;

import java.util.Collection;

public record CadastralPlan(
    Collection<Section> sections
) implements Surface, BuiltSurface {
    @Override
    public double calculateArea() {
        return sections.stream()
                       .mapToDouble(Section::calculateArea)
                       .reduce(0.0, Double::sum);
    }

    @Override
    public double calculatePerimeter() {
        return sections.stream()
                       .mapToDouble(Section::calculatePerimeter)
                       .reduce(0.0, Double::sum);
    }

    @Override
    public double calculateBuiltSurface() {
        return sections.stream()
                       .mapToDouble(Section::calculateBuiltSurface)
                       .reduce(0.0, Double::sum);
    }

    @Override
    public double calculateBuiltPerimeter() {
        return sections.stream()
                       .mapToDouble(Section::calculateBuiltPerimeter)
                       .reduce(0.0, Double::sum);
    }

    
}
