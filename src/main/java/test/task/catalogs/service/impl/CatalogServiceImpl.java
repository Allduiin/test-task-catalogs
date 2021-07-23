package test.task.catalogs.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import test.task.catalogs.model.Catalog;
import test.task.catalogs.repositories.CatalogRepository;
import test.task.catalogs.service.CatalogService;

@AllArgsConstructor
@Service
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
        Catalog oldCatalog = getById(catalog.getId());
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
    public Catalog getById(Long id) {
        return catalogRepository.getById(id);
    }

    @Override
    public Catalog delete(Long id) {
        Catalog catalog = catalogRepository.getById(id);
        if (catalog.getCount() == 0) {
            removeChild(catalog.getFatherId(), catalog.getId());
            update(catalog.getFatherCatalog());
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
