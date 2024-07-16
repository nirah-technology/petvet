package io.nirahtech.petvet.simulator.cadastre.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

public final class CadastralPlan implements Surface, BuiltSurface {
    private final Collection<Section> sections;

    public CadastralPlan() {
        this.sections = new HashSet<>();
    }

    public void addSection(final Section section) {
        if (Objects.nonNull(section)) {
            this.sections.add(section);
        }
    }

    public void removeSection(final Section section) {
        if (Objects.nonNull(section) && this.sections.contains(section)) {
            this.sections.remove(section);
        }
    }
    
    public Collection<Section> getSections() {
        return Collections.unmodifiableCollection(this.sections);
    }

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
