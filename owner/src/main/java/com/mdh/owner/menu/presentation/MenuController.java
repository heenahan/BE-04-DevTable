package com.mdh.owner.menu.presentation;

import com.mdh.owner.global.ApiResponse;
import com.mdh.owner.menu.application.MenuService;
import com.mdh.owner.menu.presentation.dto.MenuCategoryCreateRequest;
import com.mdh.owner.menu.presentation.dto.MenuCategoryUpdateRequest;
import com.mdh.owner.menu.presentation.dto.MenuCreateRequest;
import com.mdh.owner.menu.presentation.dto.MenuUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class MenuController {

    private final MenuService menuService;

    // TODO Location header로 변경
    @PostMapping("/api/owner/v1/categories/{categoryId}/menus")
    public ResponseEntity<ApiResponse<Void>> createMenu(@PathVariable("categoryId") Long categoryId, @Valid @RequestBody MenuCreateRequest request) {
        var menuId = menuService.createMenu(categoryId, request);
        var uri = URI.create(String.format("/api/owner/v1/categories/%d/menus/%d", categoryId, menuId));
        return ResponseEntity.created(uri)
                .body(ApiResponse.created(null));
    }

    @PatchMapping("/api/owner/v1/menus/{menuId}")
    public ResponseEntity<ApiResponse<Void>> updateMenu(@PathVariable("menuId") Long menuId, @Valid @RequestBody MenuUpdateRequest request) {
        menuService.updateMenu(menuId, request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    // TODO Location header로 변경
    @PostMapping("/api/owner/v1/shops/{shopId}/categories")
    public ResponseEntity<ApiResponse<Void>> createMenuCategory(@PathVariable("shopId") Long shopId, @Valid @RequestBody MenuCategoryCreateRequest request) {
        var menuCategoryId = menuService.createMenuCategory(shopId, request);
        var uri = URI.create(String.format("/api/owner/v1/shops/%d/categories/%d", shopId, menuCategoryId));
        return ResponseEntity.created(uri)
                .body(ApiResponse.created(null));
    }

    @PatchMapping("/api/owner/v1/shops/categories/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> updateMenuCategory(@PathVariable("categoryId") Long categoryId, @Valid @RequestBody MenuCategoryUpdateRequest request) {
        menuService.updateMenuCategory(categoryId, request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @DeleteMapping("/api/owner/v1/shops/categories/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteMenuCategory(@PathVariable("categoryId") Long categoryId) {
        menuService.deleteMenuCategory(categoryId);
        return new ResponseEntity<>(ApiResponse.noContent(null), HttpStatus.NO_CONTENT);
    }
}
