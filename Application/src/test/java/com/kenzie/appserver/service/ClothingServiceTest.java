package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.repositories.ClothingRepository;
import com.kenzie.appserver.repositories.model.ClothingRecord;
import com.kenzie.appserver.service.model.Clothing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClothingServiceTest {
    private ClothingRepository clothingRepository;
    private CacheStore cacheStore;
    private ClothingService clothingService;

    @BeforeEach
    void setup() {
        clothingRepository = mock(ClothingRepository.class);
        cacheStore = mock(CacheStore.class);
        clothingService = new ClothingService(clothingRepository,cacheStore);
    }
    /** ------------------------------------------------------------------------
     *  exampleService.findById
     *  ------------------------------------------------------------------------ **/

    @Test
    void findByClothingId() {
        // GIVEN
        String clothingId = randomUUID().toString();

        ClothingRecord record = new ClothingRecord();
        record.setClothingId(clothingId);
        record.setItemName("shirt");
        record.setPrice(10.00);
        record.setInStock(true);
        record.setItemSize("M");
        when(clothingRepository.findById(clothingId)).thenReturn(Optional.of(record));
        // WHEN
        Clothing clothing = clothingService.findById(clothingId);

        // THEN
        Assertions.assertNotNull(clothing, "The clothing is returned");
        Assertions.assertEquals(record.getClothingId(), clothing.getClothingId(), "The clothing id matches");
        Assertions.assertEquals(record.getItemName(), clothing.getItemName(), "The clothing name matches");
        Assertions.assertEquals(record.getInStock(), clothing.getInStock(), "In Stock matches");
        Assertions.assertEquals(record.getPrice(), clothing.getPrice(), "The clothing price matches");
        Assertions.assertEquals(record.getItemSize(), clothing.getItemSize(), "The clothing size matches");
    }

    @Test
    void findByClothingId_invalid() {
        // GIVEN
        String clothingId = randomUUID().toString();
        when(clothingRepository.findById(clothingId)).thenReturn(Optional.empty());

        // WHEN
        Clothing clothing = clothingService.findById(clothingId);

        // THEN
        Assertions.assertNull(clothing, "Clothing is null when not found");
    }

    //Add new clothing
    @Test
    void addNewClothing() {
        // GIVEN
        String clothingId = randomUUID().toString();
        Clothing clothing = new Clothing(clothingId, "shirt", true, 10.0, "S");
        ArgumentCaptor<ClothingRecord> clothingRecordCaptor = ArgumentCaptor.forClass(ClothingRecord.class);

        // WHEN
        Clothing returnedClothing = clothingService.addNewClothing(clothing);

        // THEN
        Assertions.assertNotNull(returnedClothing);
        verify(clothingRepository).save(clothingRecordCaptor.capture());
        ClothingRecord record = clothingRecordCaptor.getValue();

        Assertions.assertNotNull(record, "The clothing record is returned");
        Assertions.assertEquals(record.getClothingId(), clothing.getClothingId(), "The clothing id matches");
        Assertions.assertEquals(record.getItemName(), clothing.getItemName(), "The clothing name matches");
        Assertions.assertEquals(record.getItemSize(), clothing.getItemSize(), "The item size matches");
        Assertions.assertEquals(record.getPrice(), clothing.getPrice(), "The clothing price matches");
        Assertions.assertEquals(record.getInStock(), clothing.getInStock(), "The in stock flag matches");
    }

 //update clothing
    @Test
    void updateClothing() {
         // GIVEN
         String clothingId = randomUUID().toString();
         Clothing clothing = new Clothing(clothingId, "skirt", true, 15.0, "M");
         ArgumentCaptor<ClothingRecord> clothingRecordCaptor = ArgumentCaptor.forClass(ClothingRecord.class);

         // WHEN
         when(clothingRepository.existsById(clothingId)).thenReturn(true);
         clothingService.updateClothing(clothing);

         // THEN
         verify(clothingRepository).save(clothingRecordCaptor.capture());
         ClothingRecord record = clothingRecordCaptor.getValue();

         Assertions.assertNotNull(record, "The concert record is returned");
         Assertions.assertEquals(record.getClothingId(), clothing.getClothingId(), "The clothing id matches");
         Assertions.assertEquals(record.getItemName(), clothing.getItemName(), "The clothing name matches");
         Assertions.assertEquals(record.getInStock(), clothing.getInStock(), "The in stock flag matches");
         Assertions.assertEquals(record.getPrice(), clothing.getPrice(), "The clothing price matches");
         Assertions.assertEquals(record.getItemSize(), clothing.getItemSize(), "The clothing size matches");
     }
    @Test
    void deleteClothing() {
        String clothingId = randomUUID().toString();
        Clothing clothing = new Clothing(clothingId, "shirt", true, 10.0, "L");
        ArgumentCaptor<ClothingRecord> clothingRecordCaptor = ArgumentCaptor.forClass(ClothingRecord.class);
        clothingService.addNewClothing(clothing);

        // WHEN
        clothingService.deleteClothing(clothing.getClothingId());

        // THEN
        Clothing cacheClothing = cacheStore.get(clothing.getClothingId());
        verify(clothingRepository).delete(clothingRecordCaptor.capture());
        Assertions.assertNull(cacheClothing);
    }
    @Test
    void findAllClothing_multiple_clothing() {
        // GIVEN
        ClothingRecord record1 = new ClothingRecord();
        record1.setClothingId(randomUUID().toString());
        record1.setItemName("shirt");
        record1.setInStock(true);
        record1.setPrice(10.0);
        record1.setItemSize("M");

        ClothingRecord record2 = new ClothingRecord();
        record2.setClothingId(randomUUID().toString());
        record2.setItemName("shirt");
        record2.setInStock(true);
        record2.setPrice(20.0);
        record2.setItemSize("L");

        ClothingRecord record3 = new ClothingRecord();
        record3.setClothingId(randomUUID().toString());
        record3.setItemName("pant");
        record3.setInStock(true);
        record3.setPrice(10.0);
        record3.setItemSize("M");

        List<ClothingRecord> recordList = new ArrayList<>();
        recordList.add(record1);
        recordList.add(record2);
        recordList.add(record3);
        when(clothingRepository.findAll()).thenReturn(recordList);

        // WHEN
        List<Clothing> specificClothingList = clothingService.findSpecficClothings("shirt");

        // THEN
        Assertions.assertNotNull(specificClothingList, "The clothing list is returned");
        Assertions.assertEquals(2, specificClothingList.size(), "There are two clothes with shirt name");

        for (Clothing clothing : specificClothingList) {
            if (clothing.getClothingId() == record1.getClothingId()) {
                Assertions.assertEquals(record1.getClothingId(), clothing.getClothingId(), "The clothing id matches");
                Assertions.assertEquals(record1.getItemName(), clothing.getItemName(), "The clothing name matches");
                Assertions.assertEquals(record1.getInStock(), clothing.getInStock(), "The in stock flag matches");
                Assertions.assertEquals(record1.getPrice(), clothing.getPrice(), "The clothing price matches");
                Assertions.assertEquals(record1.getItemSize(), clothing.getItemSize(), "The clothing size matches");
            } else if (clothing.getClothingId() == record2.getClothingId()) {
                    Assertions.assertEquals(record2.getClothingId(), clothing.getClothingId(), "The clothing id matches");
                    Assertions.assertEquals(record2.getItemName(), clothing.getItemName(), "The clothing name matches");
                    Assertions.assertEquals(record2.getInStock(), clothing.getInStock(), "The in stock flag matches");
                    Assertions.assertEquals(record2.getPrice(), clothing.getPrice(), "The clothing price matches");
                    Assertions.assertEquals(record2.getItemSize(), clothing.getItemSize(), "The clothing size matches");
            } else {
                Assertions.assertTrue(false, "Clothing returned that was not in the records!");
            }
        }
    }
    @Test
    void findAllClothing() {
        // GIVEN
        ClothingRecord record1 = new ClothingRecord();
        record1.setClothingId(randomUUID().toString());
        record1.setItemName("shirt");
        record1.setInStock(true);
        record1.setPrice(10.0);
        record1.setItemSize("M");

        ClothingRecord record2 = new ClothingRecord();
        record2.setClothingId(randomUUID().toString());
        record2.setItemName("shirt");
        record2.setInStock(true);
        record2.setPrice(20.0);
        record2.setItemSize("L");

        ClothingRecord record3 = new ClothingRecord();
        record3.setClothingId(randomUUID().toString());
        record3.setItemName("pant");
        record3.setInStock(true);
        record3.setPrice(10.0);
        record3.setItemSize("M");

        List<ClothingRecord> recordList = new ArrayList<>();
        recordList.add(record1);
        recordList.add(record2);
        recordList.add(record3);
        when(clothingRepository.findAll()).thenReturn(recordList);

        // WHEN
        List<Clothing> allClothingList = clothingService.findAll();

        // THEN
        Assertions.assertNotNull(allClothingList, "The clothing list is returned");
        Assertions.assertEquals(3, allClothingList.size(), "All clothing must be returned");

        for (Clothing clothing : allClothingList) {
            if (clothing.getClothingId() == record1.getClothingId()) {
                Assertions.assertEquals(record1.getClothingId(), clothing.getClothingId(), "The clothing id matches");
                Assertions.assertEquals(record1.getItemName(), clothing.getItemName(), "The clothing name matches");
                Assertions.assertEquals(record1.getInStock(), clothing.getInStock(), "The in stock flag matches");
                Assertions.assertEquals(record1.getPrice(), clothing.getPrice(), "The clothing price matches");
                Assertions.assertEquals(record1.getItemSize(), clothing.getItemSize(), "The clothing size matches");
            } else if (clothing.getClothingId() == record2.getClothingId()) {
                Assertions.assertEquals(record2.getClothingId(), clothing.getClothingId(), "The clothing id matches");
                Assertions.assertEquals(record2.getItemName(), clothing.getItemName(), "The clothing name matches");
                Assertions.assertEquals(record2.getInStock(), clothing.getInStock(), "The in stock flag matches");
                Assertions.assertEquals(record2.getPrice(), clothing.getPrice(), "The clothing price matches");
                Assertions.assertEquals(record2.getItemSize(), clothing.getItemSize(), "The clothing size matches");
            } else if (clothing.getClothingId() == record3.getClothingId()) {
                Assertions.assertEquals(record3.getClothingId(), clothing.getClothingId(), "The clothing id matches");
                Assertions.assertEquals(record3.getItemName(), clothing.getItemName(), "The clothing name matches");
                Assertions.assertEquals(record3.getInStock(), clothing.getInStock(), "The in stock flag matches");
                Assertions.assertEquals(record3.getPrice(), clothing.getPrice(), "The clothing price matches");
                Assertions.assertEquals(record3.getItemSize(), clothing.getItemSize(), "The clothing size matches");
            }
        }
    }
}


