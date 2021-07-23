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
        if (catalog.getFatherId() != null) {
            catalog = catalogRepository.add(catalog);
            addChild(catalog.getFatherId(), catalog.getId());
            return catalog;
        }
        return catalogRepository.add(catalog);
    }

    @Override
    public Catalog update(Catalog catalog) {
        Catalog oldCatalog = getById(catalog.getId());
        if (oldCatalog.getFatherId() == null) {
            if (catalog.getFatherId() != null) {
                addChild(catalog.getFatherId(), catalog.getId());
            }
        } else {
            if (catalog.getFatherId() != null) {
                if (!catalog.getFatherId().equals(oldCatalog.getFatherId())) {
                    addChild(catalog.getFatherId(), catalog.getId());
                    removeChild(oldCatalog.getFatherId(), catalog.getId());
                }
            } else {
                removeChild(oldCatalog.getFatherId(), catalog.getId());
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
            if (catalog.getFatherId() != null) {
                removeChild(catalog.getFatherId(), catalog.getId());
            }
            return catalogRepository.delete(catalog);
        }
        throw new RuntimeException("Catalog " + catalog
                + " can not be deleted because it have inner catalogs");
    }

    private void removeChild(Long fatherId, Long childId) {
        Catalog father = catalogRepository.getById(fatherId);
        father.getChildCatalogs().removeIf(c -> (c.getId().equals(childId)));
        catalogRepository.update(father);
    }

    private void addChild(Long fatherId, Long childId) {
        Catalog father = catalogRepository.getById(fatherId);
        Catalog child = catalogRepository.getById(childId);
        father.getChildCatalogs().add(child);
        catalogRepository.update(father);
    }

}
