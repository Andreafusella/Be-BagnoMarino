package com.bagno.marino.service;

import com.bagno.marino.model.allergens.Allergens;
import com.bagno.marino.model.allergens.AllergensDto;
import com.bagno.marino.repository.AllergensRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AllergensService {

    @Autowired
    private AllergensRepository allergensRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<AllergensDto> getAllAllergensAdmin() {
        List<Allergens> allergensList = allergensRepository.findAll();

        List<AllergensDto> allergensDtoList = new ArrayList<>();
        for (Allergens allergens : allergensList) {
            AllergensDto allergensDto = new AllergensDto();
            modelMapper.map(allergens, allergensDto);
            allergensDtoList.add(allergensDto);
        }
        return allergensDtoList;
    }

    public Map<String, String> getAllAllergens() {
        List<Allergens> allergens = allergensRepository.findAll();
        Map<String, String> result = new HashMap<>();

        for (Allergens allergen : allergens) {
            result.put(allergen.getName(), allergen.getSymbol());
        }

        return result;
    }
}
