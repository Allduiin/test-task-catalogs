package test.task.catalogs.model.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import test.task.catalogs.model.Catalog;

@Getter
@NoArgsConstructor
public class CatalogRequestDto {
    private Long id;
    private String name;
    private Long fatherId;
    private List<Catalog> childCatalogs;
}
