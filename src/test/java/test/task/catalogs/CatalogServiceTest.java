package test.task.catalogs;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import test.task.catalogs.exceptions.ServiceProcessingException;
import test.task.catalogs.model.Catalog;
import test.task.catalogs.repositories.CatalogRepository;
import test.task.catalogs.service.CatalogService;
import test.task.catalogs.service.impl.CatalogServiceImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CatalogServiceTest {
    private static CatalogRepository catalogRepository;
    private static CatalogService catalogService;
    private static final String CATALOG_NAME = "TEST_NAME";
    private static final String CATALOG_NAME2 = "TEST_NAME2";
    private static final Long CATALOG_ID = 1L;
    private static final Long FATHER_ID = 2L;
    private static final Long OLD_FATHER_ID = 3L;

    @Before
    public void restartMock() {
        catalogRepository = mock(CatalogRepository.class);
        catalogService = new CatalogServiceImpl(catalogRepository);
    }

    @Test
    public void createWithoutFatherTest() {
        Catalog catalog = new Catalog();
        catalog.setName(CATALOG_NAME);
        Catalog resultCatalog = new Catalog();
        resultCatalog.setName(CATALOG_NAME);
        resultCatalog.setId(CATALOG_ID);
        when(catalogRepository.add(catalog)).thenReturn(resultCatalog);
        Assert.assertEquals("method not return added catalog"
                , resultCatalog, catalogService.create(catalog));
    }

    @Test
    public void createWithFatherTest() {
        Catalog catalog = new Catalog();
        catalog.setName(CATALOG_NAME);
        catalog.setFatherId(FATHER_ID);

        Catalog resultCatalog = new Catalog();
        resultCatalog.setName(CATALOG_NAME);
        resultCatalog.setId(CATALOG_ID);
        resultCatalog.setFatherId(FATHER_ID);

        Catalog actualFatherCatalog = new Catalog();
        actualFatherCatalog.setChildCatalogs(new ArrayList<>());

        when(catalogRepository.add(catalog)).thenReturn(resultCatalog);
        when(catalogRepository.getById(CATALOG_ID)).thenReturn(resultCatalog);
        when(catalogRepository.getById(FATHER_ID)).thenReturn(actualFatherCatalog);
        catalog = catalogService.create(catalog);
        Assert.assertEquals("method not return added catalog",
                resultCatalog, catalog);
        Assert.assertEquals("Child catalog must be added to father catalog",
                1, actualFatherCatalog.getChildCatalogs().size());
        verify(catalogRepository).update(Mockito.any());
    }

    @Test
    public void updateOldFatherNullNewFatherNullTest() {
        Catalog catalog = new Catalog();
        catalog.setName(CATALOG_NAME);
        catalog.setId(CATALOG_ID);
        Catalog oldCatalog = new Catalog();
        oldCatalog.setId(CATALOG_ID);
        oldCatalog.setName(CATALOG_NAME2);
        when(catalogRepository.getById(CATALOG_ID)).thenReturn(oldCatalog);
        when(catalogRepository.update(catalog)).thenReturn(catalog);
        Assert.assertEquals("method not return updated catalog"
                , catalog, catalogService.update(catalog));
    }

    @Test
    public void updateOldFatherNullNewFatherNotNullTest() {
        Catalog catalog = new Catalog();
        catalog.setName(CATALOG_NAME);
        catalog.setFatherId(FATHER_ID);
        catalog.setId(CATALOG_ID);

        Catalog oldCatalog = new Catalog();
        oldCatalog.setId(CATALOG_ID);
        oldCatalog.setName(CATALOG_NAME2);

        Catalog newFather = new Catalog();
        newFather.setChildCatalogs(new ArrayList<>());

        when(catalogRepository.getById(FATHER_ID)).thenReturn(newFather);
        when(catalogRepository.getById(CATALOG_ID)).thenReturn(oldCatalog, catalog);
        when(catalogRepository.update(catalog)).thenReturn(catalog);
        when(catalogRepository.update(newFather)).thenReturn(Mockito.any());

        Catalog expected = catalog;

        catalog = catalogService.update(catalog);

        Assert.assertEquals("method not return updated catalog",
                expected, catalog);
        Assert.assertEquals("Updated catalog must be added to new father catalog",
                1, newFather.getChildCatalogs().size());
        verify(catalogRepository).update(newFather);
    }

    @Test
    public void updateOldFatherNotNullNewFatherNullTest() {
        Catalog catalog = new Catalog();
        catalog.setName(CATALOG_NAME);
        catalog.setId(CATALOG_ID);

        Catalog oldCatalog = new Catalog();
        oldCatalog.setId(CATALOG_ID);
        oldCatalog.setName(CATALOG_NAME2);
        oldCatalog.setFatherId(OLD_FATHER_ID);

        Catalog oldFather = new Catalog();
        oldFather.setChildCatalogs(new ArrayList<>(List.of(oldCatalog)));


        when(catalogRepository.getById(CATALOG_ID)).thenReturn(oldCatalog);
        when(catalogRepository.getById(OLD_FATHER_ID)).thenReturn(oldFather);
        when(catalogRepository.update(oldFather)).thenReturn(Mockito.any());
        when(catalogRepository.update(catalog)).thenReturn(catalog);

        Catalog actualCatalog = catalogService.update(catalog);

        Assert.assertEquals("Updated catalog must be removed from old father catalog",
                0, oldFather.getChildCatalogs().size());
        Assert.assertEquals("method not return updated catalog",
                catalog, actualCatalog);
        verify(catalogRepository).update(oldFather);
    }

    @Test
    public void updateOldFatherNotNullNewFatherNotNullTest() {
        Catalog catalog = new Catalog();
        catalog.setName(CATALOG_NAME);
        catalog.setFatherId(FATHER_ID);
        catalog.setId(CATALOG_ID);

        Catalog oldCatalog = new Catalog();
        oldCatalog.setId(CATALOG_ID);
        oldCatalog.setName(CATALOG_NAME2);
        oldCatalog.setFatherId(OLD_FATHER_ID);

        Catalog oldFather = new Catalog();
        oldFather.setChildCatalogs(new ArrayList<>(List.of(oldCatalog)));

        Catalog newFather = new Catalog();
        newFather.setChildCatalogs(new ArrayList<>());

        when(catalogRepository.getById(CATALOG_ID)).thenReturn(oldCatalog);
        when(catalogRepository.getById(OLD_FATHER_ID)).thenReturn(oldFather);
        when(catalogRepository.getById(FATHER_ID)).thenReturn(newFather);
        when(catalogRepository.update(oldFather)).thenReturn(Mockito.any());
        when(catalogRepository.update(catalog)).thenReturn(catalog);

        Catalog actualCatalog = catalogService.update(catalog);

        Assert.assertEquals("Updated catalog must be removed from old father catalog",
                0, oldFather.getChildCatalogs().size());
        Assert.assertEquals("Updated catalog must be added to new father catalog",
                1, newFather.getChildCatalogs().size());

        verify(catalogRepository).update(oldFather);
        verify(catalogRepository).update(newFather);

        Assert.assertEquals("method not return updated catalog",
                catalog, actualCatalog);
    }

    @Test
    public void updateOldFatherEqualsNewFatherTest() {
        Catalog catalog = new Catalog();
        catalog.setName(CATALOG_NAME);
        catalog.setFatherId(FATHER_ID);
        catalog.setId(CATALOG_ID);

        Catalog oldCatalog = new Catalog();
        oldCatalog.setId(CATALOG_ID);
        oldCatalog.setName(CATALOG_NAME2);
        oldCatalog.setFatherId(FATHER_ID);

        when(catalogRepository.getById(CATALOG_ID)).thenReturn(oldCatalog);
        when(catalogRepository.update(catalog)).thenReturn(catalog);

        Catalog actualCatalog = catalogService.update(catalog);
        Assert.assertEquals("method not return updated catalog",
                catalog, actualCatalog);
    }

    @Test
    public void getByIdTest() {
        Catalog catalog = new Catalog();
        catalog.setId(CATALOG_ID);

        when(catalogRepository.getById(catalog.getId())).thenReturn(catalog);

        Catalog actualCatalog = catalogService.getById(catalog.getId());
        Assert.assertEquals("method not return updated catalog",
                catalog, actualCatalog);
    }

    @Test
    public void deleteRootCatalogNormalTest() {
        Catalog catalog = new Catalog();
        catalog.setId(CATALOG_ID);

        when(catalogRepository.getById(CATALOG_ID)).thenReturn(catalog);
        when(catalogRepository.delete(catalog)).thenReturn(catalog);
        Catalog actualCatalog = catalogService.delete(catalog.getId());
        verify(catalogRepository).delete(catalog);
        Assert.assertEquals("method not return updated catalog",
                catalog, actualCatalog);
    }

    @Test(expected = ServiceProcessingException.class)
    public void deleteRootCatalogWithInnerCatalogsTest() {
        Catalog catalog = new Catalog();
        Catalog childCatalog = new Catalog();
        catalog.setChildCatalogs(new ArrayList<>(List.of(childCatalog)));

        when(catalogRepository.getById(CATALOG_ID)).thenReturn(catalog);
        when(catalogRepository.delete(catalog)).thenReturn(catalog);
        catalogService.delete(CATALOG_ID);
    }

    @Test
    public void deleteCatalogWithFatherTest() {
        Catalog catalog = new Catalog();
        catalog.setId(CATALOG_ID);
        catalog.setFatherId(FATHER_ID);

        Catalog father = new Catalog();
        father.setChildCatalogs(new ArrayList<>(List.of(catalog)));

        when(catalogRepository.getById(CATALOG_ID)).thenReturn(catalog);
        when(catalogRepository.getById(FATHER_ID)).thenReturn(father);
        when(catalogRepository.delete(catalog)).thenReturn(catalog);
        Catalog actualCatalog = catalogService.delete(catalog.getId());
        verify(catalogRepository).delete(catalog);

        Assert.assertEquals("Deleted catalog must be removed from father catalog",
                0, father.getChildCatalogs().size());
        Assert.assertEquals("method not return updated catalog",
                catalog, actualCatalog);
    }
}