package ai.openfabric.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Hritik Chaudhary
 */
@Entity
public class DockerMount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String source;

    @Getter
    @Setter
    private String destination;

    @Getter
    @Setter
    private String driver;

    @Getter
    @Setter
    private String mode;

    @Getter
    @Setter
    private boolean rw;

    @Getter
    @Setter
    private String propagation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    @Getter
    @Setter
    @JsonIgnore
    private Worker worker;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DockerMount that = (DockerMount) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
