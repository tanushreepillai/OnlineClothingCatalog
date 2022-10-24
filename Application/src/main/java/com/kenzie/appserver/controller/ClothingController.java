package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.ClothingCreateRequest;
import com.kenzie.appserver.controller.model.ClothingCreateResponse;
import com.kenzie.appserver.service.ClothingService;

import com.kenzie.appserver.service.model.Clothing;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clothing")
public class ClothingController {

    private ClothingService clothingService;

    ClothingController(ClothingService clothingService) {
        this.clothingService = clothingService;
    }

    @GetMapping("/{clothingId}")
    public ResponseEntity<ClothingCreateResponse> get(@PathVariable("clothingId") String clothingId) {
        Clothing clothing = clothingService.findById(clothingId);
        if (clothing == null) {
            return ResponseEntity.notFound().build();
        }
        ClothingCreateResponse clothingResponse = createClothingResponse(clothing);
        return ResponseEntity.ok(clothingResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClothingCreateResponse>> getAllClothing() {

        List<Clothing> allClothing = clothingService.findAll();

        List<ClothingCreateResponse> responses = allClothing.stream().map(clothing ->
                createClothingResponse(clothing)).collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ClothingCreateResponse> addClothing(
            @RequestBody ClothingCreateRequest clothingCreateRequest) {
        Clothing clothing = new Clothing(clothingCreateRequest.getClothingId(),
                clothingCreateRequest.getItemName(),
                clothingCreateRequest.getInStock(),
                clothingCreateRequest.getPrice(),
                clothingCreateRequest.getItemSize());
        clothingService.addNewClothing(clothing);

        ClothingCreateResponse clothingResponse = createClothingResponse(clothing);

        return ResponseEntity.created(URI.create("/clothing/" + clothingResponse.getClothingId()))
                .body(clothingResponse);
    }

    @PutMapping
    public ResponseEntity<ClothingCreateResponse> updateClothing(
            @RequestBody ClothingCreateRequest clothingUpdateRequest) {
        Clothing clothing = new Clothing(clothingUpdateRequest.getClothingId(),
                clothingUpdateRequest.getItemName(),
                clothingUpdateRequest.getInStock(),
                clothingUpdateRequest.getPrice(),
                clothingUpdateRequest.getItemSize());
        clothingService.updateClothing(clothing);

        ClothingCreateResponse clothingResponse = createClothingResponse(clothing);

        return ResponseEntity.ok(clothingResponse);
    }

    @DeleteMapping("/{clothingId}")
    public ResponseEntity deleteClothingByClothingId(@PathVariable("clothingId") String clothingId) {
        clothingService.deleteClothing(clothingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/itemName/{itemName}")
    public ResponseEntity<List<ClothingCreateResponse>> getAllClothingSpecificType(@PathVariable("itemName") String itemName) {
        List<Clothing> clothings = clothingService.findSpecficClothings(itemName);
        if (clothings == null ||  clothings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<ClothingCreateResponse> response = new ArrayList<>();
        for (Clothing clothing : clothings) {
            response.add(this.createClothingResponse(clothing));
        }

        return ResponseEntity.ok(response);
    }

    private ClothingCreateResponse createClothingResponse(Clothing clothing) {
        ClothingCreateResponse clothingResponse = new ClothingCreateResponse();
        clothingResponse.setClothingId(clothing.getClothingId());
        clothingResponse.setInStock(clothing.getInStock());
        clothingResponse.setItemName(clothing.getItemName());
        clothingResponse.setItemSize(clothing.getItemSize());
        clothingResponse.setPrice(clothing.getPrice());
        return clothingResponse;
    }


}
