package test.task.catalogs.service.impl;

import lombok.AllArgsConstructor;
import test.task.catalogs.model.Catalog;
import test.task.catalogs.repositories.CatalogRepository;
import test.task.catalogs.service.CatalogService;

@AllArgsConstructor
public class CatalogServiceImpl implements CatalogService {
    private final CatalogRepository catalogRepository;

    @Override
    public Catalog create(Catalog catalog) {
        if (catalog.getFatherCatalog() != null) {
            catalog = catalogRepository.add(catalog);
            addChild(catalog.getFatherId(), catalog.getId());
            return catalog;
        }
        return catalogRepository.add(catalog);
    }

    @Override
    public Catalog update(Catalog catalog) {
        Catalog oldCatalog = get(catalog.getId());
        if (oldCatalog.getFatherCatalog() == null) {
            if (catalog.getFatherCatalog() != null) {
                addChild(catalog.getFatherId(), catalog.getId());
            }
        } else {
            if (!catalog.getFatherId().equals(oldCatalog.getFatherId())) {
                removeChild(oldCatalog.getFatherId(), catalog.getId());
                addChild(catalog.getFatherId(), catalog.getId());
            }
        }
        return catalogRepository.update(catalog);
    }

    @Override
    public Catalog get(Long id) {
        return catalogRepository.get(id);
    }

    @Override
    public Catalog delete(Long id) {
        Catalog catalog = catalogRepository.get(id);
        catalog.getFatherCatalog().getChildCatalogs().remove(catalog);
        update(catalog.getFatherCatalog());
        if (catalog.getChildCatalogs().isEmpty()) {
            return catalogRepository.delete(catalog);
        }
        throw new RuntimeException("Catalog " + catalog
                + " can not be deleted because it have inner catalogs");
    }

    private void removeChild(Long fatherId, Long childId) {
        catalogRepository.removeChild(fatherId, childId);
    }

    private void addChild(Long fatherId, Long childId) {
        catalogRepository.addChild(fatherId, childId);
    }

}
