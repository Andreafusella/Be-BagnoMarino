package com.bagno.marino.service;

import com.bagno.marino.model.allergens.Allergens;
import com.bagno.marino.repository.AllergensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AllergensService {

    @Autowired
    private AllergensRepository allergensRepository;

    public Map<String, String> getAllAllergens() {
        List<Allergens> allergens = allergensRepository.findAll();
        Map<String, String> result = new HashMap<>();

        for (Allergens allergen : allergens) {
            result.put(allergen.getName(), allergen.getSymbol());
        }

        return result;
    }
}
