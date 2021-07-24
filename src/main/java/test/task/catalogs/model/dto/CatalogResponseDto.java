package test.task.catalogs.model.dto;

import java.util.List;
import lombok.Data;
import test.task.catalogs.model.Catalog;

@Data
public class CatalogResponseDto {
    private Long id;
    private String name;
    private Long fatherId;
    private List<Catalog> childCatalogs;
    private Long count;
}
