package org.tenten.tentenbe.domain.category.service;

import org.springframework.stereotype.Service;
import org.tenten.tentenbe.domain.category.dto.response.CategoryResponse;
import org.tenten.tentenbe.global.common.enums.Category;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {
    public List<CategoryResponse> getCategory() {
        return Arrays.stream(Category.values()).map(category -> {
            return new CategoryResponse(category.getCode(), category.getName());
        }).toList();
    }
}
