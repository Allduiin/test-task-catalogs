package test.task.catalogs.repositories;

import test.task.catalogs.model.Catalog;

public interface CatalogRepository {
    Catalog add(Catalog catalog);

    Catalog update(Catalog catalog);

    Catalog get(Long id);

    Catalog delete(Long id);
}
