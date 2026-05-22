package fr.school.smartenergy.service;

import com.google.gson.reflect.TypeToken;
import fr.school.smartenergy.model.EnergyType;
import fr.school.smartenergy.util.JsonUtils;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map;

/**
 * Loads energy tariffs from tariffs.json and estimates consumption costs.
 */
public class CostService {

    private static final String TARIFFS_RESOURCE = "/config/tariffs.json";

    // Default tariffs used as fallback
    private static final Map<EnergyType, Double> DEFAULT_TARIFFS;
    static {
        DEFAULT_TARIFFS = new EnumMap<>(EnergyType.class);
        DEFAULT_TARIFFS.put(EnergyType.ELECTRICITY, 0.25);
        DEFAULT_TARIFFS.put(EnergyType.WATER, 0.004);
        DEFAULT_TARIFFS.put(EnergyType.GAS, 0.12);
        DEFAULT_TARIFFS.put(EnergyType.HEATING, 0.18);
        DEFAULT_TARIFFS.put(EnergyType.AIR_CONDITIONING, 0.22);
        DEFAULT_TARIFFS.put(EnergyType.SOLAR_PRODUCTION, -0.10);
    }

    private final Map<EnergyType, Double> tariffs;

    public CostService() {
        this.tariffs = loadTariffs();
    }

    private Map<EnergyType, Double> loadTariffs() {
        Type type = new TypeToken<Map<String, Double>>(){}.getType();
        Map<String, Double> raw = JsonUtils.loadResource(TARIFFS_RESOURCE, type);
        if (raw == null || raw.isEmpty()) {
            return new EnumMap<>(DEFAULT_TARIFFS);
        }
        Map<EnergyType, Double> result = new EnumMap<>(EnergyType.class);
        for (Map.Entry<String, Double> entry : raw.entrySet()) {
            try {
                result.put(EnergyType.valueOf(entry.getKey()), entry.getValue());
            } catch (IllegalArgumentException ignored) {
                // Unknown key in tariffs.json, skip
            }
        }
        // Fill missing values with defaults
        for (EnergyType et : EnergyType.values()) {
            result.putIfAbsent(et, DEFAULT_TARIFFS.getOrDefault(et, 0.0));
        }
        return result;
    }

    /**
     * Estimates the cost for a given energy type and quantity.
     * @param energyType the energy type
     * @param quantity   the consumed quantity (in the type's unit)
     * @return estimated cost in EUR
     */
    public double estimateCost(EnergyType energyType, double quantity) {
        double tariff = tariffs.getOrDefault(energyType, 0.0);
        return tariff * quantity;
    }

    public Map<EnergyType, Double> getTariffs() {
        return tariffs;
    }

    public double getTariff(EnergyType energyType) {
        return tariffs.getOrDefault(energyType, 0.0);
    }
}

