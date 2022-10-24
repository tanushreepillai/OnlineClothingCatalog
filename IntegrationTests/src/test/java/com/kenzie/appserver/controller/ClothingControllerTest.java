package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.ClothingCreateRequest;
import com.kenzie.appserver.service.ClothingService;
import com.kenzie.appserver.service.model.Clothing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class ClothingControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ClothingService clothingService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getById_Exists() throws Exception {
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        Double price = 10.0;
        String itemSize = "L";
        Boolean inStock = false;

        Clothing clothing = new Clothing(id, name, inStock, price, itemSize);
        Clothing persistedClothing = clothingService.addNewClothing(clothing);
        // WHEN
        mvc.perform(get("/clothing/{clothingId}", persistedClothing.getClothingId())
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(jsonPath("clothingId")
                        .value(is(id)))
                .andExpect(jsonPath("itemName")
                        .value(is(name)))
                .andExpect(jsonPath("inStock")
                        .value(is(inStock)))
                .andExpect(jsonPath("price")
                        .value(is(price)))
                .andExpect(jsonPath("itemSize")
                        .value(is(itemSize)))
                .andExpect(status().isOk());
    }

    @Test
    public void getClothingt_ClothingDoesNotExist() throws Exception {
        // GIVEN
        String clothingId = UUID.randomUUID().toString();
        // WHEN
        mvc.perform(get("/clothing/{clothingId}", clothingId)
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isNotFound());
    }

    @Test

    public void createClothing_CreateSuccessful() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String itemSize = "L";
        Double price = 90.0;
        Boolean inStock = false;

        ClothingCreateRequest clothingCreateRequest = new ClothingCreateRequest();
        clothingCreateRequest.setClothingId(id);
        clothingCreateRequest.setItemSize(itemSize);
        clothingCreateRequest.setItemName(name);
        clothingCreateRequest.setPrice(price);
        clothingCreateRequest.setInStock(inStock);

        mapper.registerModule(new JavaTimeModule());

        // WHEN
        mvc.perform(post("/clothing")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clothingCreateRequest)))
                // THEN
                    .andExpect(jsonPath("clothingId")
                            .exists())
                    .andExpect(jsonPath("itemName")
                            .value(is(name)))
                    .andExpect(jsonPath("inStock")
                            .value(is(inStock)))
                    .andExpect(jsonPath("price")
                            .value(is(price)))
                    .andExpect(jsonPath("itemSize")
                            .value(is(itemSize)))
                    .andExpect(status().isCreated());
    }

    @Test
    public void updateClothing_PutSuccessful() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        Double price = 10.0;
        String itemSize = "L";
        Boolean inStock = false;

        Clothing clothing = new Clothing(id, name, inStock, price, itemSize);
        Clothing persistedClothing = clothingService.addNewClothing(clothing);

        String newName = mockNeat.strings().valStr();
        Double newPrice = 100.0;

        ClothingCreateRequest clothingCreateRequest = new ClothingCreateRequest();
        clothingCreateRequest.setClothingId(id);
        clothingCreateRequest.setItemName(newName);
        clothingCreateRequest.setInStock(inStock);
        clothingCreateRequest.setPrice(newPrice);
        clothingCreateRequest.setItemSize(itemSize);

        mapper.registerModule(new JavaTimeModule());

        // WHEN
        mvc.perform(put("/clothing")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clothingCreateRequest)))
                // THEN
                .andExpect(jsonPath("clothingId")
                        .exists())
                .andExpect(jsonPath("itemName")
                        .value(is(newName)))
                .andExpect(jsonPath("inStock")
                        .value(is(inStock)))
                .andExpect(jsonPath("price")
                        .value(is(newPrice)))
                .andExpect(jsonPath("itemSize")
                        .value(is(itemSize)))
                .andExpect(status().isOk());
    }
    @Test
    public void deleteClothing_DeleteSuccessful() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        Double price = 10.0;
        String itemSize = "L";
        Boolean inStock = false;

        Clothing clothing = new Clothing(id, name, inStock, price, itemSize);
        Clothing persistedClothing = clothingService.addNewClothing(clothing);

        // WHEN
        mvc.perform(delete("/clothing/{clothingId}", persistedClothing.getClothingId())
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isNoContent());
        assertThat(clothingService.findById(id)).isNull();
    }
    @Test
    public void findAllClothing_Successful() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        Double price = 10.0;
        String itemSize = "L";
        Boolean inStock = false;

        Clothing clothing = new Clothing(id, name, inStock, price, itemSize);
        clothingService.addNewClothing(clothing);

        List<Clothing> clothingList = clothingService.findAll();

        // WHEN
        mvc.perform(get("/clothing/all", clothingList)
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk());
        assertThat(clothingService.findAll()).isNotNull();
    }

}