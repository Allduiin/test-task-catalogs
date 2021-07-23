package test.task.catalogs.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    private Catalog fatherCatalog;
    @OneToMany
    private List<Catalog> childCatalogs;
    @Transient
    private Long count;

    public Long getFatherId() {
        return fatherCatalog.getId();
    }

    public Long getCount() {
        return childCatalogs.size() + childCatalogs.stream()
                .mapToLong(Catalog::getCount)
                .sum();
    }
}
