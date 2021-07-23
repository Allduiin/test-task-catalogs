package test.task.catalogs.service;

import test.task.catalogs.model.Catalog;

public interface CatalogService {
    Catalog create(Catalog catalog);

    Catalog update(Catalog catalog);

    Catalog getById(Long id);

    Catalog delete(Long id);
}
