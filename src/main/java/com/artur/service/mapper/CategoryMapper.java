package com.artur.service.mapper;

import com.artur.entity.Category;
import com.artur.types.CategoryType;
import com.artur.utils.InstantMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {InstantMapper.class})
public class CategoryMapper {

    public CategoryType map(Category category){
        return category.getCategoryName();
    }
}
