package test.task.catalogs.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Catalog fatherCatalog;
    @OneToMany
    private List<Catalog> childCatalogs;
    private Long count;
}
