package test.task.catalogs.mapper;

import org.springframework.stereotype.Component;
import test.task.catalogs.model.Catalog;
import test.task.catalogs.model.dto.CatalogRequestDto;
import test.task.catalogs.model.dto.CatalogResponseDto;

@Component
public class CatalogMapper {

    public Catalog getCatalogFromCatalogRequestDto(CatalogRequestDto catalogRequestDto) {
        Catalog catalog = new Catalog();
        catalog.setName(catalogRequestDto.getName());
        catalog.setFatherId(catalogRequestDto.getFatherId());
        catalog.setChildCatalogs(catalogRequestDto.getChildCatalogs());
        return catalog;
    }

    public CatalogResponseDto getCatalogResponseDtoFromCatalog(Catalog catalog) {
        CatalogResponseDto catalogResponseDto = new CatalogResponseDto();
        catalogResponseDto.setId(catalog.getId());
        catalogResponseDto.setName(catalog.getName());
        catalogResponseDto.setFatherId(catalog.getFatherId());
        catalogResponseDto.setChildCatalogs(catalog.getChildCatalogs());
        catalogResponseDto.setCount(catalog.getCount());
        return catalogResponseDto;
    }
}
