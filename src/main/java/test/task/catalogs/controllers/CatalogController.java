package test.task.catalogs.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import test.task.catalogs.mapper.CatalogMapper;
import test.task.catalogs.model.dto.CatalogRequestDto;
import test.task.catalogs.model.dto.CatalogResponseDto;
import test.task.catalogs.service.CatalogService;

@RestController
@RequestMapping("/catalog")
@AllArgsConstructor
public class CatalogController {
    private final CatalogService catalogService;
    private final CatalogMapper catalogMapper;

    @PostMapping("/add")
    public void addCatalog(@RequestBody CatalogRequestDto catalogRequestDto) {
        catalogService.create(catalogMapper.getCatalogFromCatalogRequestDto(catalogRequestDto));
    }

    @PostMapping("/update")
    public void updateCatalog(@RequestBody CatalogRequestDto catalogRequestDto) {
        catalogService.update(catalogMapper.getCatalogFromCatalogRequestDto(catalogRequestDto));
    }

    @GetMapping("/get-by-Id")
    public CatalogResponseDto getById(@RequestParam Long id) {
        return catalogMapper.getCatalogResponseDtoFromCatalog(catalogService.getById(id));
    }

    @GetMapping("/delete")
    public CatalogResponseDto delete(@RequestParam Long id) {
        return catalogMapper.getCatalogResponseDtoFromCatalog(catalogService.delete(id));
    }
}
