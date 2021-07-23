package test.task.catalogs;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import test.task.catalogs.config.AppConfig;
import test.task.catalogs.model.Catalog;
import test.task.catalogs.service.CatalogService;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);
        CatalogService catalogService = context.getBean(CatalogService.class);

        Catalog catalog = new Catalog();
        catalog.setName("Nadoelo");

        catalog = catalogService.create(catalog);

        Catalog catalogChild = new Catalog();
        catalogChild.setName("Vasa");
        catalogChild.setFatherCatalog(catalog);
        catalogService.create(catalogChild);
        System.out.println("All works");
    }
}
