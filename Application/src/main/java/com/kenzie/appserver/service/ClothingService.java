package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.repositories.ClothingRepository;
import com.kenzie.appserver.repositories.model.ClothingRecord;
import com.kenzie.appserver.service.model.Clothing;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClothingService {
    private ClothingRepository clothingRepository;
    private CacheStore cache;

    public ClothingService(ClothingRepository clothingRepository, CacheStore cache) {

        this.clothingRepository = clothingRepository;
        this.cache = cache;
    }

    public Clothing findById(String clothingId) {
        Clothing cachedClothing = cache.get(clothingId);
        if (cachedClothing != null) {
            return cachedClothing;
        }
        Clothing clothingFromBackendService = clothingRepository
                .findById(clothingId)
                .map(clothing -> new Clothing(clothing.getClothingId(),
                        clothing.getItemName(),
                        clothing.getInStock(),
                        clothing.getPrice(),
                        clothing.getItemSize()))
                .orElse(null);

        if (clothingFromBackendService != null) {
            cache.add(clothingFromBackendService.getClothingId(), clothingFromBackendService);
        }
        return clothingFromBackendService;
    }

    public List<Clothing> findAll() {
        List<Clothing> allClothing = new ArrayList<>();
        clothingRepository
                .findAll()
                .forEach(clothing -> allClothing.add(new Clothing(clothing.getClothingId(),
                        clothing.getItemName(),
                        clothing.getInStock(),
                        clothing.getPrice(),
                        clothing.getItemSize())));
        return allClothing;
    }

    public Clothing addNewClothing(Clothing clothing) {
        ClothingRecord clothingRecord = new ClothingRecord();
        clothingRecord.setClothingId(clothing.getClothingId());
        clothingRecord.setItemName(clothing.getItemName());
        clothingRecord.setInStock(clothing.getInStock());
        clothingRecord.setPrice(clothing.getPrice());
        clothingRecord.setItemSize(clothing.getItemSize());
        clothingRepository.save(clothingRecord);
        return clothing;
    }

    public void updateClothing(Clothing clothing) {
        if (clothingRepository.existsById(clothing.getClothingId())) {
            ClothingRecord clothingRecord = new ClothingRecord();
            clothingRecord.setClothingId(clothing.getClothingId());
            clothingRecord.setInStock(clothing.getInStock());
            clothingRecord.setPrice(clothing.getPrice());
            clothingRecord.setItemSize(clothing.getItemSize());
            clothingRecord.setItemName(clothing.getItemName());
            clothingRepository.save(clothingRecord);
            cache.evict(clothing.getClothingId());
        }
    }

    public void deleteClothing(String clothingId) {
        ClothingRecord clothingRecord = new ClothingRecord();
        clothingRecord.setClothingId(clothingId);
        cache.evict(clothingId);
        clothingRepository.delete(clothingRecord);

    }

    public List<Clothing> findSpecficClothings(String itemName) {
        List<Clothing> clothingList = new ArrayList<>();

        Iterable<ClothingRecord> clothingIterator = clothingRepository.findAll();
        for (ClothingRecord record : clothingIterator) {
            if (record.getItemName().toLowerCase().contains(itemName.toLowerCase())) {
                clothingList.add(new Clothing(record.getClothingId(),
                        record.getItemName(),
                        record.getInStock(),
                        record.getPrice(),
                        record.getItemSize()));
            }
        }
        return clothingList;
    }
}


