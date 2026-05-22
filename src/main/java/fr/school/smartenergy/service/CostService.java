package fr.school.smartenergy.service;

import fr.school.smartenergy.model.EnergyType;

public class CostService {

    public double estimateCost(EnergyType type, double quantity) {
        double rate = switch (type) {
            case ELECTRICITE -> 0.25;
            case EAU -> 0.004;
            case GAZ -> 0.12;
            case CHAUFFAGE -> 0.18;
            case CLIMATISATION -> 0.22;
            case SOLAIRE -> 0.0;
        };

        return quantity * rate;
    }
}