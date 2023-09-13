package com.mdh.devtable.menu.persentation;

import com.mdh.devtable.global.ApiResponse;
import com.mdh.devtable.menu.application.MenuService;
import com.mdh.devtable.menu.persentation.dto.MenuCategoryCreateRequest;
import com.mdh.devtable.menu.persentation.dto.MenuCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/api/owner/v1/categories/{categoryId}/menus")
    public ResponseEntity<ApiResponse<Void>> createMenu(@PathVariable("categoryId") Long categoryId, @Valid @RequestBody MenuCreateRequest request) {
        var menuId = menuService.createMenu(categoryId, request);
        var uri = URI.create(String.format("/api/owner/v1/categories/%d/menus/%d", categoryId, menuId));
        return ResponseEntity.created(uri)
                .body(ApiResponse.created(null));
    }

    @PostMapping("/api/owner/v1/shops/{shopId}/categories")
    public ResponseEntity<ApiResponse<Void>> createMenuCategory(@PathVariable("shopId") Long shopId, @Valid @RequestBody MenuCategoryCreateRequest request) {
        var menuCategoryId = menuService.createMenuCategory(shopId, request);
        var uri = URI.create(String.format("/api/owner/v1/shops/%d/categories/%d", shopId, menuCategoryId));
        return ResponseEntity.created(uri)
                .body(ApiResponse.created(null));
    }
}